package com.ibm.issac.toolkit.license.pattern;

import java.util.Date;

import com.ibm.issac.toolkit.DatetimeUtility;
import com.ibm.issac.toolkit.logging.ColorLog;

/**
 * 通过对日期进行基本转换，确认请求码是否有效
 * 
 * @author issac
 * 
 */
public final class BasicDateValidationPattern extends LicensePattern {

	public boolean validate(String requestCode) {
		try {
			String currentDate = DatetimeUtility.formatDate("MMdd", new Date());
			final int dateInt = Integer.valueOf(currentDate).intValue();
			final int requestInt = Integer.valueOf(requestCode).intValue();
			if (requestInt == (dateInt*2-9)){
				ColorLog.debug("The ire number is valid.");
				return true;
			}
		} catch (Exception e) {
			ColorLog.warn("An error occurred while validating the IRE NUMBER.");
		}
		return false;
	}
}
