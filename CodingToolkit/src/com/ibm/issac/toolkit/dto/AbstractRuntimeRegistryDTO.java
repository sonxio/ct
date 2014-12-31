package com.ibm.issac.toolkit.dto;

import org.apache.log4j.PropertyConfigurator;

import com.ibm.issac.toolkit.logging.ColorLog;
import com.ibm.issac.toolkit.validation.StringValidation;

/**
 * 加载配置文件，保存各种运行时数据
 * 
 * 建议所有运行时数据使用public变量保存在本DTO中。
 * 
 * @author issac
 * 
 */
public abstract class AbstractRuntimeRegistryDTO {
	/**
	 * 加载配置文件，把配置信息读取进来
	 * 
	 * @param fileName
	 */
	public abstract void loadProperties(String fileName);

}
