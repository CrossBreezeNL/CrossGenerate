package com.xbreeze.license;

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
	private String _url;
	
	/***
	 * The version of software that is loaded
	 */
	
	private String _version;

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
	public String getUrl() {
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
	public void setLicenseKey(String _licenseKey) {
		this._licenseKey = _licenseKey;
	}
	public void setDeveloperMode(Boolean _inDeveloperMode) {
		this._inDeveloperMode = _inDeveloperMode;
	}
	public void setContractId(String _contractId) {
		this._contractId = _contractId;
	}
	public void setTag(String _tag) {
		this._tag = _tag;
	}
	public void setUrl(String _url) {
		this._url = _url;
	}
	public void setVersion(String _version) {
		this._version = _version;
	}
	
	

}
