package com.ibm.issac.toolkit5.app.jconv;

import java.io.*;
import java.nio.charset.*;
import java.util.ArrayList;

/**
 * <pre>
 * This class converts a folder from any given encoding to another.
 * For iconv fans:
 * 	1) iconv doesn't support to replace the source file. To be specific, iconv -f gbk -t utf-8 Test.java > Test.java will yield a blank file.
 * 	2) iconv doesn't support to convert a folder whole.
 *  3) iconv doesn't support to backup original files.
 *  4) it's not multi-platform.
 *  
 * This class is to overcome such obstacles. Feel free to upload to github for further changes.
 * </pre>
 * @author song shsongj@cn.ibm.com
 *
 */
public class Jconv {

	// private static final char BYTE_ORDER_MARK = '\uFEFF';
	private static final String ANSI_CODE = "windows-1252";
	private static final String UTF_CODE = "UTF8";
	private static final Charset ANSI_CHARSET = Charset.forName(ANSI_CODE);
	private static final String PATH_SEP = "\\";
	// private static final boolean WRITE_BOM = false;

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Please name a source and a target directory");
			return;
		}

		File inputFolder = new File(args[0]);
		if (!inputFolder.isDirectory()) {
			System.out.println("Input folder " + inputFolder + " does not exist");
			return;
		}
		File outputFolder = new File(args[1]);

		if (outputFolder.exists()) {
			System.out.println("Folder " + outputFolder + " exists - aborting");
			return;
		}
		if (outputFolder.mkdir()) {
			System.out.println("Placing converted files in " + outputFolder);
		} else {
			System.out.println("Output folder " + outputFolder + " exists - aborting");
			return;
		}

		ArrayList<File> fileList = new ArrayList<File>();
		for (final File fileEntry : inputFolder.listFiles()) {
			fileList.add(fileEntry);
		}

		InputStream in;
		Reader reader = null;
		Writer writer = null;
		int converted = 0;

		try {
			for (File file : fileList) {
				try {
					in = new FileInputStream(file.getAbsoluteFile());
					reader = new InputStreamReader(in, ANSI_CHARSET);

					OutputStream out = new FileOutputStream(outputFolder.getAbsoluteFile() + PATH_SEP + file.getName());
					writer = new OutputStreamWriter(out, UTF_CODE);

					// if (WRITE_BOM)
					// writer.write(BYTE_ORDER_MARK);
					char[] buffer = new char[1024];
					int read;
					while ((read = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, read);
					}
					++converted;
				} finally {
					reader.close();
					writer.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(converted + " files converted");
	}

}