package com.ibm.issac.toolkit.nativeRuntime;

import java.io.IOException;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.param.SysProp;
import com.ibm.issac.toolkit.timing.Pauser;

/**
 * ��WINDOWS���������� <br/>
 * ע�⣺ Ŀǰ����ƴ������⣺ÿ��ֻ�����е�һ��������к�������ǲ���ִ�еġ�<br/>
 * ��Ϊ������Ƶ�������AIX�ϣ���������ʱ���á�
 * 
 * @author issac
 *
 */
public class RunWindowsNativeCommand extends AbstractNativeCommandSupport {
	public int process(String sourceStr, boolean filterRequired) throws IOException, InterruptedException {
		String commandLine = sourceStr.replaceAll("/", "\\\\");
		DevLog.super_trace("[NATIVE CMD] Command line: >" + commandLine + "<");
		String[] commandArray = new String[] { "cmd.exe", "/C", commandLine };
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
		DevLog.super_trace("[NATIVE CMD] Process exitValue: " + retVal);
		// ����ִ�б���
		outputM.put("last report", this.reportRunningInfo(retVal, outF, errF));
		outputM.put("error output", errF.getCompleteOutput());
		return retVal;
	}
}
