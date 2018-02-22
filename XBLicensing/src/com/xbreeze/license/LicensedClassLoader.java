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
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

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
	private HttpURLConnection connection;
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

		if (StringHelper.isEmptyOrWhitespace(_config.getLicenseKey())) {
			throw new LicenseException("Licensing key not specified");
		}

		if (StringHelper.isEmptyOrWhitespace(_config.getUrl())) {
			throw new LicenseException("Licensing url not specified");
		}

		if (StringHelper.isEmptyOrWhitespace(config.getVersion())) {
			throw new LicenseException("Licensing application version not specified");
		}

		if (StringHelper.isEmptyOrWhitespace(_config.getContractId())) {
			throw new LicenseException("Contract key not specified");
		}

		// Ensure url ends with a slash
		if (!_config.getUrl().endsWith("\\")) {
			_config.setUrl(_config.getUrl().concat("\\"));
		}

		// When running in developer mode output informational and skip license check.
		if (this._config.getDeveloperMode()) {
			logger.info("Notice: Running in developer mode");
		} else {
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
			
			// Create the connection.
			createConnection(message.length());
			String responseMessage = null;
			
			try {
				connection.connect();
				// Create a writer.
				OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
				// Write the message.
				writer.write(message);
				writer.close();
				// Get the status code.
				statusCode = connection.getResponseCode();
				responseMessage = connection.getResponseMessage();
			} catch (IOException e) {
				logger.severe(String.format("Error occured during license check: %s", e.getMessage()));
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
		// TODO If we use a proper URI object you can check whether the url is local or
		// remote.
		if (this._config.getDeveloperMode()) {
			// The resource location will be using a forward slash (/) for remote loading.
			// When the files are loaded locally we have the flip the slashes and make it relative to the config url.
			Path localClassPath = Paths.get(this._config.getUrl(), resourceLocation.replace("/", "\\"));
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
			} catch (IOException e) {
				logger.severe(
						String.format("Couldn't load using remote path: %s: %s", resourceLocation, e.getMessage()));
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
	 * @throws IOException
	 *             when class cannot be loaded
	 */
	private byte[] getClassOrResourceFromNetwork(String classfile) throws IOException {
		try {
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
				createConnection(message.length());
				connection.connect();
				OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
				writer.write(message);
				writer.close();

				statusCode = connection.getResponseCode();
				if (statusCode == 204) {
					retry++;
					logger.warning("Invalid token/signature, retry " + String.valueOf(retry));
				}
			}
			if (statusCode == 200) {
				// Response contains classfile
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;
				byte[] data = new byte[65536];
				while ((nRead = connection.getInputStream().read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}
				buffer.flush();

				return buffer.toByteArray();
				// byte[] output = new byte[connection.getInputStream().available()];
				// connection.getInputStream().read(output);

				// return output;
			} else {
				throw new IOException();
			}
		} catch (MalformedURLException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
			return null;
		}

	}

	/***
	 * Creates connection for loading classes
	 * 
	 * @param length
	 *            content length of message sent, set as header
	 */
	private void createConnection(int length) {
		try {
			HttpURLConnection con = null;
			URL url = new URL(this._config.getUrl());
			if (this._config.getUrl().toLowerCase().startsWith("https")) {
				con = (HttpsURLConnection) url.openConnection();
			} else {
				con = (HttpURLConnection) url.openConnection();
			}

			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestProperty("Content-Length", String.valueOf(length));
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			this.connection = con;
		} catch (MalformedURLException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
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
