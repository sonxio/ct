package com.ibm.issac.toolkit.param;

import java.util.HashMap;
import java.util.Map;

import com.ibm.issac.toolkit.DevLog;

/**
 * 使用-D参数返回值； 因为这种参数提供方式很容易遗忘，所以所有调用到时，都要求打印调用日志。
 * 
 * @author issac
 * 
 */
public class SysProp {
	private static Map helpM = new HashMap(); // 缓存对参数的解释
	private static Map historyM = new HashMap(); // 保存访问过的KEY，避免重复打印对同一个KEY的访问日志

	public static void cacheHelp(String pName, String helpStr) {
		SysProp.helpM.put(pName, helpStr);
	}

	/**
	 * @deprecated 如果需要显示帮助，建议单独调用cacheHelp，以免在这个类里面方法参数过度复杂
	 * @param pName
	 * @param help
	 * @return
	 */
	public static boolean pboolean(String pName, String help) {
		cacheHelp(pName, help);
		return pboolean(pName);
	}

	/**
	 * @deprecated 换用b_bool
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
		// 保存访问记录，并生成访问日志
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
	 * @deprecated 建议不做PUBLIC调用，以后把int名称用来表示int类型，Int用来表示Integer类型
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
		// 保存访问记录，并生成访问日志
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
		// 生成处理结果描述
		String resultDesc = parsedVal == null ? "DefaultVal: " + defaultVal : "ParsedVal: " + parsedVal.intValue();
		// 保存访问记录，并生成访问日志
		SysProp.printDebugMsg(pName, resultDesc, setValStr);
		return parsedVal == null ? defaultVal : parsedVal.intValue();
	}

	private static void printDebugMsg(String pName, String resultDesc, String setValStr) {
		if (!historyM.containsKey(pName)) {
			historyM.put(pName, new Integer(0));
			// 生成DEBUG信息
			StringBuffer sb = new StringBuffer("[SysProp] ");
			sb.append(pName).append("=").append(resultDesc).append(", SetVal : ").append(setValStr);
			appendHelp(pName, sb);
			DevLog.debug(sb.toString());
		}
	}

	private static void appendHelp(String pName, StringBuffer sb) {
		// 如果有帮助内容，则显示出来：
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
	 * 获得行分隔符
	 * @return
	 */
	public static String getLS() {
		return SysProp.pstr("line.separator");
	}

	/**
	 * 获得文件分隔符，即目录分隔符
	 * @return
	 */
	public static String getFS() {
		return SysProp.pstr("file.separator");
	}

	public static String getOSName() {
		return SysProp.pstr("os.name");
	}

	/**
	 * @return 当前OS脚本的附文件名，包含点
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
