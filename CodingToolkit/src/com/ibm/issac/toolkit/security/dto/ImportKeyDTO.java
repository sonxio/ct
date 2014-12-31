package com.ibm.issac.toolkit.security.dto;

/**
 * 表示把一个KEY导入KEY STORE所需的各项参数
 * 
 * @author issac
 * 
 */
public final class ImportKeyDTO {
	private String keypass;
	private String defaultalias;
	private String keystorename;
	private String keyFileName;
	private String certFileName;

	public String getKeyFileName() {
		return keyFileName;
	}

	public void setKeyFileName(String keyFileName) {
		this.keyFileName = keyFileName;
	}

	public String getCertFileName() {
		return certFileName;
	}

	public void setCertFileName(String certFileName) {
		this.certFileName = certFileName;
	}

	public String getKeypass() {
		return keypass;
	}

	public void setKeypass(String keypass) {
		this.keypass = keypass;
	}

	public String getDefaultalias() {
		return defaultalias;
	}

	public void setDefaultalias(String defaultalias) {
		this.defaultalias = defaultalias;
	}

	public String getKeystorename() {
		return keystorename;
	}

	public void setKeystorename(String keystorename) {
		this.keystorename = keystorename;
	}

}
