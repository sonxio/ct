package com.ibm.issac.toolkit.util.counter;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现统计，必须实例化，以便同时存在多个COUNTER
 * @author issac
 *
 */
public class Counter {
	private Map dataM = new HashMap();
	
	public void setDesc(String id, String desc){
		CountedItem item = (CountedItem) dataM.get(id);
		if(item==null){
			CountedItem i2 = new CountedItem(desc);
			i2.addOneMore();
			dataM.put(id, i2);
			return;
		}
		item.setDesc(desc);
	}
	
	public void addOneMore(String id){
		CountedItem item = (CountedItem) dataM.get(id);
		if(item==null){
			CountedItem i2 = new CountedItem("Not described.");
			i2.addOneMore();
			dataM.put(id, i2);
			return;
		}
		item.addOneMore();
	}
}
