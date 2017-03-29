package com.ibm.issac.toolkit.nativeRuntime;

import java.io.IOException;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.param.SysProp;
import com.ibm.issac.toolkit.timing.Pauser;

/**
 * 在WINDOWS下运行命令 <br/>
 * 注意： 目前该设计存在问题：每次只能运行第一行命令，换行后的命令是不能执行的。<br/>
 * 因为程序设计的重心在AIX上，该问题暂时搁置。
 * 
 * @author issac
 *
 */
public class RunWindowsNativeCommand extends AbstractNativeCommandSupport {
	public int process(String sourceStr, boolean filterRequired) throws IOException, InterruptedException {
		String commandLine = sourceStr.replaceAll("/", "\\\\");
		DevLog.super_trace("[NATIVE CMD] Command line: >" + commandLine + "<");
		String[] commandArray = new String[] { "cmd.exe", "/C", commandLine };
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
		DevLog.super_trace("[NATIVE CMD] Process exitValue: " + retVal);
		// 生成执行报告
		outputM.put("last report", this.reportRunningInfo(retVal, outF, errF));
		outputM.put("error output", errF.getCompleteOutput());
		return retVal;
	}
}
