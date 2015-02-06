package com.ibm.issac.toolkit.file.byLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.issac.toolkit.DevLog;

/**
 * 解析mqs.ini
 * 
 * @author issac
 *
 */
public class INI_BLP implements ByLineProcesser {
	private List sectionL = new ArrayList();// mqs.ini过滤得到的QMGR数据
	private Map currentDataM = new HashMap(); // 当前一节的数据

	public void process(String text) {
		if (text == null)
			return;
		final String trimmedStr = text.trim();
		// 判断是否新的一个QMGR数据开始
		if (this.isNewSectionBeginning(trimmedStr)) {
			DevLog.debug("Discovered a new section :" + trimmedStr);
			// 把当前数据保存起来
			if (currentDataM != null) {
				// 保存数据
				DevLog.trace("Saving previously-discovered section.");
				if (!currentDataM.isEmpty()) {
					final Map dataM = new HashMap();
					dataM.putAll(currentDataM);
					sectionL.add(dataM);
				}else{
					DevLog.trace("Not adding an empty map.");
				}
			}
			// 开始创建新数据
			currentDataM = new HashMap();
			currentDataM.put("$anaSetting.SECTION_KEYWORD", trimmedStr);
		}
		// 收集数据
		if (trimmedStr.startsWith("#")) {
			DevLog.trace("ignoring text due to starting with # in >" + trimmedStr + "<");
			return;
		}
		int sepIndex = trimmedStr.indexOf('=');
		if (sepIndex < 0) {
			DevLog.trace("ignoring text due to no \'=\' in >" + trimmedStr + "<");
			return;
		}
		final String keyStr = trimmedStr.substring(0, sepIndex);
		final String valStr = trimmedStr.substring(sepIndex + 1, trimmedStr.length());
		DevLog.trace("Adding KEY: " + keyStr + "\tVAL: " + valStr);
		currentDataM.put(keyStr, valStr);
	}

	/**
	 * 判断INI是否新开始了一段
	 * 
	 * @param trimmedStr
	 * @return
	 */
	private boolean isNewSectionBeginning(String trimmedStr) {
		if (trimmedStr.isEmpty())
			return false;
		if (trimmedStr.indexOf('=') >= 0)
			return false;
		if (trimmedStr.startsWith("#"))
			return false;
		return true;
	}

	public Object afterProcessing(Object msg) {
		// 最后一批数据currentDataM需要首先保存好
		if (currentDataM != null) {
			DevLog.trace("Saving lastly discovered data.");
			final Map dataM = new HashMap();
			dataM.putAll(currentDataM);
			sectionL.add(dataM);
			currentDataM = null;
		}
		return sectionL;
	}
}
