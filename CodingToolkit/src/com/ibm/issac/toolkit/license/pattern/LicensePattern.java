package com.ibm.issac.toolkit.license.pattern;

public abstract class LicensePattern {
	
	/**
	 * 验证请求码是否拥有LICENSE
	 * @param requestCode
	 * @return
	 */
	public abstract boolean validate(String requestCode);
}
