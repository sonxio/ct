package com.ibm.issac.toolkit.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.logging.Plog;
import com.ibm.issac.toolkit.validation.StringValidation;

public class ConsoleInput {

	/**
	 * 收集用户输入的字符串
	 * 
	 * @param question
	 * @param 用户直接按回车的情况下给出默认值
	 * @return
	 * @throws IOException
	 */
	public static String answerString(String question, String defaultVal) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Plog.p2(question);
		String answer = br.readLine();
		if (StringValidation.isStringReadable(answer))
			return answer;
		return defaultVal;

	}

	/**
	 * 收集用户输入的整数
	 * 
	 * @param question
	 * @return
	 * @throws IOException
	 */
	public static int answerInt(String question, int defaultVal) throws IOException {
		String ansStr = ConsoleInput.answerString(question, null);
		try {
			int i = Integer.valueOf(ansStr).intValue();
			return i;
		} catch (Exception e) {
			DevLog.warn("对于该问题的回答不能解析为int，将使用默认值" + defaultVal + "，问题是：" + question);
			e.printStackTrace();
			return defaultVal;
		}
	}

	public static double answerDouble(String question, double dv) throws IOException {
		String ansStr = ConsoleInput.answerString(question, null);
		try {
			double dbl = Double.valueOf(ansStr).doubleValue();
			return dbl;
		} catch (Exception e) {
			DevLog.warn("对于该问题的回答不能解析为double，将使用默认值" + dv + "，问题是：" + question);
			e.printStackTrace();
			return dv;
		}
	}

	public static float answerFloat(String question, float dv) throws IOException {
		try {
			String ansStr = ConsoleInput.answerString(question, null);
			return Float.valueOf(ansStr).floatValue();
		} catch (Exception e) {
			DevLog.warn("对于该问题的回答不能解析为float，将使用默认值" + dv + "，问题是：" + question);
			e.printStackTrace();
			return dv;
		}
	}
}
