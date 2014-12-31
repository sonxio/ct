package com.ibm.issac.toolkit.util;

import java.io.UnsupportedEncodingException;

import com.ibm.issac.toolkit.logging.ColorLog;
import com.ibm.issac.toolkit.validation.StringValidation;

public class StringUtil {
	
	/**
	 * 如果STRING中包含可读内容，返回真；否则为假。
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isReadable(String string) {
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
	
	
	/**
	 * 用String形式显示一个String数组
	 * 
	 * @param stringArray
	 * @return
	 */
	public static String flattenStringArray(String[] stringArray) {
		final StringBuffer sb = new StringBuffer("[");
		for (int i = 0; i < stringArray.length; i++) {
			if (i != 0)
				sb.append(',');
			sb.append(stringArray[i]);
		}
		sb.append(']');
		return sb.toString();
	}

	/**
	 * Returns the supplied string, up to (and excluding) the first newline character. If the supplied string is null, the output string is also null.
	 * 
	 * @param string
	 *            String to parse.
	 * @return String the first line of the input string.
	 */
	public static String getFirstLine(String string) {
		String retVal = null;
		if (string != null) {
			int firstNewline = string.indexOf('\n');
			if (firstNewline != -1) {
				retVal = string.substring(0, firstNewline);
			} else {
				retVal = string;
			}
		}
		return retVal;
	}

	/**
	 * 返回从开头到第一个空格的字符串
	 * 
	 * @param partStr
	 * @param delimeterStr
	 * @return
	 */
	public static String getSubStringEndingAtTheFirstDelimeter(String partStr, String delimeterStr) throws StringIndexOutOfBoundsException {
		return partStr.substring(0, partStr.indexOf(delimeterStr));
	}

	/**
	 * 返回从第一个空格开始到结尾的部分字符串
	 * 
	 * @param partStr
	 * @param delimeterStr
	 * @return
	 */
	public static String getSubStringBeginningFromTheFirstDelimeter(String partStr, String delimeterStr) throws StringIndexOutOfBoundsException {
		return partStr.substring(partStr.indexOf(delimeterStr) + delimeterStr.length(), partStr.length());
	}

	/**
	 * 获得两个分隔符之间的字符串
	 * 
	 * @param delimiterA
	 * @param delimiterB
	 * @return
	 */
	public static String getSubStringInbetween(String partStr, String delimiterA, String delimiterB) {
		partStr = StringUtil.getSubStringBeginningFromTheFirstDelimeter(partStr, delimiterA);
		return StringUtil.getSubStringEndingAtTheFirstDelimeter(partStr, delimiterB);

	}

	/**
	 * 属性配置中可能包含中文时使用该选项。
	 * 
	 * @param propertyName
	 * @param defaultValue
	 *            如果中文解析失败，就是用默认值
	 * @return
	 */
	public static String readChinese(String str, String defaultValue) {
		try {
			// return new String(str.getBytes("ISO8859_1"), "GBK");
			return new String(str.getBytes("UTF-8"), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return defaultValue;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public static String generateRamdomString(String prefix) {
		long uniqueNumber = System.currentTimeMillis() % 1000;
		if(!StringValidation.isStringReadable(prefix))
			prefix = "RAMDOM_STRING_";
		return prefix + uniqueNumber;
	}
}
