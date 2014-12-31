package com.ibm.issac.toolkit.license;

import com.ibm.issac.toolkit.license.pattern.BasicDateValidationPattern;
import com.ibm.issac.toolkit.license.pattern.FlooredSqrtPattern;

/**
 * IRE系列的工具不允许任何用户使用。因此提供了一些授权方案。
 * @author issac
 *
 */
public final class LicenseValidation {

	/**
	 * 验证当前用户是否拥有LICENSE。
	 * @param licensePattern 不同的LICENSE验证类型
	 * @param requestCode 请求码，根据LICENSE验证类型的不同，请求码内容也会有区别
	 * @return 用户是否持有LICENSE
	 */
	public boolean isLicenseGranted(String licensePattern, String requestCode){
		if("BasicDateValidation_LicenseSurfix".equals(licensePattern))
			return new BasicDateValidationPattern().validate(requestCode);
		if("FlooredSqrtPattern".equals(licensePattern))
			return new FlooredSqrtPattern().validate(requestCode);
		throw new RuntimeException("License Pattern "+licensePattern+" does not exist.");
	}
}
