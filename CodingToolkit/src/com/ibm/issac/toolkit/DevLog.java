package com.ibm.issac.toolkit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

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
	 * @param appName
	 */
	public static void writeFileLog(String appName){
		DevLog.init(appName);
	}
	
	/**
	 * 如果调用该方法，则生成文件日志，否则不打印文件日志。
	 * @deprecated 名称没有真正体现出含义，DevLog不需要初始化，这个命令只是用来生成文件日志的。
	 * @param appName
	 */
	public static void init(String appName) {
		// -----------------------------------
		// 确定日志文件生成的位置
		if (!SysProp.b_bool("issac.DevLog.writeFile", true)) {
			logWriter = null;// 不允许写日志文件，从而降低性能消耗，对于J2EE等System.out已经写入的情况，应如此设置
			DevLog.trace("[DevLog] DevLog will not write to a file.");
			return;
		}
		// 如果没有特别指定LOG FILE的文件位置，则根据环境变量生成日志文件
		String logFileName = SysProp.pstr("song.logFileNameWithPath");
		if (StringUtil.isEmpty(logFileName)) {
			//DELAYED_TODO 在windows上有BUG，这个文件会是空的，可能和java.io.tmpdir输出最后包含\有关，可以通过设置song.logFileNameWithPath变量规避。
			logFileName = SysProp.b_str("java.io.tmpdir", "./") + SysProp.getFS() + "devlog_" + appName + "_" + DateUtil.getNow("yyyyMMdd") + ".log";
		}
		try {
			DevLog.trace("[DevLog] IRE devlog written to " + logFileName);
			logWriter = new FileWriter(logFileName, true);
		} catch (IOException e) {
			DevLog.warn("[DevLog] Failed initiating DevLog. Logs will not be saved to disk.");
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

	/**
	 * super trace，用来TRACE日志量非常大的内容
	 * 
	 * @param logStr
	 */
	public static void super_trace(String logStr) {
		DevLog.writeLog("S", logStr);
	}

	private static void writeLog(String type, String logStr) {
		final String prefixedLog = DevLog.buildPrefixedLog(type, logStr);
		// 在文件中记录日志，无论日志等级类型如何，都会在文件中记录，而且永远用TEXT方式记录
		if (logWriter != null) {
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
		if(SysProp.b_bool("song.devlog.printNothingInSystemOut", false)){
			return;
		}
		// 在SYSTEM OUT中显示日志，对于界面显示，不增加时间、线程，以免在WAS,TOMCAT等应用中显示时显示出多余的时间。
		// 界面显示不显示PREFIXED LOG，只显示关键内容
		if(SysProp.b_bool("song.devlog.printDateTimeInSystemOut", true)){
			DevLog.display(prefixedLog);
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(logStr).append('\n');
		DevLog.display(sb.toString());
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
}
