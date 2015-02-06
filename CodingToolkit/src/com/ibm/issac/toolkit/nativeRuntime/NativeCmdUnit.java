package com.ibm.issac.toolkit.nativeRuntime;

/**
 * 包含了适合各个平台的命令
 * 
 * @author issac
 *
 */
public class NativeCmdUnit {
	private String cmd_Windows;
	private String cmd_Linux;
	private String cmd_AIX;
	private String cmd_Other;

	/**
	 * 把所有命令设置为一样的内容
	 */
	public void setCmd_ALL(String cmd){
		cmd_Windows = cmd;
		cmd_Linux = cmd;
		cmd_AIX = cmd;
		cmd_Other = cmd;
	}
	
	public String getCmd_Windows() {
		return cmd_Windows;
	}

	public void setCmd_Windows(String cmd_Windows) {
		this.cmd_Windows = cmd_Windows;
	}

	public String getCmd_Linux() {
		return cmd_Linux;
	}

	public void setCmd_Linux(String cmd_Linux) {
		this.cmd_Linux = cmd_Linux;
	}

	public String getCmd_AIX() {
		return cmd_AIX;
	}

	public void setCmd_AIX(String cmd_AIX) {
		this.cmd_AIX = cmd_AIX;
	}

	public String getCmd_Other() {
		return cmd_Other;
	}

	public void setCmd_Other(String cmd_Other) {
		this.cmd_Other = cmd_Other;
	}
}
