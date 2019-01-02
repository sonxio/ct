package com.ibm.issac.toolkit.util;

import com.ibm.issac.toolkit.DevLog;

/**
 * 比较LIST中是否有MATCH指定对象的对象。
 * 
 * @author issac
 * 
 */
public class Matcher {
	private String title = "";
	private Object standardObj; // 用作标准的OBJ

	public Matcher(String title, Object standardObj) {
		this.title = title;
		this.standardObj = standardObj;
		DevLog.trace("[Matcher-" + title + "] Matching standard obj: >" + standardObj+"< " + EncodingUtil.r(standardObj));
	}

	/**
	 * 判断两个对象是否MATCH
	 * 
	 * @param obj1
	 *            当前要对比的OBJ（每次都要变化）
	 * @return
	 */
	public boolean match(Object obj1) {
		DevLog.trace("[Matcher-" + title + "] Comparing >" + obj1 + "< " + EncodingUtil.r(obj1));
		if (obj1 == null && standardObj == null) {
			DevLog.debug("[Matcher-" + title + "] obj1 and standardObj are both null. They are considered matched.");
			return true;
		}
		if (obj1 == null) {
			DevLog.debug("[Matcher-" + title + "] When trying to find a match for >" + standardObj + "<, obj1 for comparison is null. This obj1 is ignored.");
			return false;
		}
		if (obj1.equals(standardObj)) {
			DevLog.trace("[Matcher-" + title + "] A match is found for standardObj >" + standardObj + "<.");
			return true;
		}
		return false;
	}

	public String reportNotMatched() {
		return "[Matcher-" + title + "] No match found, standard: " + standardObj;
	}
}
