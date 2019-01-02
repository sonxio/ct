package com.ibm.issac.toolkit.logging;

/**
 * 以后改用Plog，为了和之前的代码兼容，保留该类
 * @deprecated
 * @author issac
 *
 */
public class ProgressLog {

	public static void progress_LV0(String msg) {
		ColorLog.info("");
		ColorLog.info("");
		ColorLog.info(".............>" + msg);
		ColorLog.info("");
	}

	public static void progress_LV1(String msg) {
		ColorLog.info("");
		ColorLog.info("........>" + msg);
	}

	public static void progress_LV2(String msg) {
		ColorLog.info(".....>" + msg);
	}
}
