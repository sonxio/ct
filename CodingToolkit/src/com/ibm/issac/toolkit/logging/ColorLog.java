package com.ibm.issac.toolkit.logging;

import com.ibm.issac.toolkit.DatetimeUtility;

/**
 * 写日志，设置为FINAL避免被恶意篡改部分方法
 * 
 * @author issac
 * 
 */
public final class ColorLog {
	/*
	 * public static final String ANSI_CLS = "\u001b[2J"; public static final String ANSI_HOME = "\u001b[H"; public static final String ANSI_BOLD = "\u001b[1m"; public static final String ANSI_AT55 = "\u001b[10;10H"; public static final String ANSI_REVERSEON = "\u001b[7m"; public static final String ANSI_NORMAL = "\u001b[0m"; public static final String ANSI_WHITEONBLUE = "\u001b[37;44m";
	 */

	public static void init(String fileName) {
		/*
		 * AnsiConsole.systemInstall(); AnsiConsole.out.println(ANSI_CLS); AnsiConsole.out.println (ANSI_AT55 + ANSI_REVERSEON + "Hello world" + ANSI_NORMAL); AnsiConsole.out.println (ANSI_HOME + ANSI_WHITEONBLUE + "Hello world" + ANSI_NORMAL); AnsiConsole.out.print (ANSI_BOLD + "Press a key..." + ANSI_NORMAL);
		 */
	}

	public static void debug(String logStr) {
		ColorLog.println(logStr);
	}

	public static void info(String logStr) {
		ColorLog.println(logStr);
	}

	public static void warn(String logStr) {
		ColorLog.println(logStr);
	}

	/**
	 * show string without any header
	 * 
	 * @param logStr
	 */
	public static void print(String logStr) {
		System.out.print(logStr);
	}

	/**
	 * show string without any header
	 * 
	 * @param logStr
	 */
	public static void println(String logStr) {
		System.out.println(DatetimeUtility.getDatetimeNow() + "\t" + logStr);
	}
}
