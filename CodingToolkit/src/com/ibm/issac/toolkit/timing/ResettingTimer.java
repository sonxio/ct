package com.ibm.issac.toolkit.timing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ibm.issac.toolkit.DevLog;
import com.ibm.issac.toolkit.param.SysProp;

/**
 * 连续记录多个时间点，最后可以一起生成报告的计时器<br/>
 * 该timer每次记录时长后自动重新开始计时<br/>
 * 测试中确认这个类导致CPU占用过高，因此增加了控制是否启用的开关，在压力测试当中，或实际运行CPU资源不足时，应关闭使用这个类。
 * 
 * @author issac
 * 
 */
public class ResettingTimer extends TimerDTO {
	private List tL = new ArrayList();
	private String title="";

	public ResettingTimer(Object obj, boolean autoStart) {
		super(autoStart);
		//避免反射机制造成CPU消耗过大
		if (!SysProp.b_bool("issac.debugMode.timer", true)) {
			return;
		}
		this.title = obj.getClass().getSimpleName();
		DevLog.trace("[TIMER-" + title + "] Starting");
	}

	/**
	 * 类似长跑跑了一圈，记录一个时长
	 */
	public void lapAndReset() {
		if (!SysProp.b_bool("issac.debugMode.timer", true)) {
			return;
		}
		long t = this.getCurrentInterval();
		tL.add(new Long(t));
		DevLog.trace("[TIMER-" + title + "] lap No. " + tL.size());
		this.startTimer();
	}

	public String reportLapTimes() {
		if (!SysProp.b_bool("issac.debugMode.timer", true)) {
			return "";
		}
		return this.reportLapTimes("");
	}

	/**
	 * 生成一个目前为止所有时长记录
	 * 
	 * @return
	 */
	public String reportLapTimes(String title) {
		if (!SysProp.b_bool("issac.debugMode.timer", true)) {
			return "";
		}
		// 获取警告阀值
		final int threshold = SysProp.d_int("issac.timerThreshold", 1);
		// 生成字符串
		final StringBuffer sb = new StringBuffer(), sb1 = new StringBuffer(" = ");
		sb.append("[TIMER-").append(title).append("]");
		sb.append(title).append(':');
		// 计算总时长
		long total = 0;
		final Iterator it = tL.iterator();
		while (it.hasNext()) {
			Long t = (Long) it.next();
			total += t.longValue();
			// 如果超过阀值，则统计详细时间组成情况
			sb1.append(t.longValue() / 1000.0);
			if (it.hasNext()) {
				sb1.append(" + ");
			}
		}
		if (total > threshold) {
			sb.append(total / 1000.0).append('s');
			sb.append(sb1);
		}
		return sb.toString();
	}
}
