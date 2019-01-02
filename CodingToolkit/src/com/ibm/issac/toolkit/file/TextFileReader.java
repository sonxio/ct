package com.ibm.issac.toolkit.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.util.StringUtil;

public class TextFileReader {

	/**
	 * 把文本文件中所有内容读入一个字符串
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readTextFile(String fileName) {
		File file = new File(fileName);
		StringBuffer contents = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;
			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				contents.append(text).append(System.getProperty("line.separator"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// show file contents here
		return contents.toString();
	}

	public static String readTextFileForException(String fileName) throws IOException {
		File file = new File(fileName);
		StringBuffer contents = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String text = null;
		// repeat until all lines is read
		while ((text = reader.readLine()) != null) {
			contents.append(text).append(System.getProperty("line.separator"));
		}
		if (reader != null) {
			reader.close();
		}

		// show file contents here
		return contents.toString();
	}

	public static List<String> readTextFileIntoStringList(String fileName) throws IOException {
		DevLog.super_trace("[TextFileReader] loading string array from text file: " + fileName);
		File file = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String text = null;
		List<String> sL = new ArrayList<String>();
		int lineNo = 0;
		// repeat until all lines is read
		while ((text = reader.readLine()) != null) {
			lineNo++;
			if (StringUtil.isReadable(text))
				sL.add(text);
			else {
				DevLog.super_trace("[TextFileReader] omitting invalid text file data: " + text + " at line " + lineNo);
			}
		}
		if (reader != null) {
			reader.close();
		}
		DevLog.super_trace("[TextFileReader] finished loading string array from text file: " + fileName);
		// show file contents here
		return sL;
	}

	public static List<Integer> readTextFileIntoIntegerList(String fileName) throws IOException {
		DevLog.super_trace("[TextFileReader] loading integer array from text file: " + fileName);
		File file = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String text = null;
		List<Integer> iL = new ArrayList<Integer>();
		int lineNo = 0;
		// repeat until all lines is read
		while ((text = reader.readLine()) != null) {
			lineNo++;
			if (StringUtil.isReadable(text)) {
				try {
					Integer i = Integer.valueOf(text);
					iL.add(i);
				} catch (NumberFormatException e) {
					DevLog.super_trace("[TextFileReader] omitting invalid text file data: " + text + " at line " + lineNo + ". It can't be correctly formatted as integer. " + e.getMessage());
					e.printStackTrace();
				}
			} else {
				DevLog.super_trace("[TextFileReader] omitting invalid text file data: " + text + " at line " + lineNo);
			}
		}
		if (reader != null) {
			reader.close();
		}
		DevLog.super_trace("[TextFileReader] finished loading string array from text file: " + fileName);
		// show file contents here
		return iL;
	}

	public static Map readProperties(String filePath) {
		Map m = new HashMap();
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String val = props.getProperty(key);
				m.put(key, val);
			}
			return m;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把文本文件中所有内容读入一个字符串 每一行读入到List的一行中。
	 * 
	 * @deprecated 改用IntoStringList
	 * @param fileName
	 * @return
	 */
	public static List readTextFileIntoList(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		List l = new ArrayList();
		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;
			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				l.add(text);
			}
			return l;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// show file contents here
		return l;
	}

	public static String readFileByByte(String fileName, String encodingStr) throws IOException {
		if (!StringUtil.isReadable(encodingStr)) {
			encodingStr = "UTF-8";
		}
		File file = new File(fileName);
		InputStream in = new FileInputStream(file);
		byte[] b = new byte[(int) file.length()];
		in.read(b);
		String requestMessage = new String(b, encodingStr);
		in.close();
		return requestMessage;
	}
}
