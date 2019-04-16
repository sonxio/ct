package com.ibm.issac.toolkit.param.simpleConfig;

import java.util.List;
import java.util.Map;

import com.ibm.issac.toolkit.DevLog;

/**
 * 一些配置行可以包含指令，这是个指令单元
 * 
 * @author song
 *
 */
public class CmdUnit {
	private String cmdWithParam;
	private String cmd;
	private String param;

	@Override
	public String toString() {
		return "CmdUnit [cmdWithParam=" + cmdWithParam + ", cmd=" + cmd + ", param=" + param + "]";
	}

	public String getCmdWithParam() {
		return cmdWithParam;
	}

	public void setCmdWithParam(String cmdWithParam) {
		this.cmdWithParam = cmdWithParam;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public void parse(String text) {
		final String HEADER = "@__cmd ";
		String cmdWithParam = text.substring(HEADER.length(), text.length());
		this.setCmdWithParam(cmdWithParam);
		//从CMD开始到第一个空格为具体COMMAND，第一个空格后为参数
		String cmd = cmdWithParam.substring(0, cmdWithParam.indexOf(" "));
		this.setCmd(cmd);
		this.setParam(cmdWithParam.substring(cmdWithParam.indexOf(" ")+1, cmdWithParam.length()));
	}

	/**
	 * 执行命令
	 * @param configM 目前已经得到的配置数据
	 * @return 命令根据一定规则得到新的VALUE
	 * @throws SimpleConfigException 
	 */
	public String execute(Map<String, List<String>> configM) throws SimpleConfigException {
		if("copyValueFromAnAboveKey".equals(cmd)) {
			DevLog.super_trace("fetching values from the key "+param);
			return configM.get(param).get(0);
		}
		throw new SimpleConfigException("[SimpleConfig] unsupported config command: "+cmd);
	}

}
