package com.ibm.issac.toolkit.format;

import java.text.DecimalFormat;

/**
 * 格式化数据类型，例如格式化小数点等。
 * VasF = Vas Format
 * @author issac
 * 
 */
public class NuF {

	/**
	 * 显示为现金格式
	 *  cu =currency
	 * @param d
	 * @return
	 */
	public static String cu(double d) {
		DecimalFormat df3 = new DecimalFormat("#.###");
		return df3.format(d);
	}
	
	/**
	 * 只保留一位小数点的数字 dot 1
	 * @param d
	 * @return
	 */
	public static String d1(double d) {
		DecimalFormat df3 = new DecimalFormat("#.#");
		return df3.format(d);
	}
	
	/**
	 * 只保留一位小数点的数字 dot 4
	 * @param d
	 * @return
	 */
	public static String d4(double d) {
		DecimalFormat df4 = new DecimalFormat("#.####");
		return df4.format(d);
	}
	public static String in(double d){
		DecimalFormat df0 = new DecimalFormat("#");
		return df0.format(d);
	}
	
	/**
	 * 显示百分比
	 * @param d
	 * @return
	 */
	public static String pe(double d){
		DecimalFormat df0 = new DecimalFormat("#.##%");
		return df0.format(d);
	}

	
}
