package com.ibm.issac.toolkit.nativeRuntime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.param.SysProp;

/**
 * 处理OS命令的OUTPUT STREAM, ERROR STREAM的CLASS.整理成理想的格式，分类。
 * 
 * @author issac
 * 
 */
final class NativeOutputFormatter extends Thread {
	private InputStream is;
	private String type;
	private StringBuffer reportSb;// 包含带有REPORT关键词的输出信息集合，即只记录带有REPORT关键词的输出信息
	private StringBuffer completeSb;// 包含了全部输出信息集合
	private boolean processingFinished = false; // 是否已经处理完。用来确保异步线程处理结束

	public NativeOutputFormatter(InputStream is, String type) {
		this.is = is;
		this.type = type;
		reportSb = new StringBuffer();
		completeSb = new StringBuffer();
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				// 打印所有输出结果
				DevLog.info("[NATIVE CMD] " + type + "> " + line);
				// 输出信息一律放入COMPLETE SB。
				completeSb.append(line).append(SysProp.getLS());
				// 如果带有REPORT关键词，则放入REPORT SB
				if (line.startsWith(SysProp.b_str("issac.native.reportKeyword", "REPORT:"))) {
					reportSb.append(line).append(SysProp.getLS());
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			this.processingFinished = true;
		}
	}

	/**
	 * 包含带有REPORT关键词的输出信息集合，即只记录带有REPORT关键词的输出信息
	 * 
	 * @return
	 */
	public StringBuffer getReportOutput() {
		if(!this.processingFinished){
			throw new RuntimeException("Attempting to get native output before finishing processing.");
		}
		return reportSb;
	}

	/**
	 * 获取包含全部输出信息的集合。
	 * 
	 * @return
	 */
	public StringBuffer getCompleteOutput() {
		if(!this.processingFinished){
			throw new RuntimeException("Attempting to get native output before finishing processing.");
		}

		return this.completeSb;
	}

	/**
	 * 是否已经处理完。用来确保异步线程处理结束
	 * 
	 * @return
	 */
	public boolean isProcessingFinished() {
		return processingFinished;
	}
}