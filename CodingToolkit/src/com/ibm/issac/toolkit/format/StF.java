package com.ibm.issac.toolkit.format;

/**
 * string format
 * @author issac
 *
 */
public class StF {

	/**
	 * 统一字符串长度，把字符加在最后补足
	 * @see 功能还有问题
	 * @param sb
	 * @param c 长度不足时，用于补足的字符
	 * @param len
	 */
	public static StringBuffer unifyLengthBackward(StringBuffer sb, char c, int len1) {
		//检查长度，添加空格
		int len = sb.length();
		for(int i=len; i<=len1; i++){
			sb.append(c);
		}
		return sb;
	}
	
	/**
	 * 统一字符串长度，把字符加在最前补足
	 * @param sb
	 * @param c 长度不足时，用于补足的字符
	 */
	public static StringBuffer unifyLengthForward(StringBuffer sb, char c, int len1) {
		//检查长度，添加空格		
		int len = sb.length();
		StringBuffer sb1 = new StringBuffer(c);
		for(int i=len; i<=len1; i++){
			sb1.append(sb);
		}
		return sb1;		
	}
	
	/**
	 * 为STRING加上左右括号，用于日志输出避免不能识别左右的空格
	 * @param str
	 * @return
	 */
	public static String quoted(String str){
		StringBuffer sb = new StringBuffer(">");
		sb.append(str).append("<");
		return sb.toString();
	}
}
