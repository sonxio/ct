package com.ibm.issac.toolkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

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
	private static FileWriter logWriter;
	private static String logFileName; // 日志文件名的基础部分，后面会增加日期，不要包含后缀
	private static Integer dayOfMonth = -1;// 用于日志隔日翻新
	private static OutputStream os1; // 如果不是NULL，则额外输出到这个OS中，用于在SERVLET中输出数据

	/**
	 * 选择用TEXT或者HTML输出日志
	 * 
	 * @param logModeStr
	 */
	public static void setOutputStream1(OutputStream os) {
		DevLog.os1 = os;
	}

	/**
	 * 如果调用该方法，则生成文件日志，否则不打印文件日志。
	 * 
	 * @param appName
	 */
	public static void writeFileLog(String appName) {
		DevLog.refreshFileLogWriter();
	}

	/**
	 * 如果调用该方法，则生成文件日志，否则不打印文件日志。
	 * 
	 * @deprecated 名称没有真正体现出含义，DevLog不需要初始化，这个命令只是用来生成文件日志的。
	 * @param appName
	 */
	public static void init(String appName) {
		DevLog.refreshFileLogWriter();
	}

	private static void refreshFileLogWriter() {
		// ---------------------------
		if (logWriter != null)
			try {
				logWriter.flush();
				logWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		// ------------------------
		if (StringUtil.isEmpty(logFileName))
			logFileName = StringUtil.generateRandomString();
		try {
			System.out.println("[DevLog] refreshing file log writer");
			// String fileNameWithPath = SysProp.b_str("java.io.tmpdir", ".") + SysProp.getFS() + logFileName + "_" + DateUtil.getNow("yyyyMMdd") + ".log";
			String fileNameWithPath = "." + SysProp.getFS() + "devlog_" + logFileName + "_" + DateUtil.getNow("yyyyMMdd") + ".log";
			File f = new File(fileNameWithPath);
			System.out.println("[DevLog] IRE devlog written to " + f.getAbsolutePath());
			logWriter = new FileWriter(f, true);
		} catch (IOException e) {
			System.out.println("[DevLog] Failed initiating DevLog. Logs will not be saved to disk.");
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
		// 增加线程id,不再用线程名称，因为有些线程名称非常长。而且was默认情况下就只打印线程id
		sb.append(Thread.currentThread().getId()).append(' ');
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

	/**
	 * super trace，用来TRACE日志量非常大的内容
	 * 
	 * @param logStr
	 */
	public static void super_trace(String logStr) {
		DevLog.writeLog("S", logStr);
	}

	/**
	 * 写日志。为了避免循环引用，这个方法和它引用的方法都不能调用DevLog
	 * 
	 * @param type
	 * @param logStr
	 */
	private static void writeLog(String type, String logStr) {
		final String prefixedLog = DevLog.buildPrefixedLog(type, logStr);
		// 在文件中记录日志，无论日志等级类型如何，都会在文件中记录，而且永远用TEXT方式记录
		if (logWriter != null) {
			DevLog.refreshFileLogOnceNeeded();
			try {
				logWriter.write(prefixedLog);
				logWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 根据日志设置类型决定是否在SYSTEM OUT打印日志内容
		String logLevel = SysProp.b_str("issac.logLevel", "-S-T-D-I-W-E-");
		if (logLevel.indexOf(type) < 0) { // 不打印logLevel变量中不包含级别的日志
			return;
		}
		// 为了避免干扰其他日志，这个参数提供了避免打印一切信息的选项，即仅写入devlog文件日志，不输出到java标准输出
		if (SysProp.b_bool("song.devlog.printNothingInSystemOut", false)) {
			return;
		}
		// 在SYSTEM OUT中显示日志，对于界面显示，不增加时间、线程，以免在WAS,TOMCAT, LIBERTY等应用中显示时显示出多余的时间。
		// 界面显示不显示PREFIXED LOG，只显示关键内容
		if (SysProp.b_bool("song.devlog.printDateTimeInSystemOut", true)) {
			DevLog.display(prefixedLog);
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(logStr).append('\n');
		DevLog.display(sb.toString());
	}

	private static void refreshFileLogOnceNeeded() {
		Calendar cal = Calendar.getInstance();
		int newDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth == -1) {// 初始值
			dayOfMonth = newDayOfMonth;
			return;
		}
		if (newDayOfMonth != dayOfMonth) {
			DevLog.refreshFileLogWriter();
			dayOfMonth = newDayOfMonth;
			System.out.println("[DevLog] refreshing file log " + dayOfMonth);
		}
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
		// 为HTML格式进行特别处理
		if (DevLog.os1 != null) {
			StringBuffer sb = new StringBuffer("");
			sb.append("<pre>").append(logStr).append("</pre>");
			try {
				os1.write(sb.toString().getBytes());
			} catch (IOException e) {
				System.out.println("[DevLog] Error displaying data. " + logStr);
				e.printStackTrace();
			}
			return;
		}
		// 非HTML，则直接打印日志
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

	public static void main(String[] args) {
		DevLog.writeFileLog("test");
		DevLog.super_trace("test super trace");
		DevLog.debug("test debug");
	}
}
