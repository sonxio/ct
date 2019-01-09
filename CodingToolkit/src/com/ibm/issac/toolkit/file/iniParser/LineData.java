package com.ibm.issac.toolkit.file.iniParser;

/**
 * 在逐行解析时，把逐行解析出的数据保存为该对象，该对象同时负责把一行文本封装起来。
 * @author issac
 *
 */
public abstract class LineData {

	/**
	 * 把一行文本内容提取到当前对象中
	 * @param lineStr
	 * @return 是否解析成功
	 */
	public abstract boolean parse(String lineStr);
}
