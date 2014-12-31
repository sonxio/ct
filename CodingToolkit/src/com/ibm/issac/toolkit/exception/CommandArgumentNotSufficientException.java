package com.ibm.issac.toolkit.exception;

import com.ibm.issac.toolkit.DevLog;

public class CommandArgumentNotSufficientException extends Exception {

	public CommandArgumentNotSufficientException(String str) {
		DevLog.warn(str);
	}

}
