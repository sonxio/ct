package com.ibm.issac.toolkit.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 生成格式恰当的日期
 * 
 * @author issac
 * 
 */
public class DateUtil {
	/**
	 * 得到yyyy/MM/dd HH:mm格式的当前时间
	 * 
	 * @return
	 */
	public static String getDatetimeNow() {
		return DateUtil.formatDate("yyyy/MM/dd HH:mm:ss:SSS", new Date());
	}

	public static String getNow(String fmt) {
		return DateUtil.formatDate(fmt, new Date());
	}

	public static String formatDateByRoutine(Date date) {
		return DateUtil.formatDate("yyyy/MM/dd HH:mm:ss", date);
	}

	/**
	 * 格式化时间
	 * 
	 * @param format
	 * @param date
	 * @return
	 */
	public static String formatDate(String format, Date date) {
		final SimpleDateFormat fmt = new SimpleDateFormat(format);
		return fmt.format(date);
	}

	public static String formatGregorianCalendar(GregorianCalendar c) {
		Date date = c.getTime();
		return DateUtil.formatDate("yyyy/MM/dd HH:mm:ss", date);
	}

	public static double computeSecondGap(Date dt1, Date dt2) {
		final long msGap = dt1.getTime() - dt2.getTime();
		return msGap / (1000.0);
	}

	/**
	 * 计算两个DATE类型的时间差(dt1-dt2)，用HOUR表示
	 * 
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public static double computeHourGap(Date dt1, Date dt2) {
		final long msGap = dt1.getTime() - dt2.getTime();
		return msGap / (1000.0 * 60.0 * 60.0);
	}

	/**
	 * 计算两个DATE类型的时间差(dt1-dt2)，用DAY表示 注意这是准确的时间差，例如第一天下午5点到第二天下午5点前都是计算为1天内。 如果希望隔天就表示一天，使用相差12小时会更接近。
	 * 
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public static double computeDayGap(Date dt1, Date dt2) {
		final long msGap = dt1.getTime() - dt2.getTime();
		return msGap / (1000.0 * 60.0 * 60.0 * 24.0);
	}

	/**
	 * 字符串转换为日期
	 * 
	 * @param dateStr
	 * @param formatStr
	 * @return
	 */
	public static Date toDate(String dateStr, String formatStr) {
		try {
			return DateUtil.toDateForException(dateStr, formatStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date toDateForException(String dateStr, String formatStr) throws ParseException {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		date = dd.parse(dateStr);
		return date;
	}

}
