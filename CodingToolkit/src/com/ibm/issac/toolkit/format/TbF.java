package com.ibm.issac.toolkit.format;

public class TbF {

	/**
	 * 用于显示表格的时候帮助表格对齐，该方法最后会添上制表符。
	 * 
	 * @param s
	 *            原始字符串
	 * @param l
	 *            如果s长度不到l，计算长度时汉字按照两个计算。则补充空格到l为止
	 * @return
	 */
	public static String td(String s, int l) {
		int currentL = s.getBytes().length;
		int gap = l - currentL;
		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i <= gap; i++) {
			sb.append(' ');
		}
		sb.append('\t');
		return sb.toString();
	}
}
