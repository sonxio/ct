package com.ibm.issac.toolkit.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import com.ibm.issac.toolkit.logging.ColorLog;

/**
 * 文件权限控制操作工具类
 * 
 * @author issac
 * 
 */
public final class FilePermissionUtility {
	/**
	 * 为文件加SHARED锁，避免其他进程读取、操作。该方法和OS有关，在有些OS上无效。
	 * 
	 * @param file
	 *            文件名，本方法对目录无效
	 * @return
	 * @throws IOException
	 */
	public static FileLock createFileLock(File file) throws IOException, OverlappingFileLockException {
		FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
		FileLock lock = fc.tryLock();
		if (lock == null)
			ColorLog.warn("Unable to acquire lock for file " + file.getAbsolutePath());
		FilePermissionUtility.tryWritingToFileChannel(fc);
		return lock;
	}

	/**
	 * 显示文件当前权限
	 * 
	 * @param file
	 */
	public static void displayFilePermission(File file) {
		ColorLog.debug("Can write? " + file.canWrite());
		ColorLog.debug("Can read? " + file.canRead());
	}

	/**
	 * 仅用于测试的方法，测试给一个文件中写入一些数据。
	 * 
	 * @param fc
	 */
	public static void tryWritingToFileChannel(FileChannel fc) {
		// First, we need a buffer to hold the timestamp
		ByteBuffer bytes = ByteBuffer.allocate(8); // a long is 8 bytes
		// Put the time in the buffer and flip to prepare for writing
		// Note that many Buffer methods can be "chained" like this.
		bytes.putLong(System.currentTimeMillis() + 10000).flip();
		try {
			fc.write(bytes);// Write the buffer contents to the channel
			fc.force(false); // Force them out to the disk
			ColorLog.debug("File channel wrote to the file successfully.");
		} catch (IOException e) {
			ColorLog.warn("File Channel failed to write to the file.");
			e.printStackTrace();
		}

	}
}
