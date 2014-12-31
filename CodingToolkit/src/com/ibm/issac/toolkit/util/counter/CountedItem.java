package com.ibm.issac.toolkit.util.counter;

/**
 * 用来统计的一项数据
 * 
 * @author issac
 * 
 */
public class CountedItem {
	private String desc="";
	private int count=0;

	public CountedItem(String desc) {
		super();
		this.desc = desc;
		this.count = 0;
	}
	
	public void addOneMore(){
		this.count++;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
