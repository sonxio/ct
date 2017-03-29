package com.ibm.issac.toolkit.logging;

/**
 * 打印一些不便打印的类型日志。
 * @author song
 *
 */
public class Reporter {
	
	public static String rArray(Object[] oA){
		if(oA==null)
			return null;
		StringBuffer sb = new StringBuffer("{");
		for(int i=0; i<oA.length; i++){
			sb.append(oA[i]).append(", ");
		}
		sb.append("}");
		return sb.toString();
	}
}
