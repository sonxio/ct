package com.ibm.issac.toolkit.file.byLine;

/**
 * 配合line by line reader，逐行处理TEXT FILE
 * @author issac
 *
 */
public interface ByLineProcesser {

	/**
	 * 处理该行文本，每次只要接收一个处理单位的文本即可。
	 * @param text
	 */
	public void process(String text);
	
	
	/**
	 * 执行一些文本已经全部处理后要进行的操作
	 * @param msg
	 */
	public Object afterProcessing(Object msg);

}
