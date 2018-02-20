package com.xbreeze.license;

import java.util.Random;

/***
 * Token used to authenticate during license validation
 * @author Willem
 *
 */
public class LicenseToken {
	private String contractKey;
	private String token;
	private Random rnd;
	
	/***
	 * Licensetoken constructor, creates a token based on the given contracy key
	 * @param contractKey the contract key used as secret for the token.
	 * @param rnd randomizer
	 */
	public LicenseToken(String contractKey, Random rnd) {
		this.contractKey = contractKey;
		this.rnd = rnd;	
		this.token = generateToken();		
	}
	
	/***
	 * Genrates a token consisting of 10 random characters
	 * @return a generated token
	 */
	private String generateToken() {
	        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%^&*_()";
	        return generateString(rnd, characters, 10);
    }
	    
	/***
	 * Generates a string from a input of permitted charaters, lenght and random offset
	 * @param rng random used to pick a character
	 * @param characters set of permitted characters
	 * @param length no of characters used to generate the string
	 * @return
	 */
	private String generateString(Random rng, String characters, int length) {	        
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
	
	/***
	 * Helper method to convert output of a messagedigest to a hex string
	 * @param arrayBytes array containing a messagedigest
	 * @return the messagedigest converted to a hex string.
	 */
	private String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
	    
	/***
	 * Creates a hashed value of a string using SHA1
	 * @param x the string to hash
	 * @return the SHA-1 hash of the string, as a byte array
	 * @throws Exception when something goes wrong hashing the string
	 */
	private byte[] encrypt(String x) throws Exception {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return d.digest();
	}    
	   
	   
	/***
	 * gets the signature corresponding to this token
	 * @return a valid signature.
	 */
	public String getSignature() {
        try {
            return convertByteArrayToHexString(encrypt(token.concat(contractKey)));
        } catch (Exception ex) {
            return "";
        }
    }
	
	/***
	 * Returns the token string
	 */
	public String getToken() {
		return this.token;
	}

}
