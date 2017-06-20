package com.ibm.issac.toolkit.param;

import java.util.HashMap;
import java.util.Map;

import com.ibm.issac.toolkit.DevLog;

/**
 * ʹ��-D��������ֵ�� ��Ϊ���ֲ����ṩ��ʽ�������������������е��õ�ʱ����Ҫ���ӡ������־��
 * 
 * @author issac
 * 
 */
public class SysProp {
	private static Map helpM = new HashMap(); // ����Բ����Ľ���
	private static Map historyM = new HashMap(); // ������ʹ���KEY�������ظ���ӡ��ͬһ��KEY�ķ�����־

	public static void cacheHelp(String pName, String helpStr) {
		SysProp.helpM.put(pName, helpStr);
	}

	/**
	 * @deprecated �����Ҫ��ʾ���������鵥������cacheHelp����������������淽���������ȸ���
	 * @param pName
	 * @param help
	 * @return
	 */
	public static boolean pboolean(String pName, String help) {
		cacheHelp(pName, help);
		return pboolean(pName);
	}

	/**
	 * @deprecated ����b_bool
	 * @param pName
	 * @return
	 */
	public static boolean pboolean(String pName) {
		String s = System.getProperty(pName);
		boolean result = false;
		if (s == null) {
			result = false;
		}
		result = Boolean.valueOf(s).booleanValue();
		// ������ʼ�¼�������ɷ�����־
		if (!historyM.containsKey(pName)) {
			historyM.put(pName, s);
			StringBuffer sb = new StringBuffer("[SysProp] ");
			sb.append(pName).append("=").append(result).append(", value : ").append(s);
			appendHelp(pName, sb);
			DevLog.debug(sb.toString());
		}
		return result;
	}

	public static boolean b_bool(String pName, boolean defaultVal) {
		String setValStr = System.getProperty(pName);
		boolean parsedVal = false;
		String resultDesc;
		if (setValStr == null) {
			parsedVal = defaultVal;
			resultDesc = "DefaultVal: " + defaultVal;
		} else {
			parsedVal = Boolean.valueOf(setValStr).booleanValue();
			resultDesc = "SetVal: " + parsedVal;
		}
		SysProp.printDebugMsg(pName, resultDesc, setValStr);
		return parsedVal;
	}

	/**
	 * @deprecated ���鲻��PUBLIC���ã��Ժ��int����������ʾint���ͣ�Int������ʾInteger����
	 * @param pName
	 * @return
	 */
	public static Integer pint(String pName) {
		String s = System.getProperty(pName);
		Integer result = null;
		if (s == null) {
			result = null;
		} else {
			result = Integer.valueOf(s);
		}
		// ������ʼ�¼�������ɷ�����־
		if (!historyM.containsKey(pName)) {
			historyM.put(pName, new Integer(0));
			StringBuffer sb = new StringBuffer("[SysProp] ");
			sb.append(pName).append("=").append(result).append(", value : ").append(s);
			appendHelp(pName, sb);
			DevLog.debug(sb.toString());
		}
		return result;
	}

	/**
	 * default-value-provided int.
	 * 
	 * @param pName
	 * @param defaultVal
	 * @return
	 */
	public static int d_int(String pName, int defaultVal) {
		String setValStr = System.getProperty(pName);
		Integer parsedVal = null;
		if (setValStr == null) {
			parsedVal = null;
		} else {
			parsedVal = Integer.valueOf(setValStr);
		}
		// ���ɴ���������
		String resultDesc = parsedVal == null ? "DefaultVal: " + defaultVal : "ParsedVal: " + parsedVal.intValue();
		// ������ʼ�¼�������ɷ�����־
		SysProp.printDebugMsg(pName, resultDesc, setValStr);
		return parsedVal == null ? defaultVal : parsedVal.intValue();
	}

	private static void printDebugMsg(String pName, String resultDesc, String setValStr) {
		if (!historyM.containsKey(pName)) {
			historyM.put(pName, new Integer(0));
			// ����DEBUG��Ϣ
			StringBuffer sb = new StringBuffer("[SysProp] ");
			sb.append(pName).append("=").append(resultDesc).append(", SetVal : ").append(setValStr);
			appendHelp(pName, sb);
			DevLog.debug(sb.toString());
		}
	}

	private static void appendHelp(String pName, StringBuffer sb) {
		// ����а������ݣ�����ʾ������
		String helpStr = (String) helpM.get(pName);
		if (helpStr != null)
			sb.append(", Help: ").append(helpStr);
	}

	public static String pstr(String pName, String help) {
		cacheHelp(pName, help);
		return pstr(pName);
	}

	public static String pstr(String pName) {
		String s = System.getProperty(pName);
		if (!historyM.containsKey(pName)) {
			historyM.put(pName, new Integer(0));
			StringBuffer sb = new StringBuffer("[SysProp] ");
			sb.append(pName).append("=").append(s);
			appendHelp(pName, sb);
			DevLog.debug(sb.toString());
		}
		return s;
	}

	public static String b_str(String pName, String defaultVal) {
		String setVal = System.getProperty(pName);
		String parsedVal;
		String resultDesc;
		if (setVal == null) {
			parsedVal = defaultVal;
			resultDesc = "DefaultVal: " + defaultVal;
		} else {
			parsedVal = setVal;
			resultDesc = "SetVal: " + setVal;
		}
		SysProp.printDebugMsg(pName, resultDesc, setVal);
		return parsedVal;
	}

	/**
	 * ����зָ���
	 * @return
	 */
	public static String getLS() {
		return SysProp.pstr("line.separator");
	}

	/**
	 * ����ļ��ָ�������Ŀ¼�ָ���
	 * @return
	 */
	public static String getFS() {
		return SysProp.pstr("file.separator");
	}

	public static String getOSName() {
		return SysProp.pstr("os.name");
	}

	/**
	 * @return ��ǰOS�ű��ĸ��ļ�����������
	 */
	public static String getShellSurfix() {
		final String osName = SysProp.getOSName();
		if (osName.startsWith("Windows")) {
			return ".bat";
		}
		if (osName.startsWith("AIX")) {
			return ".sh";
		}
		if (osName.startsWith("Linux")) {
			return ".sh";
		}
		if (osName.startsWith("Mac OS X")) {
			return ".sh";
		}
		DevLog.trace("Uncategoried OS: " + osName + ". Shell surfix will default to .sh.");
		return ".sh";
	}
}
