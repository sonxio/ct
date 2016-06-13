package com.ibm.issac.toolkit.file.byLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;

import com.ibm.issac.toolkit.util.StringUtil;

/**
 * 逐行处理TEXT FILE
 * 
 * @author issac
 * 
 */
public class TextFileByLineReader {
	/**
	 * @deprecated 缺少EXCEPTION处理，在文件找不到等情况是不便处理的
	 * @param fileName
	 * @param p
	 * @return
	 */
	public String process(String fileName, ByLineProcesser p) {
		try {
			return this.processForException(fileName, p);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String processForException(String fileName, ByLineProcesser p) throws IOException {
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
			while ((text = reader.readLine()) != null) {
				p.process(text);
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
