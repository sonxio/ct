package com.ibm.issac.toolkit.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.ibm.issac.toolkit.DevLog;

/**
 * д�ı��ļ����йز���
 * 
 * @author issac
 * 
 */
public class TextFileWriter {
	/**
	 * д���ļ����Ḳ�ǵ�ԭ���ļ���<br/>
	 * �½����ļ��ᰴ�յ�ǰUMASK/OWNER����OWNER/MODE��<br/>
	 * ���ϣ��OWNER MODE��ĳ���ļ�����һ�£�����������ļ�ʹ��NATIVE����cp -p����һ�ݣ�Ȼ����������������ǣ�����Ա���Դ�ļ���OWNER/MODE
	 * 
	 * @param fileName
	 * @param content
	 * @throws IOException
	 */
	public static void writeTextFile(String fileName, String content) throws IOException {
		DevLog.super_trace("[TextFileWriter] Writing file: " + fileName);
		final File f = new File(fileName);
		DevLog.super_trace("[TextFileWriter] Destination file path: " + f.getAbsolutePath());
		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)), true);
		pw.println(content);
		pw.close();
	}

	/**
	 * д���ļ�ĩβ������ļ��������򴴽����ļ�
	 * 
	 * @param fileName
	 * @param content
	 * @throws IOException
	 */
	public static void appendToFile(String fileName, String content) throws IOException {
		DevLog.debug("[TextFileWriter] Appending to file: " + fileName);
		FileWriter writer = new FileWriter(fileName, true);
		writer.write(content);
		writer.close();
	}
}
