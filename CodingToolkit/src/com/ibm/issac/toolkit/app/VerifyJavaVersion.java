package com.ibm.issac.toolkit.app;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.format.StF;
import com.ibm.issac.toolkit.param.SysProp;

public class VerifyJavaVersion {
	/**
	 * 当版本错误时，发生RUNTIME异常并退出。<br/>
	 * 不支持第二位版本，只能比较 1.6>1.5，下一级小版本不能比较。<br/>
	 * 注意：这个方法只有在所用JDK版本和编译JDK版本兼容时，要特别指定的版本，才会有意义。举个反例：对于JAVA 1.5编译的类，如果使用JDK 1.4运行，直接JDK就会报错IncompatibleJavaVersionError（名称可能有误），不能运行这个方法的内容。
	 * @param versionStr
	 * @param comment
	 */
	public static boolean isMinimalJavaVersionSatisfied(float minVer, String comment){
		final String currentVer = SysProp.pstr("java.version");
		DevLog.trace("VerifyJavaVersion#Java version at "+StF.quoted(currentVer));
		//获取前三位解析为数字，用于比较大小
		final String subVer = currentVer.substring(0, 3);
		final float verF = Float.valueOf(subVer).floatValue();
		DevLog.trace("VerifyJavaVersion#converted float: "+verF);
		if(verF < minVer){
			DevLog.error(comment);
			DevLog.trace("VerifyJavaVersion#Current java version: "+currentVer+" does not satisfy minimal prerequisite for this program: "+verF+".");
			return false;
		}
		return true;
	}
}
