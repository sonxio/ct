package com.ibm.issac.toolkit.condition;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示任何一种查询条件<br/> 
 * 目前condition通用类的使用场景不多，如果以后更加复杂化，可以考虑每个condition都实现为单独的类
 * 
 * @author song
 *
 */
public class Condition {
	private String conditionId;
	private List<Object> varL = new ArrayList<Object>();
	private boolean passed; // 检查结果，需要在condition检查后设置

	/**
	 * 创建一个只有1个参考参数的condition
	 * @param conditionId
	 * @param val1
	 */
	public Condition(String conditionId, Object var1) {
		this.conditionId=conditionId;
		this.varL.add(var1);
	}
	
	@Override
	public String toString() {
		return "Condition [conditionId=" + conditionId + ", varL=" + varL + ", result=" + passed + "]";
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean result) {
		this.passed = result;
	}

	public String getConditionId() {
		return conditionId;
	}

	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

	public List<Object> getVarL() {
		return varL;
	}

	public void setVarL(List<Object> varL) {
		this.varL = varL;
	}

	public Object getFirstValue() {
		return varL.get(0);
	}

}
