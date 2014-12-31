package com.ibm.issac.toolkit.timing;

import java.util.Date;

import com.ibm.issac.toolkit.util.DateUtil;

/**
 * 计算起始时间
 * 
 * @author issac
 * 
 */
public class TimerDTO {
	private Date startDate;
	private Date stopDate;

	public TimerDTO() {
	}

	public TimerDTO(boolean startAutomatically) {
		if (startAutomatically) {
			this.startTimer();
		}
	}

	public void startTimer() {
		this.startDate = new Date();
	}

	public void stopTimer() {
		this.stopDate = new Date();
	}

	/**
	 * 在stopTimer后得到毫秒为单位的时间差
	 * 
	 * @return
	 */
	public long getInterval() {
		return (stopDate.getTime() - startDate.getTime());
	}

	/**
	 * 不停止计时，报告当前已经经过的时间
	 * 
	 * @return
	 */
	public String reportCurrentInterval() {
		final StringBuffer sb = new StringBuffer();
		sb.append("TimerDTO{");
		sb.append("startDate : ").append(DateUtil.formatDate("yyyy-MM-dd HH:mm:ss.SSS", this.startDate)).append(',');
		sb.append("elapsedTime : ").append(this.getCurrentInterval() / 1000.0).append("s");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 报告当前已经经过的时间，然后重新开始计时
	 * @return
	 */
	public String reportAndReset(String title){
		final StringBuffer sb = new StringBuffer();
		sb.append("TIMER{");
		sb.append(title).append(':');
		//sb.append("startDate : ").append(DateUtil.formatDate("yyyy-MM-dd HH:mm:ss.SSS", this.startDate)).append(',');
		sb.append(this.getCurrentInterval() / 1000.0).append("s");
		sb.append("}");
		this.startTimer();
		return sb.toString();
	}

	/**
	 * 不停止计时，给出启动至今的时差
	 * @return
	 */
	public long getCurrentInterval() {
		return (new Date().getTime() - startDate.getTime());
	}
}
