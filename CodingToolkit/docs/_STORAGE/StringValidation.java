package com.ibm.issac.toolkit.validation;

import com.ibm.issac.toolkit.logging.ColorLog;
public class StringValidation {
	/**
	 * 如果STRING中包含可读内容，返回真；否则为假。
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isStringReadable(String string) {
		if (string == null)
			return false;
		if ("".equals(string.trim()))
			return false;
		if ("null".equals(string.trim().toLowerCase())){
			ColorLog.warn("You have provided a string consisted of 4 characters: N-U-L-L. It's been treated as not-readable.");
			return false;
		}
		return true;
	}
}
