package com.ibm.issac.toolkit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import com.ibm.issac.toolkit.param.SysProp;
import com.ibm.issac.toolkit.util.DateUtil;
import com.ibm.issac.toolkit.util.StringUtil;

/**
 * Logger��ΪCONSOLE��ӡ��FILE��ӡ��������CONSOLEֻ������ʾ������Ϣ����FILE����ʾ������Ϣ
 * 
 * @author issac
 * 
 */
public final class DevLog {
	private static FileWriter logWriter;
	private static OutputStream os1; // �������NULL���������������OS�У�������SERVLET���������

	/**
	 * ѡ����TEXT����HTML�����־
	 * 
	 * @param logModeStr
	 */
	public static void setOutputStream1(OutputStream os) {
		DevLog.os1 = os;
	}

	/**
	 * ������ø÷������������ļ���־�����򲻴�ӡ�ļ���־��
	 * @param appName
	 */
	public static void writeFileLog(String appName){
		DevLog.init(appName);
	}
	
	/**
	 * ������ø÷������������ļ���־�����򲻴�ӡ�ļ���־��
	 * @deprecated ����û���������ֳ����壬DevLog����Ҫ��ʼ�����������ֻ�����������ļ���־�ġ�
	 * @param appName
	 */
	public static void init(String appName) {
		// -----------------------------------
		// ȷ����־�ļ����ɵ�λ��
		if (!SysProp.b_bool("issac.DevLog.writeFile", true)) {
			logWriter = null;// ������д��־�ļ����Ӷ������������ģ�����J2EE��System.out�Ѿ�д��������Ӧ�������
			DevLog.trace("[DevLog] DevLog will not write to a file.");
			return;
		}
		// ���û���ر�ָ��LOG FILE���ļ�λ�ã�����ݻ�������������־�ļ�
		String logFileName = SysProp.pstr("song.logFileNameWithPath");
		if (StringUtil.isEmpty(logFileName)) {
			//DELAYED_TODO ��windows����BUG������ļ����ǿյģ����ܺ�java.io.tmpdir���������\�йأ�����ͨ������song.logFileNameWithPath������ܡ�
			logFileName = SysProp.b_str("java.io.tmpdir", "./") + SysProp.getFS() + "devlog_" + appName + "_" + DateUtil.getNow("yyyyMMdd") + ".log";
		}
		try {
			DevLog.trace("[DevLog] IRE devlog written to " + logFileName);
			logWriter = new FileWriter(logFileName, true);
		} catch (IOException e) {
			DevLog.warn("[DevLog] Failed initiating DevLog. Logs will not be saved to disk.");
			e.printStackTrace();
		}
	}

	protected void finalize() throws Throwable {
		if (logWriter != null)
			logWriter.close();
		super.finalize();
	}

	public static String buildPrefixedLog(String type, String msg) {
		// ΪLOG����ʱ���
		StringBuffer sb = new StringBuffer(DateUtil.getNow("yyyy-MM-dd HH:mm:ss.SSS "));
		// �����߳�����
		sb.append(Thread.currentThread().getName()).append(' ');
		// ������Ϣ���
		// if (appName != null) {
		// sb.append(appName.toUpperCase());
		// }
		sb.append(type).append(' ');
		// ������־����
		sb.append(msg);
		sb.append('\n');
		return sb.toString();
	}

	public static void debug(String logStr) {
		DevLog.writeLog("D", logStr);
	}

	public static void trace(String logStr) {
		DevLog.writeLog("T", logStr);
	}

	/**
	 * super trace������TRACE��־���ǳ��������
	 * 
	 * @param logStr
	 */
	public static void super_trace(String logStr) {
		DevLog.writeLog("S", logStr);
	}

	private static void writeLog(String type, String logStr) {
		final String prefixedLog = DevLog.buildPrefixedLog(type, logStr);
		// ���ļ��м�¼��־��������־�ȼ�������Σ��������ļ��м�¼��������Զ��TEXT��ʽ��¼
		if (logWriter != null) {
			try {
				logWriter.write(prefixedLog);
				logWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// ������־�������;����Ƿ���SYSTEM OUT��ӡ��־����
		String logLevel = SysProp.b_str("issac.logLevel", "-S-T-D-I-W-E-");
		if (logLevel.indexOf(type) < 0) { // ����ӡlogLevel�����в������������־
			return;
		}
		if(SysProp.b_bool("song.devlog.printNothingInSystemOut", false)){
			return;
		}
		// ��SYSTEM OUT����ʾ��־�����ڽ�����ʾ��������ʱ�䡢�̣߳�������WAS,TOMCAT��Ӧ������ʾʱ��ʾ�������ʱ�䡣
		// ������ʾ����ʾPREFIXED LOG��ֻ��ʾ�ؼ�����
		if(SysProp.b_bool("song.devlog.printDateTimeInSystemOut", true)){
			DevLog.display(prefixedLog);
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(logStr).append('\n');
		DevLog.display(sb.toString());
	}

	/**
	 * ��ʾ���������־
	 * 
	 * @deprecated ����PLOG
	 * @param logStr
	 */
	public static void progress(String logStr) {
		DevLog.writeLog("D", logStr);
	}

	public static void info(String logStr) {
		DevLog.writeLog("I", logStr);
	}

	public static void warn(String logStr) {
		DevLog.writeLog("W", logStr);
	}

	public static void error(String logStr) {
		DevLog.writeLog("E", logStr);
	}

	/**
	 * show string without any header
	 * 
	 * @param logStr
	 */
	public static void display(String logStr) {
		// ΪHTML��ʽ�����ر���
		if (DevLog.os1 != null) {
			StringBuffer sb = new StringBuffer("");
			sb.append("<pre>").append(logStr).append("</pre>");
			try {
				os1.write(sb.toString().getBytes());
			} catch (IOException e) {
				System.out.println("[DevLog] Error displaying data. " + logStr);
				e.printStackTrace();
			}
			return;
		}
		// ��HTML����ֱ�Ӵ�ӡ��־
		System.out.print(logStr);
	}

	/**
	 * show string without any header
	 * 
	 * @param logStr
	 */
	public static void displayln(String logStr) {
		System.out.println(logStr);
	}
}
