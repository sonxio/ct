package com.ibm.issac.toolkit.param.simpleConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.file.lineByLineParser.LineByLineParser;
import com.ibm.issac.toolkit.file.lineByLineParser.LineByLineParserException;
import com.ibm.issac.toolkit.logging.Reporter;
import com.ibm.issac.toolkit.util.StringUtil;

/**
 * <p>
 * 约定一套CONFIG文件格式，用来提供文本参数。 和JAVA PROPERTIES相比，支持LIST。
 * 和-D参数相比，不会自动处理两端的空格，而且减少文件编码问题带来的麻烦
 * </p>
 * 
 * @author song
 *
 */
public class SimpleConfigBLP implements LineByLineParser {
	private ConfigUnit configU = new ConfigUnit();
	private Map<String, List<String>> configM = new HashMap<String, List<String>>();

	public void parse(String text) throws LineByLineParserException {
		// 规则：不可读的行会被忽略
		if (StringUtil.isEmpty(text))
			return;
		// 规则：用###开头的行会被忽略，因此#可以注释
		if (text.startsWith("#__"))
			return;
		// ------------------------
		// 规则：用"@__key "开头表示是KEY，即参数名称。最终的参数名称解读后不包含提示符号
		if (text.startsWith("@__key ")) {
			// 旧的CU已经组装完成，可以保存了
			configM.put(configU.getKey(), configU.getValueL());
			configU.reset();
			// 设置新的CU KEY
			configU.setKey(text);
			return;
		}
		// -----------------------------
		// 规则@__cmd开头是命令，对应处理
		if(text.startsWith("@__cmd ")) {
			final CmdUnit cmdU = this.getCmd(text);
			String value = cmdU.execute(configM);
			configU.addValue(value);
			return;
		}
		//-----------------------------------
		// 规则：KEY之后的所有行都是VALUE。如果有很多则组成列表
		configU.addValue(text);

	}

	/**
	 * 从输入中找到具体COMMAND
	 * @param text
	 * @return
	 */
	private CmdUnit getCmd(String text) {
		final CmdUnit cmdU = new CmdUnit();
		cmdU.parse(text);
		DevLog.super_trace("[SimpleConfigBLP] cmd: "+cmdU);
		return cmdU;
	}

	/**
	 * 必须调用，否则最后一行记录不能保存。
	 */
	public Object finishParsing(Object msg) {
		configM.put(configU.getKey(), configU.getValueL());
		configU.reset();
		DevLog.super_trace("[SimpleConfigBLP] parsing finished.");
		return null;
	}

	/**
	 * 执行PROCESS后，获取处理结果
	 * 
	 * @return
	 */
	public Map<String, List<String>> getResultMap() {
		return this.configM;
	}

	/**
	 * 执行PROCESS后，获取处理结果, 返回只有1个结果的配置项
	 * 
	 * @param key
	 * @return
	 */
	public String r1(String key) {
		return configM.get(key).get(0);
	}

	public List<String> rL(String key) {
		return configM.get(key);
	}

}
