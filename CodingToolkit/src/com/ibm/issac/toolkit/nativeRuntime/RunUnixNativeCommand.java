package com.ibm.issac.toolkit.nativeRuntime;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.timing.Pauser;

/**
 * ��AIX������OS���� ע�⣺�����Ͱѽű�������ֱ�ӽ���OS
 * 
 * @author issac
 * 
 */
public class RunUnixNativeCommand extends AbstractNativeCommandSupport {
	public int process(String sourceStr, boolean filterRequired) throws IOException, InterruptedException {
		String commandStr = "";
		// ����ű�Ҫ���滻�ض����ݣ������Ԥ���ĺ����滻
		if (filterRequired) {
			commandStr = this.filterSource(sourceStr);
		} else {
			commandStr = sourceStr;
		}
		DevLog.super_trace("[NATIVE CMD] Command line: >" + commandStr + "<");
		// ִ�д����Ľű�
		String[] commandArray = new String[] { "/bin/sh", "-c", commandStr };
		// ��������
		Runtime rt = Runtime.getRuntime();
		Process proc;
		proc = rt.exec(commandArray);
		// ��ӡ����������������
		NativeOutputFormatter errF = new NativeOutputFormatter(proc.getErrorStream(), "E");
		NativeOutputFormatter outF = new NativeOutputFormatter(proc.getInputStream(), "O");
		errF.start();
		outF.start();
		int retVal = proc.waitFor();
		// ---------------
		//�ȴ��ռ����ݵ��̴߳����ꡣ������Ӵ�����Щ�̻߳�û������ͻ�����ռ�����REPORTΪ��
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
