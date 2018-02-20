package com.xbreeze.license;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
 * LicensedClassLoader does a verification on license information and remotely loads the class and the appropriate classes that are invoked from within the remotely loaded class.
 * @author Willem
 */
public class LicensedClassLoader extends ClassLoader {
    
    private LicenseConfig _config;
    private ClassLoader _parent;
    private HttpURLConnection connection;
    private Random rnd;
    private String typeOfUse;
    private static final Logger logger = Logger.getLogger(LicensedClassLoader.class.getName());
    
    /***
     * Constructor 
     * @param parent the invoking classloader
     * @param config config object with license information
     * @param debugMode wether or not running in debug mode
     * @throws LicenseError error in case of invalid license
     */
    public LicensedClassLoader(ClassLoader parent, LicenseConfig config, Boolean debugMode) throws LicenseError {
        super(parent);        
        this._parent = parent;
        this._config = config;
        
        this.rnd= new Random();
        this.typeOfUse = debugMode?"Debug":"Normal";
       
       //Check the required license attributes
        
        if (StringHelper.isEmptyOrWhitespace(_config.getLicenseKey())) {
            throw new LicenseError("Licensing key not specified" );
        }
        
        if (StringHelper.isEmptyOrWhitespace(_config.getUrl())) {
            throw new LicenseError("Licensing url not specified");
        }
        
        if (StringHelper.isEmptyOrWhitespace(config.getVersion())) {
            throw new LicenseError("Licensing application version not specified");
        }
        
        if (StringHelper.isEmptyOrWhitespace(_config.getContractId())) {
            throw new LicenseError("Contract key not specified");
        }
        
        //Ensure url ends with a slash
        if (!_config.getUrl().endsWith("\\") ) {
        	_config.setUrl(_config.getUrl().concat("\\"));
        }
        
        //When running in developermodel output notice and skip license check.
        if (this._config.getDeveloperMode()) {
           logger.info("Notice: Running in developer mode");
        }
        else {
                  
            if (!validateLicense()) {
                throw new LicenseError("License invalid");
            }
        }
    }
 
    /***
     * overridden loadClass to load class from either remote or local network location.
     */
   @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        
        try {
            byte[] data = null;
            
            //if classname does not start with com.xbreeze, let parent classloader handle the load
            if (!name.startsWith("com.xbreeze.") ) {
            	logger.info(String.format("loading of class %s is delegated to parent classloader", name));
            	return this._parent.loadClass(name);
            }
            
            //System.out.println("Loading Class '" + name + "'");
            if (this._config.getDeveloperMode()) {
                Path path = Paths.get(this._config.getUrl() + name.replace(".","\\") + ".class");
                logger.info(String.format("Loading class %s", path));
                data = Files.readAllBytes(path);
            }
            else //load from network location
            {                
                String classname = name.replace(".", "/") + ".class";
                data = getClassFromNetwork(classname);
            }    
            Class<?> c =  super.defineClass(name, data, 0, data.length);
            if (c.getPackage() == null) {
                definePackage(name.replaceAll("\\.\\w+$", ""), null, null, null, null, null, null, null);
            }
            resolveClass(c);
            return c;
        
        } catch (IOException ex) {
              //let parent resolve this class
        		logger.warning(String.format("error loading class %s: %s", name, ex.getLocalizedMessage()));
              return this._parent.loadClass(name);
        }
        
    }
    
   /***
    * Validates license by checking license key and token
    * @return true when license is valid, false otherwise
    */
    private boolean validateLicense() {
        try {
            int retry = 0;
            int statusCode  = 204;
            while (statusCode == 204 && retry < 3){
                LicenseToken t = new LicenseToken(this._config.getContractId(), this.rnd);
                String token = t.getToken();
                String sign = t.getSignature();      
                String systemID = getSystemID();
                String message = "{\"method\":\"validate\",\"licensekey\":\"" +_config.getLicenseKey() + "\",\"token\":\""+ token +"\",\"signature\":\""+ sign + "\",\"tag\":\"" + this._config.getTag() +"\",\"systemid\":\""+ systemID +"\",\"typeofuse\":\"" + typeOfUse + "\"}";                
                //String message = "{\"method\":\"validate\",\"licensekey\":\"" +licenseKey + "\",\"token\":\""+ token +"\",\"signature\":\""+ sign + "\"}";                
                createConnection(message.length());
                connection.connect();                
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(message);
                writer.close();
            
                statusCode = connection.getResponseCode();
                if (statusCode == 204) {
                    retry++;
                    System.out.println("Invalid token/signature, retry " + String.valueOf(retry));
                }
                if (statusCode == 205) {
                    System.out.println("License key is not valid.");
                    return false;
                }
            }
            if (statusCode == 200) {
                System.out.println("License check OK");
                return true;
            }
           return false;
            
            
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
    
    /***
     * Reads a class from the remote network location. 
     * @param classfile The file to load
     * @return the loaded class
     * @throws IOException when class cannot be loaded
     */
    private byte[] getClassFromNetwork(String classfile) throws IOException{
        try {      
            int retry = 0;
            int statusCode  = 204;
            while (statusCode == 204 && retry < 3){
            	LicenseToken t = new LicenseToken(this._config.getContractId(), this.rnd);
                String token = t.getToken();
                String sign = t.getSignature();                            
                String message = "{\"method\":\"getclass\",\"classfile\":\"" +classfile + "\",\"token\":\""+ token +"\",\"signature\":\""+sign + "\",\"version\":\"" + this._config.getVersion() + "\",\"licensekey\":\"" +this._config.getLicenseKey() + "\"}";
                //System.out.println(message);
                createConnection(message.length());
                connection.connect();                
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(message);
                writer.close();
            
                statusCode = connection.getResponseCode();
                if (statusCode == 204) {
                    retry++;
                    System.out.println("Invalid token/signature, retry " + String.valueOf(retry));
                }                
            }
            if (statusCode == 200) {
                //Response contains classfile  
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[65536];                
                while ((nRead = connection.getInputStream().read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();

                return buffer.toByteArray();
                //byte[] output = new byte[connection.getInputStream().available()];
                //connection.getInputStream().read(output);
                
                //return output;
            }
            else {
                throw new IOException();
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    /***
     * Creates connection for loading classes
     * @param length content length of message sent, set as header
     */
    private void createConnection(int length)  {
        try {            
            HttpURLConnection con = null;
            URL url = new URL(this._config.getUrl());            
            if (this._config.getUrl().toLowerCase().startsWith("https")) {
                con = (HttpsURLConnection) url.openConnection();
            }
            else {
                con = (HttpURLConnection)url.openConnection();
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
     * Method (attempts) to generate a system id that can be used to monitor license usage
     * for system id method attempts to read mac address of a network interface
     * If not found, tries to read hostname
     * @return the system id
     */
    private String getSystemID()  {
        try {
            Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
            String id ="";
            while (ni.hasMoreElements() && id.equalsIgnoreCase("")) {
                NetworkInterface n = ni.nextElement();
                if (n.getHardwareAddress() != null && n.isVirtual() == false && n.isPointToPoint() == false && n.isUp() ) {
                    for (int i = 0; i < n.getHardwareAddress().length;i++){
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

