package com.ibm.issac.toolkit.file.lineByLineParser;

/**
 * 配合line by line reader，逐行处理TEXT FILE
 * @author issac
 *
 */
public interface LineByLineParser {

	/**
	 * 处理该行文本，每次只要接收一个处理单位的文本即可。
	 * @param text
	 * @throws LineByLineParserException
	 */
	public void parse(String text) throws LineByLineParserException;
	
	
	/**
	 * 执行一些文本已经全部处理后要进行的操作<p/>
	 * 有时一些缓存操作必须在所有操作执行完后再做一次，以便对最后一行记录执行。因此需要一个表示处理完成的方法
	 * @param msg
	 */
	public Object finishParsing(Object msg);

}
