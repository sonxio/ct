package com.ibm.issac.toolkit.util;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import com.ibm.issac.toolkit.DevLog;

public class StringUtil {

	/**
	 * 去掉字符串中最后一个点到末尾的内容
	 * 
	 * @param str
	 * @return
	 */
	public static String removeSurfix(String str) {
		String result = str.replaceAll("\\..*$", "");
		DevLog.super_trace("[StringUtil] removing surfix for >" + str + "< gives >" + result + "<");
		return result;
	}

	/**
	 * 得到两个字符串之间的字符串
	 * 
	 * @param str1Ø
	 * @param str2
	 * @return
	 */
	public static String ignoreCaseSubstringInbetween(String content, String str1, String str2) {
		try {
			int startIndex = StringUtil.ignoreCaseIndexOf(content, str1) + str1.length();
			int endIndex = StringUtil.ignoreCaseIndexOf(content, str2);
			String str = content.substring(startIndex, endIndex);
			// 如果开头有冒号，去掉
			if (str.startsWith(":")) {
				str = str.substring(1, str.length());
			}
			str = str.trim();
			return str;
		} catch (StringIndexOutOfBoundsException ex) {
			DevLog.debug("[StringUtil] " + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * 生成一个随机字符串
	 * 
	 * @param numberOfWords
	 * @return
	 */
	public static String generateRandomString() {
		Random random = new Random();
		char[] word = new char[random.nextInt(8) + 3]; // words of length 3 through 10. (1 and 2 letter words are
														// boring.)
		for (int j = 0; j < word.length; j++) {
			word[j] = (char) ('a' + random.nextInt(26));
		}
		return new String(word);
	}

	/**
	 * 生成一个含有数字的随机字符串
	 * 
	 * @deprecated 方法名称拼写错了，而且带有数字不美观，改用纯字母的版本
	 * @param prefix
	 * @return
	 */
	public static String generateRamdomString(String prefix) {
		long uniqueNumber = System.currentTimeMillis() % 1000;
		if (!StringUtil.isReadable(prefix))
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
		 * if ("null".equals(string.trim().toLowerCase())) { ColorLog.
		 * warn("You have provided a string consisted of 4 characters: N-U-L-L. It's been treated as not-readable."
		 * ); return false; }
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

	/**
	 * 把map转化为string，方便打印日志
	 * 
	 * @param m
	 */
	public static String flattenMap(Map m) {
		StringBuffer sb = new StringBuffer("Map{");
		Set kS = m.keySet();
		Iterator it = kS.iterator();
		while (it.hasNext()) {
			Object key = it.next();
			sb.append("{key: ").append(key).append(", value: ").append(m.get(key)).append("}");
		}
		sb.append("}");
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
	 * Returns the supplied string, up to (and excluding) the first newline
	 * character. If the supplied string is null, the output string is also null.
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
	public static String getSubStringEndingAtTheFirstDelimeter(String partStr, String delimeterStr)
			throws StringIndexOutOfBoundsException {
		return partStr.substring(0, partStr.indexOf(delimeterStr));
	}

	/**
	 * 返回从第一个空格开始到结尾的部分字符串
	 * 
	 * @param partStr
	 * @param delimeterStr
	 * @return
	 */
	public static String getSubStringBeginningFromTheFirstDelimeter(String partStr, String delimeterStr)
			throws StringIndexOutOfBoundsException {
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

	/**
	 * 将字符串中的连续的多个换行缩减成一个换行
	 * 
	 * @param str
	 *            要处理的内容
	 * @return 返回的结果
	 */
	public static String replaceLineBlanks(String str) {
		String result = "";
		if (str != null) {
			Pattern p = Pattern.compile("(\r?\n(\\s*\r?\n)+)");
			java.util.regex.Matcher m = p.matcher(str);
			result = m.replaceAll("\r\n");
		}
		return result;
	}

	/**
	 * 删除每一行开头结尾的空格，删掉连续换行符
	 * 
	 * @param wholeText
	 * @return
	 */
	public static String trimContent(String wholeText) {
		String str = StringUtil.replaceLineBlanks(wholeText);
		str = str.replaceAll(" +", " ");// 把多个空格变为1个
		return str;
	}

	/**
	 * 返回指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始，不区分大小。
	 * 
	 * @param subject
	 *            被查找字符串。
	 * @param search
	 *            要查找的子字符串。
	 * @return 指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始。
	 */
	public static int ignoreCaseIndexOf(String subject, String search) {
		return ignoreCaseIndexOf(subject, search, -1);
	}

	/**
	 * 返回指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始，不区分大小。
	 * 
	 * @param subject
	 *            被查找字符串。
	 * @param search
	 *            要查找的子字符串。
	 * @param fromIndex
	 *            开始查找的索引位置。其值没有限制，如果它为负，则与它为 0 的效果同样：将查找整个字符串。
	 *            如果它大于此字符串的长度，则与它等于此字符串长度的效果相同：返回 -1。
	 * @return 指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始。
	 */
	public static int ignoreCaseIndexOf(String subject, String search, int fromIndex) {

		// 当被查找字符串或查找子字符串为空时，抛出空指针异常。
		if (subject == null || search == null) {
			throw new NullPointerException("输入的参数为空");
		}

		fromIndex = fromIndex < 0 ? 0 : fromIndex;

		if (search.equals("")) {
			return fromIndex >= subject.length() ? subject.length() : fromIndex;
		}

		int index1 = fromIndex;
		int index2 = 0;

		char c1;
		char c2;

		loop1: while (true) {

			if (index1 < subject.length()) {
				c1 = subject.charAt(index1);
				c2 = search.charAt(index2);

			} else {
				break loop1;
			}

			while (true) {
				if (isEqual(c1, c2)) {

					if (index1 < subject.length() - 1 && index2 < search.length() - 1) {

						c1 = subject.charAt(++index1);
						c2 = search.charAt(++index2);
					} else if (index2 == search.length() - 1) {

						return fromIndex;
					} else {

						break loop1;
					}

				} else {

					index2 = 0;
					break;
				}
			}
			// 重新查找子字符串的位置
			index1 = ++fromIndex;
		}

		return -1;
	}

	/**
	 * 返回指定子字符串在此字符串中最右边出现处的索引。
	 * 
	 * @param subject
	 *            被查找字符串。
	 * @param search
	 *            要查找的子字符。
	 * @return 在此对象表示的字符序列中最后一次出现该字符的索引；如果在该点之前未出现该字符，则返回 -1
	 */
	public static int ignoreCaseLastIndexOf(String subject, String search) {
		if (subject == null) {
			throw new NullPointerException("输入的参数为空");
		} else {
			return ignoreCaseLastIndexOf(subject, search, subject.length());
		}
	}

	/**
	 * 返回指定字符在此字符串中最后一次出现处的索引，从指定的索引处开始进行反向查找。
	 * 
	 * @param subject
	 *            被查找字符串 。
	 * @param search
	 *            要查找的子字符串。
	 * @param fromIndex
	 *            开始查找的索引。fromIndex 的值没有限制。如果它大于等于此字符串的长度，则与它小于此字符串长度减 1
	 *            的效果相同：将查找整个字符串。 如果它为负，则与它为 -1 的效果相同：返回 -1。
	 * @return 在此对象表示的字符序列（小于等于 fromIndex）中最后一次出现该字符的索引； 如果在该点之前未出现该字符，则返回 -1
	 */
	public static int ignoreCaseLastIndexOf(String subject, String search, int fromIndex) {

		// 当被查找字符串或查找子字符串为空时，抛出空指针异常。
		if (subject == null || search == null) {
			throw new NullPointerException("输入的参数为空");
		}

		if (search.equals("")) {
			return fromIndex >= subject.length() ? subject.length() : fromIndex;
		}

		fromIndex = fromIndex >= subject.length() ? subject.length() - 1 : fromIndex;

		int index1 = fromIndex;
		int index2 = 0;

		char c1;
		char c2;

		loop1: while (true) {

			if (index1 >= 0) {
				c1 = subject.charAt(index1);
				c2 = search.charAt(index2);
			} else {
				break loop1;
			}

			while (true) {
				// 判断两个字符是否相等
				if (isEqual(c1, c2)) {
					if (index1 < subject.length() - 1 && index2 < search.length() - 1) {

						c1 = subject.charAt(++index1);
						c2 = search.charAt(++index2);
					} else if (index2 == search.length() - 1) {

						return fromIndex;
					} else {

						break loop1;
					}
				} else {
					// 在比较时，发现查找子字符串中某个字符不匹配，则重新开始查找子字符串
					index2 = 0;
					break;
				}
			}
			// 重新查找子字符串的位置
			index1 = --fromIndex;
		}

		return -1;
	}

	/**
	 * 判断两个字符是否相等。
	 * 
	 * @param c1
	 *            字符1
	 * @param c2
	 *            字符2
	 * @return 若是英文字母，不区分大小写，相等true，不等返回false； 若不是则区分，相等返回true，不等返回false。
	 */
	private static boolean isEqual(char c1, char c2) {
		// 字母小写 字母大写
		if (((97 <= c1 && c1 <= 122) || (65 <= c1 && c1 <= 90)) && ((97 <= c2 && c2 <= 122) || (65 <= c2 && c2 <= 90))
				&& ((c1 - c2 == 32) || (c2 - c1 == 32))) {

			return true;
		} else if (c1 == c2) {
			return true;
		}

		return false;
	}

}
