package com.ibm.issac.toolkit.logging;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	public static String rMap(Map m) {
		Set s = m.keySet();
		Iterator it = s.iterator();
		StringBuffer sb = new StringBuffer();
		while(it.hasNext()) {
			Object key = it.next();
			Object value = m.get(key);
			sb.append(key).append(" : ").append(value).append(", ");
		}
		return sb.toString();
	}
	
	/**
	 * JDK实际会直接把LIST打印出来
	 * @param l
	 * @return
	 */
	public static String rList(List l) {
		return l.toString();
	}
}
