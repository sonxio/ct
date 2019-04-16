package com.ibm.issac.toolkit.file.lineByLineParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;

import com.ibm.issac.toolkit.util.StringUtil;

/**
 * 逐行处理TEXT FILE<p/>
 * <pre>
 * 适用于针对文本每一行都有独立处理逻辑的业务。
 * 例子参见MOE项目com.ibm.issac.moe.anaSetting.parser
 * 使用方法为：实现BLP，业务逻辑写在BLP里，
 * BLP的PROCESS方法可以针对每一行区分出很多处理情况，分别单独处理
 * 然后用READER读取BLP。
 * </pre>
 * 
 * @author issac
 * 
 */
public class LineByLineReader {
	/**
	 * @deprecated 缺少EXCEPTION处理，在文件找不到等情况是不便处理的
	 * @param fileName
	 * @param p
	 * @return
	 */
	public String readSilently(String fileName, LineByLineParser p) {
		try {
			return this.read(fileName, p);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineByLineParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String read(String fileName, LineByLineParser p) throws IOException, LineByLineParserException {
		if(!StringUtil.isReadable(fileName)){
			throw new IOException("File name not readable: >"+fileName+"<");
		}
		fileName=URLDecoder.decode(fileName,"utf-8");//防止目录里有空格
		final File file = new File(fileName);
		StringBuffer contents = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;
			// repeat until all lines is read
			// 不要TRIM，因为读取一些配置的时候前后的空格是必须的。
			while ((text = reader.readLine()) != null) {
				p.parse(text);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		// show file contents here
		return contents.toString();
	}
}
