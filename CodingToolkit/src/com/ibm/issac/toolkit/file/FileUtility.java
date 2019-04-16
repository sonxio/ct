package com.ibm.issac.toolkit.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Date;

import com.ibm.issac.toolkit.Cube;
import com.ibm.issac.toolkit.DatetimeUtility;
import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.logging.ColorLog;
import com.ibm.issac.toolkit.nativeRuntime.ErrorRunningNativeCommandException;
import com.ibm.issac.toolkit.nativeRuntime.NativeCmdUnit;
import com.ibm.issac.toolkit.nativeRuntime.RunNativeCmd;
import com.ibm.issac.toolkit.nativeRuntime.RunUnixNativeCommand;
import com.ibm.issac.toolkit.nativeRuntime.RunWindowsNativeCommand;
import com.ibm.issac.toolkit.param.SysProp;
import com.ibm.issac.toolkit.util.StringUtil;

/**
 * 辅助文件操作
 * 
 * @deprecated 命令混乱，以后统一用fileutil
 * @author issac
 * 
 */
public class FileUtility {
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
	 * MOVE方法在UNIX上不会改变OWNER/MODE
	 * @param srcF
	 * @param dstF
	 * @param overwriteWhenExisted
	 */
	public static void move(File srcF, File dstF, boolean overwriteWhenExisted) {
		DevLog.trace("[FILE MOVE] Trying to move file from >" + srcF.getAbsolutePath() + "< to >" + dstF.getAbsolutePath() + "<");
		if (srcF.getAbsolutePath().equals(dstF.getAbsolutePath())) {
			DevLog.debug("[FILE MOVE] Source file\'s absoluate path is the same as the destination file. Operation aborted.");
			return;
		}
		if (!srcF.isFile()) {
			DevLog.debug("[FILE MOVE] Source is not a file. Operation aborted.");
			return;
		}
		// ---------------------------------
		if (dstF.exists()) {// 若在待转移目录下，已经存在目标文件
			DevLog.debug("[FILE MOVE] Destination file existed. Overwrite? " + overwriteWhenExisted);
			if (!overwriteWhenExisted) {// 不允许覆盖，则放弃操作
				DevLog.trace("[FILE MOVE] Destination file existed. Operation aborted.");
				return;
			}
			// 覆盖现有文件
			srcF.renameTo(dstF);
			return;
		}
		// 不存在目标文件
		srcF.renameTo(dstF);
	}

	/**
	 * 把文件s拷贝为文件t
	 * 该方法会修改OWNER,MODE
	 * @param srcF
	 * @param dstF
	 * @param overwriteWhenExisted
	 *            如果目标文件已经存在，是否覆盖
	 */
	public static void copy(File srcF, File dstF, boolean overwriteWhenExisted) {
		DevLog.trace("[FILE COPY] Trying to copy file from >" + srcF.getAbsolutePath() + "< to >" + dstF.getAbsolutePath() + "<");
		if (!srcF.isFile()) {
			DevLog.debug("[FILE COPY] Source is not a file. Operation aborted.");
			return;
		}
		if (dstF.exists()) {
			DevLog.debug("[FILE COPY] The destination file existed when trying to perform file copy. Overwrite? " + overwriteWhenExisted);
			if (!overwriteWhenExisted) {
				DevLog.debug("[FILE COPY] Destination file existed. Operation aborted.");
				return;
			}
		}
		// 开始拷贝文件，遇到现有文件则覆盖。
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(srcF);
			fo = new FileOutputStream(dstF);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 使用cp -p在UNIX上不会修改OWNER,MODE<br/>
	 * 使用NATIVE命令拷贝文件。这样的优势是可以规避一部分OWNER, MODE的问题<br/>
	 * 该方法不支持覆盖，如果目标文件存在，则拒绝操作
	 * 
	 * @param srcPath
	 * @param destPath
	 * @throws ErrorRunningNativeCommandException
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void copyFileNatively(String srcPath, String dstPath) throws ErrorRunningNativeCommandException, IOException, InterruptedException {
		// 为了避免误操作，确认所要拷贝的对象的确是文件
		if (!StringUtil.isReadable(srcPath)) {
			DevLog.debug("[FILE COPY NATIVELY] source path not readable. >" + srcPath + "<. Operation aborted.");
			return;
		}
		if (!StringUtil.isReadable(dstPath)) {
			DevLog.debug("[FILE COPY NATIVELY] destination path not readable. >" + dstPath + "<. Operation aborted.");
			return;
		}
		final File srcF = new File(srcPath);
		final File dstF = new File(dstPath);
		DevLog.super_trace("[FILE COPY NATIVELY] Trying to copy file from >" + srcF.getAbsolutePath() + "< to >" + dstF.getAbsolutePath() + "<");
		if (!srcF.isFile()) {
			DevLog.debug("[FILE COPY NATIVELY] Source is not a file. Operation aborted.");
			return;
		}
		if (dstF.exists()) {
			DevLog.debug("[FILE COPY NATIVELY] The destination file existed when trying to perform file copy. Operation aborted.");
			return;
		}
		// 调用对应OS命令
		final NativeCmdUnit ncu = new NativeCmdUnit();
		if (!SysProp.getOSName().startsWith("Windows")) {
			final String cmd = "cp -p " + srcPath + " " + dstPath;// -p preserve,保持OWNER,MODE，创建时间
			ncu.setCmd_ALL(cmd);
			ncu.setCmd_Windows(null);
		} else {
			ncu.setCmd_ALL(null);
			ncu.setCmd_Windows("copy " + srcPath + " " + dstPath);
		}
		final RunNativeCmd r = new RunNativeCmd();
		int retVal;
		retVal = r.runNativeCmd(ncu);
		DevLog.super_trace("[FILE COPY NATIVELY] REPORT: " + r.getOutputM().get("last report"));
		if (retVal != 0) {
			throw new ErrorRunningNativeCommandException("[FILE COPY NATIVELY] error output: " + r.getOutputM().get("error output"));
		}
	}

	/**
	 * 查询一个文本文件有多少行
	 * 
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
