package com.ibm.issac.toolkit.timing;

import java.io.IOException;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.logging.ColorLog;

/**
 * ��ָͣ��ʱ�䳤��
 * 
 * @author issac
 * 
 */
public class Pauser {
	public static void pauseThread(long ms) {
		try {
			DevLog.trace("Pausing "+ms+"ms...");
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			ColorLog.info("You have interrupted a paused thread.");
		}
	}

	public static void pauseThreadForException(long ms) throws InterruptedException {
		DevLog.super_trace("Pausing "+ms+"ms..."); 
		Thread.sleep(ms);
	}

	public static void pressAnyKeyToContinue() {
		DevLog.info("Press any key to continue...");
		try {
			int i = System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
