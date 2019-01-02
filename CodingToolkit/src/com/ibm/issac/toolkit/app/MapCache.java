package com.ibm.issac.toolkit.app;

import java.util.HashMap;
import java.util.Map;

import com.ibm.issac.toolkit.DevLog;

public class MapCache {
	public static MapCache ins = new MapCache();
	private Map m;

	private MapCache() {
		m = new HashMap();
		DevLog.super_trace("[MapCache] MapCache initiated.");
	}

	public void put(String key, Object val) {
		m.put(key, val);
		DevLog.super_trace("[MapCache] cached >" + key + "<");
	}

	public Object get(String key) {
		DevLog.super_trace("[MapCache] getting >" + key + "<");
		return m.get(key);
	}
}
