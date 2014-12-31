package com.ibm.issac.toolkit.app;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.ibm.issac.toolkit.logging.ColorLog;
import com.ibm.issac.toolkit.param.TextualProperties;

/**
 * Load application properties from a textual .properties file.
 * 
 * @author issac
 * 
 */
public class TextualAppProperties {
	private Properties p;

	/**
	 * Load the config file.
	 * 
	 * @param fileName
	 *            Name of the .properties file
	 */
	public void initialize(String fileName) {
		final TextualProperties xp = new TextualProperties();
		this.p = xp.parse(fileName);
		ColorLog.debug(p.toString());
	}

	public int getProperty_int(String propertyName) {
		try {
			int intValue = Integer.valueOf(p.getProperty(propertyName)).intValue();
			return intValue;
		} catch (NumberFormatException e) {
			ColorLog.warn("Value for " + propertyName + " cannot be parsed as a number. It will be ignored and set to -1.");
			e.printStackTrace();
			return -1;
		}
	}

	public String getProperty_String(String propertyName) {
		return p.getProperty(propertyName);
	}

	public boolean getProperty_boolean(String propertyName) {
		return Boolean.valueOf(p.getProperty(propertyName)).booleanValue();
	}

	public URL getProperty_URL(String propertyName) {
		try {
			final URL url = new URL(p.getProperty(propertyName));
			return url;
		} catch (MalformedURLException e) {
			ColorLog.warn("The property value of " + propertyName + " cannot be correctly identified as URL. Thus this property will be marked as null.");
			return null;
		}
	}

	/**
	 * 属性配置中可能包含中文时使用该选项。
	 * 
	 * @param propertyName
	 * @param defaultValue
	 *            如果中文解析失败，就是用默认值
	 * @return
	 */
	public String getProperty_ChineseString(String propertyName, String defaultValue) {
		try {
			return new String(this.getProperty_String(propertyName).getBytes("ISO8859_1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			ColorLog.warn("The property value of " + propertyName + " cannot be correctly parsed as ISO8859_1 encoding. Value of this property will be taken as your default value: " + defaultValue);
			e.printStackTrace();
			return defaultValue;
		} catch (NullPointerException e) {
			ColorLog.warn("The property value of " + propertyName + " cannot be correctly parsed as it might not exist in your config file. The default value will be used: " + defaultValue);
			e.printStackTrace();
			return defaultValue;
		}
	}
}
