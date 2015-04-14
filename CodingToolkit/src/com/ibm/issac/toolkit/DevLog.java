package com.ibm.issac.toolkit;

import java.io.FileWriter;
import java.io.IOException;

import com.ibm.issac.toolkit.param.SysProp;
import com.ibm.issac.toolkit.util.DateUtil;
import com.ibm.issac.toolkit.util.StringUtil;

/**
 * Logger分为CONSOLE打印和FILE打印，总体上CONSOLE只用于显示所需信息，而FILE则显示所有信息
 * 
 * @author issac
 * 
 */
public final class DevLog {
	private static String appName;
	private static String logFileName;
	private static FileWriter logWriter;

	public static void setLogFileName(String logFileStr) {
		DevLog.logFileName = logFileStr;
	}

	public static String getLogFileName() {
		return logFileName;
	}

	public static void init(String appName) {
		//-----------------------------------
		//确定日志文件生成的位置
		if(!SysProp.b_bool("issac.DevLog.writeFile", true)){
			logWriter = null;//不允许写日志文件，从而降低性能消耗，对于J2EE等System.out已经写入的情况，应如此设置
			DevLog.debug("DevLog will not write to a file.");
			return;
		}
		DevLog.appName = appName;
		if (DevLog.appName == null) {
			DevLog.appName = "IreApp";
		}
		// 如果没有特别指定LOG FILE的文件位置，则根据环境变量生成日志文件
		if (!StringUtil.isReadable(DevLog.logFileName)) {
			final String logPath = SysProp.b_str("issac.logPath", SysProp.b_str("java.io.tmpdir", ""));
			logFileName = logPath+SysProp.getFS() + "devlog_" + appName + "_" + DateUtil.getNow("yyyyMMdd") + ".log";
		}
		try {
			DevLog.debug("log file written to " + logFileName);
			logWriter = new FileWriter(logFileName, true);
		} catch (IOException e) {
			DevLog.warn("Failed initiating DevLog. Logs will not be saved to disk.");
			e.printStackTrace();
		}
	}

	protected void finalize() throws Throwable {
		if (logWriter != null)
			logWriter.close();
		super.finalize();
	}

	public static String buildPrefixedLog(String type, String msg) {
		// 为LOG增加时间戳
		StringBuffer sb = new StringBuffer(DateUtil.getNow("yyyy-MM-dd HH:mm:ss.SSS "));
		// 增加线程名称
		sb.append(Thread.currentThread().getName()).append(' ');
		// 增加消息编号
		// if (appName != null) {
		// sb.append(appName.toUpperCase());
		// }
		sb.append(type).append(' ');
		// 增加日志内容
		sb.append(msg);
		sb.append('\n');
		return sb.toString();
	}

	public static void debug(String logStr) {
		DevLog.writeLog("D", logStr);
	}

	public static void trace(String logStr) {
		DevLog.writeLog("T", logStr);
	}

	private static void writeLog(String type, String logStr) {
		String prefixedLog = DevLog.buildPrefixedLog(type, logStr);
		// 在文件中记录日志，无论日志等级类型如何，都会在文件中记录
		if (logWriter != null) {
			try {
				logWriter.write(prefixedLog);
				logWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 根据日志设置类型决定是否在SYSTEM OUT打印日志内容
		String logLevel = SysProp.b_str("issac.logLevel", "-T-D-I-W-E-");
		if (logLevel.indexOf(type) < 0) { // 不打印logLevel变量中不包含级别的日志
			return;
		}
		// 在SYSTEM OUT中显示日志，对于界面显示，不增加时间、线程，以免在WAS,TOMCAT等应用中显示时显示出多余的时间。
		// StringBuffer sb = new StringBuffer(type);
		// sb.append(' ').append(logStr).append('\n');
		DevLog.display(prefixedLog);
	}

	/**
	 * 显示处理进度日志
	 * 
	 * @deprecated 改用PLOG
	 * @param logStr
	 */
	public static void progress(String logStr) {
		DevLog.writeLog("D", logStr);
	}

	public static void info(String logStr) {
		DevLog.writeLog("I", logStr);
	}

	public static void warn(String logStr) {
		DevLog.writeLog("W", logStr);
	}

	public static void error(String logStr) {
		DevLog.writeLog("E", logStr);
	}

	/**
	 * show string without any header
	 * 
	 * @param logStr
	 */
	public static void display(String logStr) {
		System.out.print(logStr);
	}

	/**
	 * show string without any header
	 * 
	 * @param logStr
	 */
	public static void displayln(String logStr) {
		System.out.println(logStr);
	}
}
