package com.xbreeze.license;

import java.io.ByteArrayInputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.xbreeze.util.StringHelper;

/**
 * LicensedClassLoader does a verification on license information and remotely
 * loads the class and the appropriate classes that are invoked from within the
 * remotely loaded class.
 * 
 * @author Willem
 */
public class LicensedClassLoader extends ClassLoader {
	// The class logger.
	private static final Logger logger = Logger.getLogger(LicensedClassLoader.class.getName());

	private static final String XBREEZE_BASE_PACKAGE = "com.xbreeze.";

	private LicenseConfig _config;
	private ClassLoader _parent;
	private Random rnd;
	private String typeOfUse;

	/***
	 * Constructor
	 * 
	 * @param parent
	 *            the invoking ClassLoader
	 * @param config
	 *            config object with license information
	 * @param debugMode
	 *            whether or not running in debug mode
	 * @throws LicenseException
	 *             error in case of invalid license
	 */
	public LicensedClassLoader(ClassLoader parent, LicenseConfig config, Boolean debugMode) throws LicenseException {
		super(parent);
		this._parent = parent;
		this._config = config;

		this.rnd = new Random();
		this.typeOfUse = debugMode ? "Debug" : "Normal";
		
		// Check the required license attributes

		// Check whether the license key is specified.
		if (StringHelper.isEmptyOrWhitespace(_config.getLicenseKey())) {
			throw new LicenseException("Licensing key not specified");
		}

		// Check whether the url is specified.
		if (StringHelper.isEmptyOrWhitespace(_config.getUrl().toString())) {
			throw new LicenseException("Licensing url not specified");
		}
		// Check whether the license url scheme is https (only when not in developer mode).
		else if (!_config.getDeveloperMode() && !_config.getUrl().getScheme().equals("https")) {
			throw new LicenseException("Licensing url scheme must be HTTPS");
		}

		// Check whether the version is specified.
		if (StringHelper.isEmptyOrWhitespace(config.getVersion())) {
			throw new LicenseException("Licensing application version not specified");
		}

		// Check whether the contract id is specified.
		if (StringHelper.isEmptyOrWhitespace(_config.getContractId())) {
			throw new LicenseException("Contract key not specified");
		}

		// When running in developer mode output informational and skip license check.
		if (_config.getDeveloperMode()) {
			logger.setLevel(Level.INFO);
			logger.info("Notice: Running in developer mode");
		} else {
			logger.setLevel(Level.WARNING);
			// If the license is invalid, throw an exception.
			if (!validateLicense()) {
				throw new LicenseException("License invalid");
			}
		}
	}

	/***
	 * overridden loadClass to load class from either remote or local network
	 * location.
	 */
	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		// If the class which needs to be loaded is not a com.xbreeze class, let the
		// parent handle it.
		if (handleByParent(className))
			return this._parent.loadClass(className);

		// if className does not start with com.xbreeze, let parent ClassLoader handle
		// the load
		// First let the parent ClassLoader try to handle it.
		try {
			return _parent.loadClass(className);
		}

		// If the parent ClassLoader can't handle loading of the class we use the remote
		// class loading.
		catch (ClassNotFoundException cnfe) {

			// Create the class file location using the class full name.
			String classFileLocation = String.format("%s.class", className.replace(".", "/"));
			// Get the binary representation of the class.
			byte[] data = getClassOrResourceData(classFileLocation);

			// If the class wasn't found, re-throw the ClassNotFoundException.
			if (data == null)
				throw cnfe;

			// Create the class object using the data array.
			Class<?> c = super.defineClass(className, data, 0, data.length);

			// If the package information is missing, try to set it.
			// TODO HW: Why would this be missing?
			if (c.getPackage() == null) {
				definePackage(className.replaceAll("\\.\\w+$", ""), null, null, null, null, null, null, null);
			}

			// Link the class.
			// TODO HW: Is this so every class is only loaded once in a runtime?
			resolveClass(c);

			// Return the class.
			return c;
		}

		// }
		// TODO HW: Why let the parent handle the class if there is a IOException?
		// catch (IOException ex) {
		// // Let parent resolve this class
		// logger.warning(String.format("error loading class %s: %s", name,
		// ex.getLocalizedMessage()));
		// return this._parent.loadClass(name);
		// }
	}

	@Override
	public InputStream getResourceAsStream(String resourceName) {
		logger.info(String.format("Trying to load resource %s", resourceName));
		if (handleByParent(resourceName))
			return super.getResourceAsStream(resourceName);

		// Create a byte array for storing the resource bytes in.
		byte[] data = getClassOrResourceData(resourceName);

		// If the resource wasn't found, return null.
		if (data == null)
			return null;

		return new ByteArrayInputStream(data);
	}

	/**
	 * Check whether the class or resource should be handled by the parent. This
	 * must happen when the package of the class or resource is not within a XBreeze
	 * package.
	 * 
	 * @return True when the class or resource should be handled by the parent
	 *         ClassLoader
	 */
	private boolean handleByParent(String name) {
		return !name.replace("/", ".").startsWith(XBREEZE_BASE_PACKAGE);
	}

	/***
	 * Validates license by checking license key and token
	 * 
	 * @return true when license is valid, false otherwise
	 */
	private boolean validateLicense() {
		int retry = 0;
		int statusCode = 204;
		// Try to get the license info 3 times at max.
		while (statusCode == 204 && retry < 3) {
			LicenseToken t = new LicenseToken(this._config.getContractId(), this.rnd);
			String token = t.getToken();
			String sign = t.getSignature();
			String systemID = getSystemID();
			String message = "{\"method\":\"validate\",\"licensekey\":\"" + _config.getLicenseKey() + "\",\"token\":\""
					+ token + "\",\"signature\":\"" + sign + "\",\"tag\":\"" + this._config.getTag()
					+ "\",\"systemid\":\"" + systemID + "\",\"typeofuse\":\"" + typeOfUse + "\"}";
			// String message = "{\"method\":\"validate\",\"licensekey\":\"" +licenseKey +
			// "\",\"token\":\""+ token +"\",\"signature\":\""+ sign + "\"}";
			
			CloseableHttpResponse licenseResponse;
			try {
				licenseResponse = performRequest(_config.getUrl(), message);
			} catch (LicenseException e) {
				logger.severe(String.format("Error occured during license check: %s", e.getMessage()));
				return false;
			}
			// Set the status code.
			statusCode = licenseResponse.getStatusLine().getStatusCode();
			// Set the response message.
			String responseMessage = null;
			if (licenseResponse.getEntity() != null) {
				try {
					responseMessage = EntityUtils.toString(licenseResponse.getEntity());
				} catch (ParseException | IOException e) {
					logger.severe(String.format("Error while getting response body: %s", e.getMessage()));
					return false;
				}
			}
			// If there was no response body, we will set the message to the status reason phrase.
			if (responseMessage == null || responseMessage.length() == 0) {
				responseMessage = licenseResponse.getStatusLine().getReasonPhrase();
			}
			
			try {
				licenseResponse.close();
			} catch (IOException e) {
				logger.severe(String.format("Error while closing HTTP connection: %s", e.getMessage()));
				return false;
			}
			
			// Switch on the response code.
			switch (statusCode) {
			case 200:
				logger.info("License check OK");
				return true;
				
			case 204:
				retry++;
				logger.severe("Invalid token/signature, retry " + String.valueOf(retry));					
				break;
				
			case 205:
				logger.severe("License key is not valid.");
				return false;
				
			default:
				logger.severe(String.format("License check failed: %d - %s", statusCode, responseMessage));
				return false;
			}
		}

		return false;
	}

	private byte[] getClassOrResourceData(String resourceLocation) {
		// If we are in developer mode, we try to find the class on the local machine.
		if (this._config.getDeveloperMode()) {
			// The resource location will be using a forward slash (/) for remote loading.
			// When the files are loaded locally we have the flip the slashes and make it relative to the config url.
			Path localClassPath = Paths.get(this._config.getUrl()).resolve(resourceLocation.replace("/", "\\"));
			logger.info(String.format("Loading local class or resource %s", localClassPath));
			try {
				return Files.readAllBytes(localClassPath);
			} catch (IOException e) {
				logger.severe(String.format("Couldn't load using local path: %s: %s", localClassPath, e.getMessage()));
				// Return null, so its clear the class or resource couldn't be found.
				return null;
			}
		}
		// Load from network location
		else {
			// Create the class file name by replacing the . with a / and add .class at the
			// end.
			logger.info(String.format("Loading remote class or resource %s", resourceLocation));
			try {
				return getClassOrResourceFromNetwork(resourceLocation);
			} catch (LicenseException e) {
				logger.info(String.format("Couldn't load using remote path: %s: %s", resourceLocation, e.getMessage()));
				// Return null, so its clear the class or resource couldn't be found.
				return null;
			}
		}
	}

	/***
	 * Reads a class from the remote network location.
	 * 
	 * @param classfile
	 *            The file to load
	 * @return the loaded class
	 * @throws LicenseException 
	 */
	private byte[] getClassOrResourceFromNetwork(String classfile) throws LicenseException {
		// Store the number of retries.
		int retry = 0;
		// Initialize the status code to 204 (No Content)
		// TODO HW: Why 204?
		// TODO Make getting binary data from remote generic. Same code now in multiple functions to get remote binary.
		int statusCode = 204;
		// Try to get the class at maximum 3 times.
		while (statusCode == 204 && retry < 3) {
			LicenseToken t = new LicenseToken(this._config.getContractId(), this.rnd);
			String token = t.getToken();
			String sign = t.getSignature();
			String message = "{\"method\":\"getclass\",\"classfile\":\"" + classfile + "\",\"token\":\"" + token
					+ "\",\"signature\":\"" + sign + "\",\"version\":\"" + this._config.getVersion()
					+ "\",\"licensekey\":\"" + this._config.getLicenseKey() + "\"}";
			
			CloseableHttpResponse classOrResourceResponse = performRequest(_config.getUrl(), message);
			
			// Get the status code.
			statusCode = classOrResourceResponse.getStatusLine().getStatusCode();
			if (statusCode == 204) {
				retry++;
				try {
					classOrResourceResponse.close();
				} catch (IOException e) {
					logger.severe(String.format("Error while closing HTTP connection: %s", e.getMessage()));
				}
				logger.warning("Invalid token/signature, retry " + String.valueOf(retry));
			}
			else if (statusCode == 200) {
				// Response contains classfile
				HttpEntity httpEntity = classOrResourceResponse.getEntity();
				
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;
				byte[] data = new byte[65536];
				try {
					InputStream inputStream = httpEntity.getContent();
					while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
						buffer.write(data, 0, nRead);
					}
					buffer.flush();
					classOrResourceResponse.close();
				} catch (IOException e) {
					throw new LicenseException(String.format("Error while fetching remote class file: %s", e.getMessage()));
				}

				// Return the bytes of the class or resource.
				return buffer.toByteArray();
			}
		}
		
		throw new LicenseException("Error while fetching remote class file");
	}

	/***
	 * Creates connection for loading classes
	 * 
	 * @param length
	 *            content length of message sent, set as header
	 * @throws LicenseException 
	 */
	private CloseableHttpResponse performRequest(URI uri, String message) throws LicenseException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost request = new HttpPost(uri);
		StringEntity params;
		try {
			params = new StringEntity(message);
			request.setEntity(params);
		} catch (UnsupportedEncodingException e1) {
			// TODO Handle exception properly.
			logger.severe(e1.getMessage());
		}
		request.addHeader("Content-Type", "application/json; charset=UTF-8");
		
		try {
			return httpClient.execute(request);
		} catch (IOException e) {
			throw new LicenseException(String.format("Error while performing Http request: %s", e.getMessage()));
		}
	}

	/***
	 * Method (attempts) to generate a system id that can be used to monitor license
	 * usage for system id method attempts to read mac address of a network
	 * interface If not found, tries to read hostname
	 * 
	 * @return the system id
	 */
	private String getSystemID() {
		try {
			Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
			String id = "";
			while (ni.hasMoreElements() && id.equalsIgnoreCase("")) {
				NetworkInterface n = ni.nextElement();
				if (n.getHardwareAddress() != null && n.isVirtual() == false && n.isPointToPoint() == false
						&& n.isUp()) {
					for (int i = 0; i < n.getHardwareAddress().length; i++) {
						id = id.concat(Byte.toString(n.getHardwareAddress()[i])).concat(":");
					}
				}
			}
			return id;
		} catch (SocketException ex) {
			try {
				InetAddress addr;
				addr = InetAddress.getLocalHost();
				return addr.getHostName();
			} catch (UnknownHostException ex1) {
				return "Unkown host";
			}
		}
	}
}
