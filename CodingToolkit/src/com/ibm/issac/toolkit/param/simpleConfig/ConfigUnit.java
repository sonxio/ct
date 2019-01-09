package com.ibm.issac.toolkit.param.simpleConfig;

import java.util.ArrayList;
import java.util.List;

import com.ibm.issac.toolkit.DevLog;

/**
 * 对应解析出的一对KEY/VALUE
 * 
 * @author song
 *
 */
public class ConfigUnit {
	private String key;
	private List<String> valueL;
	private String _status;

	public ConfigUnit() {
		DevLog.super_trace("[SimpleConfig] a config unit is created, and will be reset.");
		this.reset();
	}

	public void reset() {
		this._status = "RESET";
		this.key=null;
		this.valueL=new ArrayList<String>();
		DevLog.super_trace("[SimpleConfig] a config unit is reset.");
	}

	public String getKey() {
		return key;
	}

	public List<String> getValueL() {
		return valueL;
	}

	/**
	 * 来源TEXT包含"@__key "开头，必须去掉。注意@后是两个下划线
	 * <p/>
	 * 设置KEY意味着此前处于处理结束状态，将RESET整个CONFIG UNIT。
	 * 
	 * @param key
	 * @throws SimpleConfigException
	 */
	public void setKey(String key) throws SimpleConfigException {
		// 检查状态
		if (!("-NEWLY CREATED-RESET-".indexOf(_status) >= 0)) {
			throw new SimpleConfigException("[SimpleConfig] the config unit status is wrong when trying to set a key. The current status is "+_status+". Usually this indicates two consecutive keys are set, without any value in between.  ");
		}
		// 设置KEY
		final String HEADER = "@__key ";
		final int HEADER_LENGTH = HEADER.length();
		if (key.length() <= HEADER_LENGTH || !key.startsWith(HEADER))
			throw new SimpleConfigException("[SimpleConfig] Illegal key: " + key);

		this.key = key.substring(HEADER_LENGTH, key.length());
		DevLog.super_trace("[SimpleConfig] a key is set to " + this.key);
		this._status = "KEY SET";
	}

	public void addValue(String value) throws SimpleConfigException {
		// 检查状态
		if (!(("-KEY SET-VALUE ADDED-".indexOf("-" + _status + "-")) >= 0)) {
			throw new SimpleConfigException("[SimpleConfig] the config unit status is wrong while trying to set a value. currently the status is " + _status);
		}
		this.valueL.add(value);
		DevLog.super_trace("[SimpleConfig] the value >"+value+"< is added to the key "+this.key );
		this._status = "VALUE ADDED";
	}

}
