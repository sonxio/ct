package com.ibm.issac.toolkit.nativeRuntime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.param.SysProp;

public abstract class AbstractNativeCommandSupport {
	protected static final int BUFFER = 2048;
	protected Map outputM = new HashMap(); // 最后一次运行的运行时数据

	public abstract int process(String sourceStr, boolean filterRequired) throws IOException,InterruptedException;

	public Map getOutputM() {
		return outputM;
	}

	/**
	 * 打印进程执行中的输出
	 * 
	 * @param proc
	 * @throws IOException
	 */
	protected void printCommandOutput(Process proc) throws IOException {
		InputStream is = proc.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String outputStr = "";
		while ((outputStr = br.readLine()) != null) {
			DevLog.debug(outputStr);
		}
	}

	protected String reportRunningInfo(int retVal, NativeOutputFormatter out, NativeOutputFormatter err) {
		StringBuffer sb = new StringBuffer();
		sb.append("return code: ").append(retVal).append(SysProp.getLS());
		sb.append(out.getReportOutput());
		sb.append(SysProp.getLS());
		sb.append(err.getReportOutput());
		sb.append(SysProp.getLS());
		return sb.toString();
	}
}
