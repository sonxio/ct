package com.ibm.issac.toolkit.file;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.ibm.issac.toolkit.DevLog;

/**
 * 写文本文件的有关操作
 * 
 * @author issac
 * 
 */
public class TextFileWriter {
	
	/**
	 * 写新文件，会覆盖掉原有文件。
	 * @param fileName
	 * @param content
	 * @throws IOException
	 */
	public static void writeTextFile(String fileName, String content) throws IOException {
		DevLog.debug("Writing file: "+fileName);
		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName)), true);
		pw.println(content);
		pw.close();
	}
	
	/**
	 * 写到文件末尾，如果文件不存在则创建新文件
	 * @param fileName
	 * @param content
	 * @throws IOException
	 */
	public static void appendToFile(String fileName, String content) throws IOException{
		DevLog.debug("Appending file: "+fileName);
		FileWriter writer = new FileWriter(fileName, true);
		writer.write(content);
		writer.close();
	}
}
