package com.ibm.issac.toolkit.file.lineByLineParser;

/**
 * 配合line by line reader，逐行处理TEXT FILE
 * @author issac
 *
 */
public interface ByLineProcesserWithExtraData extends LineByLineParser {

	/**
	 * 用于处理的附加属性
	 * @param obj
	 */
	public void setExtraData(Object obj);

}
