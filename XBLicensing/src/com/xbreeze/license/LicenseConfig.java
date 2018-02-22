package com.xbreeze.license;

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;

/***
 * Class contains the license information for CrossBreeze products
 * @author Willem
 *
 */
public class LicenseConfig {
	
	/***
	 * The license key
	 */
	private String _licenseKey;
	
	/***
	 * indicates if running in developer mode
	 */
	private Boolean _inDeveloperMode = false;
	
	/***
	 * The contract key
	 */
	
	private String _contractId;
	
	/***
	 * An optional tag that can be supplied to track generation cycles
	 */
	private String _tag="";
	
	/***
	 * The url used to validate license info
	 */
	private URI _url;
	
	/***
	 * The version of software that is loaded
	 */
	private String _version;

	// Getters
	@XmlElement(name="LicenseKey", required=true)
	public String getLicenseKey() {
		return _licenseKey;
	}
	@XmlElement(name="ContractId", required=true)
	public String getContractId() {
		return _contractId;
	}

	@XmlElement(name="Tag", required=false)
	public String getTag() {
		return _tag;
	}

	@XmlElement(name="Url", required=true)
	public URI getUrl() {
		return _url;
	}

	@XmlElement(name="Version", required=true)
	public String getVersion() {
		return _version;
	}
	
	@XmlElement(name="DeveloperMode", required=false)
	public Boolean getDeveloperMode() {
		return _inDeveloperMode;
	}
	
	// Setters
	public void setLicenseKey(String licenseKey) {
		this._licenseKey = licenseKey;
	}
	public void setDeveloperMode(Boolean inDeveloperMode) {
		this._inDeveloperMode = inDeveloperMode;
	}
	public void setContractId(String contractId) {
		this._contractId = contractId;
	}
	public void setTag(String tag) {
		this._tag = tag;
	}
	public void setUrl(URI url) {
		this._url = url;
	}
	public void setVersion(String version) {
		this._version = version;
	}
}
