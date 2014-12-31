package com.ibm.issac.toolkit;

import java.util.Date;

/**
 * produce numbers\texts randomly.
 * 
 * @author issac
 * 
 */
public class Cube {
	/**
	 * produce a random long number
	 * 
	 * @return
	 */
	public static long produceRandomLong() {
		return System.currentTimeMillis() % 1000;
	}

	/**
	 * 产生一个随机的正数<br/>
	 * 
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static int produceRandomInteger(int minValue, int maxValue) {
		int temp = (int) Math.round(Math.random() * (maxValue - minValue) + minValue);
		return temp;
	}

	public static boolean produceRandomBoolean() {
		int i = Cube.produceRandomInteger(0, 10);
		return i % 2 == 0;
	}

	/**
	 * 生成用于ID的随机文本，提供前缀
	 * 
	 * @param prefix
	 * @return
	 */
	public static String produceId(String prefix) {
		String date = DatetimeUtility.formatDate("_yyMMddHHmmss_", new Date());
		return prefix + date + Cube.produceRandomInteger(10000, 99999);
	}
}
