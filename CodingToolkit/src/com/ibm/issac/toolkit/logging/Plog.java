package com.ibm.issac.toolkit.logging;

import com.ibm.issac.toolkit.DevLog;

/**
 * @author issac
 * 
 */
public class Plog {

	public static void p0(String msg) {
		DevLog.info("");
		DevLog.info(".............>" + msg);
		DevLog.info("");
		DevLog.info("");
	}

	public static void p1(String msg) {
		DevLog.info("");
		DevLog.info("........>" + msg);
		DevLog.info("");
	}

	public static void p2(String msg) {
		DevLog.info("");
		DevLog.info(".....>" + msg);
	}

	public static void enter2(String msg) {
		DevLog.info("");
		DevLog.info(".....> ENTER#" + msg);
	}

	public static void exit2(String msg) {
		DevLog.info(".....>  EXIT#" + msg);
		DevLog.info("");
	}

	public static void p3(String msg) {
		DevLog.info("");
		DevLog.info("...>" + msg);
	}
}
