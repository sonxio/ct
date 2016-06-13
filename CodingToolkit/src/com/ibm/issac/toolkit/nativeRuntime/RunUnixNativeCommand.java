package com.ibm.issac.toolkit.nativeRuntime;

import java.io.IOException;

import com.ibm.issac.toolkit.DevLog;

/**
 * 在AIX下运行OS命令 注意：本类型把脚本的命令直接交给OS
 * 
 * @author issac
 * 
 */
public class RunUnixNativeCommand extends AbstractNativeCommandSupport {
	public int process(String sourceStr, boolean filterRequired) throws IOException, InterruptedException {
		String commandStr = "";
		// 如果脚本要求替换特定内容，则进行预定的函数替换
		if (filterRequired) {
			commandStr = this.filterSource(sourceStr);
		} else {
			commandStr = sourceStr;
		}
		DevLog.super_trace("[NATIVE CMD] Command line: >" + commandStr + "<");
		// 执行处理后的脚本
		String[] commandArray = new String[] { "/bin/sh", "-c", commandStr };
		// 启动命令
		Runtime rt = Runtime.getRuntime();
		Process proc;
		proc = rt.exec(commandArray);
		// 打印错误输出、正常输出
		StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "E");
		StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "O");
		errorGobbler.start();
		outputGobbler.start();
		int retVal = proc.waitFor();
		DevLog.super_trace("[NATIVE CMD] Process exitValue: " + retVal);
		outputM.put("last report", this.reportRunningInfo(retVal, outputGobbler, errorGobbler));
		outputM.put("error output", errorGobbler.getAllOutput().toString());
		return retVal;
	}

	private String filterSource(String sourceStr) {
		DevLog.super_trace("[NATIVE CMD] Command fitler will be implemented as instructed.");
		String filteredStr = sourceStr.replaceAll("\\$\\(basename \\$0\\)", "\\$SP_SOURCE_FILE_NAME");
		return filteredStr;
	}
}
