package com.ibm.issac.toolkit.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import com.ibm.issac.toolkit.logging.ColorLog;
import com.ibm.issac.toolkit.validation.StringValidation;

public class StringUtil {

	/**
	 * 生成一个随机字符串
	 * 
	 * @param numberOfWords
	 * @return
	 */
	public static String generateRandomString() {
		Random random = new Random();
		char[] word = new char[random.nextInt(8) + 3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
		for (int j = 0; j < word.length; j++) {
			word[j] = (char) ('a' + random.nextInt(26));
		}
		return new String(word);
	}

	/**
	 * 生成一个含有数字的随机字符串
	 * @deprecated 方法名称拼写错了，而且带有数字不美观，改用纯字母的版本
	 * @param prefix
	 * @return
	 */
	public static String generateRamdomString(String prefix) {
		long uniqueNumber = System.currentTimeMillis() % 1000;
		if (!StringValidation.isStringReadable(prefix))
			prefix = "RAMDOM_STRING_";
		return prefix + uniqueNumber;
	}

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
		/*
		 * if ("null".equals(string.trim().toLowerCase())) { ColorLog.warn("You have provided a string consisted of 4 characters: N-U-L-L. It's been treated as not-readable."); return false; }
		 */
		return true;
	}

	public static boolean isEmpty(String str) {
		return !StringUtil.isReadable(str);
	}

	/**
	 * 默认java用科学计数法打印小数，造成很难识别。
	 * 
	 * @param d
	 * @return
	 */
	public static String printNumber(double d) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
		return df.format(d);
	}

	/**
	 * 用String形式显示一个String数组
	 * 
	 * @deprecated 改用flattenArray
	 * @param stringArray
	 * @return
	 */
	public static String flattenStringArray(String[] stringArray) {
		final StringBuffer sb = new StringBuffer("[");
		for (int i = 0; stringArray != null && i < stringArray.length; i++) {
			if (i != 0)
				sb.append(',');
			sb.append(stringArray[i]);
		}
		sb.append(']');
		return sb.toString();
	}

	public static String flattenArray(Object[] objA) {
		final StringBuffer sb = new StringBuffer("[");
		for (int i = 0; objA != null && i < objA.length; i++) {
			if (i != 0)
				sb.append(',');
			sb.append(objA[i]);
		}
		sb.append(']');
		return sb.toString();
	}

	public static String flattenArray(int[] intA) {
		final StringBuffer sb = new StringBuffer("[");
		for (int i = 0; intA != null && i < intA.length; i++) {
			if (i != 0)
				sb.append(',');
			sb.append(intA[i]);
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

	/**
	 * 把字符串用空格切断为
	 * 
	 * @return
	 */
	public static String[] splitIntoList(String str, String seperator) {
		String splittedStr[] = str.split(seperator + "");
		return splittedStr;
	}

	/**
	 * 替换字符串中的回车、制表符等特殊符号。
	 * 
	 * @param inStr
	 *            需要替换的字符串
	 * @param replacement
	 *            需要替换为该字符，例如设置为空字符串
	 * @return
	 */
	public static String removeControlCharacters(String inStr, String replacement) {
		if (!StringUtil.isReadable(inStr)) {
			return inStr;
		}
		return inStr.replaceAll("[\\t\\n\\r]", replacement);
	}
}
