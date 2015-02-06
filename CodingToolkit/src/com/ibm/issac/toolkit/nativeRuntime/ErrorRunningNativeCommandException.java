package com.ibm.issac.toolkit.nativeRuntime;

import com.ibm.issac.toolkit.DevLog;

/**
 * 执行NATIVE命令时出现了错误返回。
 * 对于NATIVE命令，即使正确执行了，执行结果可能也是表示错误的结果，通常用0表示正确，其他为错误。
 * @author issac
 *
 */
public class ErrorRunningNativeCommandException extends Exception {

	public ErrorRunningNativeCommandException(String errmsg) {
		super(errmsg);
		DevLog.error(errmsg);
	}
}
