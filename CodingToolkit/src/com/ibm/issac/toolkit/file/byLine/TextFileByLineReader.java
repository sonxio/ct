package com.ibm.issac.toolkit.file.byLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * ÷––¥¶¿ÌTEXT FILE
 * 
 * @author issac
 * 
 */
public class TextFileByLineReader {
	public String process(String fileName, ByLineProcesser p) {
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
}
