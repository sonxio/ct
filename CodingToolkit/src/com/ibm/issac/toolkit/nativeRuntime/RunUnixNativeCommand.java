package com.ibm.issac.toolkit.nativeRuntime;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.timing.Pauser;

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
		NativeOutputFormatter errF = new NativeOutputFormatter(proc.getErrorStream(), "E");
		NativeOutputFormatter outF = new NativeOutputFormatter(proc.getInputStream(), "O");
		errF.start();
		outF.start();
		int retVal = proc.waitFor();
		// ---------------
		//等待收集数据的线程处理完。如果不加处理，这些线程还没处理完就会造成收集到的REPORT为空
		DevLog.super_trace("processing command output...");
		while(!(errF.isProcessingFinished()&&outF.isProcessingFinished())){
			Pauser.pauseThreadForException(200);
		}
		DevLog.super_trace("output processing finished.");
		// --------------------
		DevLog.super_trace("[NATIVE CMD] Process exitValue: " + retVal + ". error output length: " + errF.getCompleteOutput().length() + ". normal output length: " + outF.getCompleteOutput().length());
		outputM.put("last report", this.reportRunningInfo(retVal, outF, errF));
		outputM.put("error output", errF.getCompleteOutput());
		outputM.put("normal output", outF.getCompleteOutput());
		return retVal;
	}

	private String filterSource(String sourceStr) {
		DevLog.super_trace("[NATIVE CMD] Command fitler will be implemented as instructed.");
		String filteredStr = sourceStr.replaceAll("\\$\\(basename \\$0\\)", "\\$SP_SOURCE_FILE_NAME");
		return filteredStr;
	}
}
