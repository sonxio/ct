package com.ibm.issac.toolkit.condition;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个condition列表
 * @author song
 *
 */
public abstract class ConditionChecker {
	protected List<Condition> cL;
	/**
	 * 检查一个值是否满足该列表中所有condition的需求
	 * @param val
	 * @return
	 */
	public abstract boolean check(Object val);
	public List<Condition> getcL() {
		return cL;
	}
	public void setcL(List<Condition> cL) {
		this.cL = cL;
	}
	
}
