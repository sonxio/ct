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

	protected String reportRunningInfo(int retVal, StreamGobbler out, StreamGobbler err) {
		StringBuffer sb = new StringBuffer();
		sb.append("return code: ").append(retVal).append(SysProp.getLS());
		sb.append(out.reportInfo());
		sb.append(SysProp.getLS());
		sb.append(err.reportInfo());
		sb.append(SysProp.getLS());
		return sb.toString();
	}

	/**
	 * 检查OS命令的OUTPUT STREAM, ERROR STREAM的CLASS.
	 * 
	 * @author issac
	 * 
	 */
	final class StreamGobbler extends Thread {
		private InputStream is;
		private String type;
		private StringBuffer infoSb;
		private StringBuffer allOutputSb;

		StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
			infoSb = new StringBuffer();
			allOutputSb = new StringBuffer();
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					DevLog.info("[NATIVE CMD] " + type + "> " + line);
					// 判断是否需要记录该输出
					allOutputSb.append(line).append(SysProp.getLS());
					if (line.startsWith(SysProp.b_str("issac.native.reportKeyword", "REPORT:"))) {
						infoSb.append(line).append(SysProp.getLS());
					}
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		public StringBuffer reportInfo() {
			return infoSb;
		}

		public StringBuffer getAllOutput() {
			return this.allOutputSb;
		}
	}
}
