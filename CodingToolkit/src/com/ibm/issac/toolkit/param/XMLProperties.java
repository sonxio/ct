package com.ibm.issac.toolkit.param;

import java.util.Properties;

/**
 * 解析XML格式的PROPERTIES配置，如果有其他的配置文件类型需要解析，可以考虑提供超类。
 * 
 * @deprecated Not Compatible with IBM-JDK 1.4.2
 * @author issac
 * 
 */
public class XMLProperties {
	public Properties parse(String fileName) {
		final Properties p = new Properties();
		/*
		try {
			FileInputStream pInStream = new FileInputStream(new File(fileName));
			// p.loadFromXML(pInStream);
			p.list(System.out);
			return p;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		return p;
	}
}
