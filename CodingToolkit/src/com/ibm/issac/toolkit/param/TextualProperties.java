package com.ibm.issac.toolkit.param;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.logging.ColorLog;

/**
 * 解析TEXT格式的PROPERTIES配置
 * 
 * @author issac
 * 
 */
public class TextualProperties {
	private Properties p;
	
	public Object pobj(String key){
		return p.getProperty(key);
	}
	
	/**
	 * 把一个STRING TOKEN，分隔字符串，解析为STRING ARRAY
	 * 截至20190107， Java PROPERTIES仍然不支持LIST，必须解析分隔字符串
	 * @param key
	 * @param expr
	 * @return
	 */
	public String[] pstra(String key, String expr){
		String token = p.getProperty(key);
		return token.split(expr);
	}
	
	public Properties parse(String fileName) {
		p = new Properties();
		try {
			FileInputStream pInStream = new FileInputStream(new File(fileName));
			p.load(pInStream);
			DevLog.trace("Printing loaded properties from "+fileName);
			Set kS = p.keySet();
			Iterator it = kS.iterator();
			while(it.hasNext()){
				String key = (String) it.next();
				DevLog.trace(key+"="+p.getProperty(key));
			}
			DevLog.trace("All loaded properties have been printed.");
			return p;
		} catch (FileNotFoundException e) {
			ColorLog.warn("The config file "+fileName+" could not be found.");
			//e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	public void parseForException(String fileName) throws FileNotFoundException, IOException {
		final Properties p = new Properties();
		FileInputStream pInStream = new FileInputStream(new File(fileName));
		p.load(pInStream);
		p.list(System.out);
	}

	/**
	 * 把一个分隔字符串属性解析为数字LIST
	 * @param key
	 * @param expr
	 * @return
	 */
	public List pintl(String key, String expr) {
		String token = p.getProperty(key);
		String[] strA = token.split(expr);
		List intL = new ArrayList();
		for(int i=0; i<strA.length; i++){
			intL.add(Integer.valueOf(strA[i]));
		}
		return intL;
	}
}
