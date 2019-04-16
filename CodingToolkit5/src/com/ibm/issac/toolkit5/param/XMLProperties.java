package com.ibm.issac.toolkit5.param;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * 解析XML格式的PROPERTIES配置，如果有其他的配置文件类型需要解析，可以考虑提供超类。
 * 
 * @author issac
 * 
 */
public class XMLProperties {
	public Properties parse(String fileName) {
		final Properties p = new Properties();
		try {
			FileInputStream pInStream = new FileInputStream(new File(fileName));
			p.loadFromXML(pInStream);
			p.list(System.out);
			return p;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
}
