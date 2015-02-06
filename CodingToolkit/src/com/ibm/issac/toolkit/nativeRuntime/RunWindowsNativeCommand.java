package com.ibm.issac.toolkit.nativeRuntime;

import java.io.IOException;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.param.SysProp;

/**
 * 在WINDOWS下运行命令 <br/>
 * TODO 目前该设计存在问题：每次只能运行第一行命令，换行后的命令是不能执行的。<br/>
 * 因为程序设计的重心在AIX上，该问题暂时搁置。
 * 
 * @author issac
 *
 */
public class RunWindowsNativeCommand extends AbstractNativeCommandSupport {
	public int process(String sourceStr, boolean filterRequired) throws IOException, InterruptedException {
		String commandLine = sourceStr.replaceAll("/", "\\\\");
		DevLog.trace("[NATIVE CMD] Command line: >" + commandLine + "<");
		String[] commandArray = new String[] { "cmd.exe", "/C", commandLine };
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
		DevLog.debug("[NATIVE CMD] Process exitValue: " + retVal);
		// 生成执行报告
		outputM.put("last report", this.reportRunningInfo(retVal, outputGobbler, errorGobbler));
		outputM.put("error output", errorGobbler.getAllOutput());
		return retVal;
	}
}
