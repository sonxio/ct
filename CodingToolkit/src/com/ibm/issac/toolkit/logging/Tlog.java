package com.ibm.issac.toolkit.logging;

import com.ibm.issac.toolkit.DevLog;

/**
 * ��trace������ʾ������־
 * @deprecated һ�ɸ���PLOG
 * @author issac
 * 
 */
public class Tlog {

	public static void p0(String msg) {
		DevLog.trace("");
		DevLog.trace(".............>" + msg);
		DevLog.trace("");
		DevLog.trace("");
	}

	public static void p1(String msg) {
		DevLog.trace("");
		DevLog.trace("........>" + msg);
		DevLog.trace("");
	}

	public static void p2(String msg) {
		DevLog.trace("");
		DevLog.trace(".....>" + msg);
	}

	public static void enter2(String msg) {
		DevLog.trace("");
		DevLog.trace(".....> ENTER#" + msg);
	}

	public static void exit2(String msg) {
		DevLog.trace(".....>  EXIT#" + msg);
		DevLog.trace("");
	}

	public static void p3(String msg) {
		DevLog.trace("");
		DevLog.trace("...>" + msg);
	}
}
