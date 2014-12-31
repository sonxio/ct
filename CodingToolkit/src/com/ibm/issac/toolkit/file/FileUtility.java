package com.ibm.issac.toolkit.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.ibm.issac.toolkit.Cube;
import com.ibm.issac.toolkit.DatetimeUtility;
import com.ibm.issac.toolkit.logging.ColorLog;

/**
 * 辅助文件操作
 * 
 * @author issac
 * 
 */
public final class FileUtility {
	/**
	 * <p>
	 * Creates an InputStream from a file, and fills it with the complete file. Thus, available() on the returned InputStream will return the full number of bytes the file contains
	 * </p>
	 * 
	 * @param fname
	 *            The filename
	 * @return The filled InputStream
	 * @exception IOException
	 *                , if the Streams couldn't be created.
	 **/
	public static InputStream fullStream(String fname) throws IOException {
		FileInputStream fis = new FileInputStream(fname);
		DataInputStream dis = new DataInputStream(fis);
		byte[] bytes = new byte[dis.available()];
		dis.readFully(bytes);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		return bais;
	}

	/**
	 * Based on an old file name, prompt up with a new one.
	 * 
	 * @param oldName
	 */
	public static String promptNewName(String oldName) {
		return oldName + Cube.produceRandomLong();
	}

	/**
	 * normalize separtors and formats for a path presentation. This method works for both file and directory.
	 * 
	 * @param pathStr
	 * @return
	 */
	public static String normalizePathString(String pathStr) {
		String normalizedStr = pathStr;
		normalizedStr = normalizedStr.replaceAll("\\\\", "/");
		// normalizedStr = normalizedStr.replaceAll("\\", "/");
		return normalizedStr;
	}

	/**
	 * 在指定目录创建一个临时文件，需要提供包括文件名在内的完整绝对路径名
	 * 
	 * @param absolutePath
	 * @return
	 */
	public static File createTempFile(String absolutePath) {
		final File file = new File(absolutePath);
		file.deleteOnExit();// 因为STREAM没有关闭等原因，该方法有可能无效
		return file;
	}


	/**
	 * 转移文件目录
	 * 
	 * @param filename
	 *            文件名
	 * @param oldpath
	 *            旧目录
	 * @param newpath
	 *            新目录
	 * @param cover
	 *            若新目录下存在和转移文件具有相同文件名的文件时，是否覆盖新目录下文件，cover=true将会覆盖原文件，否则不操作
	 */

	public static void move(String filename, String oldpath, String newpath, boolean cover) {
		if (!oldpath.equals(newpath)) {
			File oldfile = new File(oldpath + "/" + filename);
			File newfile = new File(newpath + "/" + filename + "." + DatetimeUtility.formatDate("yyyyMMddHHmmss", new Date()));
			if (newfile.exists()) {// 若在待转移目录下，已经存在待转移文件
				if (cover)// 覆盖
					oldfile.renameTo(newfile);
				else
					ColorLog.warn("在新目录下已经存在：" + filename);
			} else {
				oldfile.renameTo(newfile);
			}
		}
	}

	/**
	 * 查询一个文本文件有多少行
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static long countRow(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			long count = 0;
			int readChars = 0;
			while ((readChars = is.read(c)) != -1) {
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n')
						++count;
				}
			}
			return count;
		} finally {
			is.close();
		}
	}

}
