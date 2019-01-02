package com.ibm.issac.toolkit.logging;

import com.ibm.issac.toolkit.DevLog;

/**
 * @author issac
 * 
 */
public class Plog {

	public static void p0(String msg) {
		DevLog.info("");
		DevLog.info("");
		DevLog.info("");
		DevLog.info("");
		DevLog.info("");
		DevLog.info("P0.......................>" + msg);
		DevLog.info("");
	}

	public static void p1(String msg) {
		DevLog.info("");
		DevLog.info("");
		DevLog.info("");
		DevLog.info("");
		DevLog.info("P1..................>" + msg);
		DevLog.info("");
	}

	public static void p2(String msg) {
		DevLog.info("");
		DevLog.info("");
		DevLog.info("");
		DevLog.info("P2............>" + msg);
	}

	public static void enter2(String msg) {
		DevLog.info("");
		DevLog.info("");
		DevLog.info("");
		DevLog.info("P2............> ENTER#" + msg);
	}

	public static void exit2(String msg) {
		DevLog.info("");
		DevLog.info("");
		DevLog.info("");
		DevLog.info("P2............>  EXIT#" + msg);
		DevLog.info("");
	}

	public static void p3(String msg) {
		DevLog.info("");
		DevLog.info("");
		DevLog.info("P3......>" + msg);
		DevLog.info("");
	}
	
	public static void p4(String msg) {
		DevLog.info("");
		DevLog.info("P4..>" + msg);
		DevLog.info("");
	}
}
