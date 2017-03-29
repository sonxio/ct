package com.ibm.issac.toolkit.nativeRuntime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.param.SysProp;

/**
 * ����OS�����OUTPUT STREAM, ERROR STREAM��CLASS.���������ĸ�ʽ�����ࡣ
 * 
 * @author issac
 * 
 */
final class NativeOutputFormatter extends Thread {
	private InputStream is;
	private String type;
	private StringBuffer reportSb;// ��������REPORT�ؼ��ʵ������Ϣ���ϣ���ֻ��¼����REPORT�ؼ��ʵ������Ϣ
	private StringBuffer completeSb;// ������ȫ�������Ϣ����
	private boolean processingFinished = false; // �Ƿ��Ѿ������ꡣ����ȷ���첽�̴߳������

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
				// ��ӡ����������
				DevLog.info("[NATIVE CMD] " + type + "> " + line);
				// �����Ϣһ�ɷ���COMPLETE SB��
				completeSb.append(line).append(SysProp.getLS());
				// �������REPORT�ؼ��ʣ������REPORT SB
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
	 * ��������REPORT�ؼ��ʵ������Ϣ���ϣ���ֻ��¼����REPORT�ؼ��ʵ������Ϣ
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
	 * ��ȡ����ȫ�������Ϣ�ļ��ϡ�
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
	 * �Ƿ��Ѿ������ꡣ����ȷ���첽�̴߳������
	 * 
	 * @return
	 */
	public boolean isProcessingFinished() {
		return processingFinished;
	}
}