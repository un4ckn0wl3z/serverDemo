package com.haoyun.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期操作工具集
 */
public abstract class DateUtil {

	/** 格式:yyyy-MM-dd HH:mm:ss */
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/** 格式:yyyyMMdd. */
	public static final String DATE_PATTERN_MIN = "yyyyMMdd";
	
	/**
	 * 系统时间获取 yyyyMMddHHmmss
	 *
	 * @return 系统时间
	 */
	public static String getStringDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date systemDate = new Date();
		return dateFormat.format(systemDate);
	}


	
	/**
	 * 系统时间获取 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 系统时间
	 */
	public static String getSystemDate() {

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
		Date systemDate = new Date();
		return dateFormat.format(systemDate);
	}
	
	/**
	 * 年月获取 yyyy-MM
	 * 
	 * @return 系统年月
	 */
	public static String getSystemDateYM() {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date systemDate = new Date();
		return dateFormat.format(systemDate);
	}
	
	/**
	 * 获取指定格式时间
	 * 
	 * @return 系统时间
	 */
	public static String getFormatDate(String format) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date systemDate = new Date();
		return dateFormat.format(systemDate);
	}

	/**
	 * 获取数月之后的日期
	 * 
	 * @param num
	 *            月数
	 * @return
	 */
	public static String getNextMountDate(int num) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
		Date systemDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(systemDate);
		c.add(Calendar.MONTH, num);
		return dateFormat.format(c.getTime());
	}

	/**
	 * 年月取得 yyyy年MM月
	 * 
	 * @return 月
	 */
	public static String getYearMonthSys() {
		String date = getSystemDate();
		return StringUtil.concat(date.substring(0, 4), "年",
				date.substring(4, 6), "月", date.substring(6, 8), "日");
	}

	/**
	 * 年月取得 yyyy年MM月
	 * 
	 * @return 月
	 */
	public static String getYearSys() {

		String date = getSystemDate();
		return StringUtil.concat(date.substring(0, 4), "年",
				date.substring(4, 6), "月");
	}

	/**
	 * 系统日期的日获取
	 * 
	 * @return 日
	 */
	public static String getDayOfSys() {
		String date = getSystemDate();
		return date.substring(6, 8);
	}

	/**
	 * 系统日期的月份获取
	 * 
	 * @return 月
	 */
	public static String getMonthOfSys() {
		String date = getSystemDate();
		return date.substring(4, 6);
	}

	/**
	 * 系统日期的年份获取
	 * 
	 * @return 年
	 */
	public static String getYearOfSys() {
		String date = getSystemDate();
		return date.substring(0, 4);
	}

	/**
	 * 文字日期(yyyyMMddHHmmss)->Date日期转换
	 * 
	 * @param sDate
	 *            文字日期(yyyy-MM-dd HH:mm:ss)
	 * @return 日期
	 */
	public static Date toDate(final String sDate) {

		SimpleDateFormat sm = new SimpleDateFormat(DATE_PATTERN,
				Locale.getDefault());

		try {
			return sm.parse(sDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Date日期 ->文字列日期(yyyyMMddHHmmss)转换
	 * 
	 * @param date
	 *            Date日期
	 * @return 文字列日期(yyyyMMddHHmmss)
	 */
	public static String toString(final Date date) {

		if (date == null) {
			return "";
		}
		SimpleDateFormat sm = new SimpleDateFormat(DATE_PATTERN,
				Locale.getDefault());
		return sm.format(date);
	}

	/**
	 * 年月取得 yyyy年MM月
	 * 
	 * @param date
	 * @return 月
	 */
	public static String getYearMonthDay(String date) {

		if (date == null) {
			return "";
		}

		return StringUtil.concat(date.substring(0, 4), "年",
				date.substring(4, 6), "月", date.substring(6, 8), "日");
	}

	/**
	 * 年月取得 yyyy年MM月
	 * 
	 * @param date
	 * @return 月
	 */
	public static String getYearMonth(String date) {

		if (date == null) {
			return "";
		}

		return StringUtil.concat(date.substring(0, 4), "年",
				date.substring(4, 6), "月");
	}

	/**
	 * 指定日期的日获取
	 * 
	 * @param date
	 *            日期
	 * @return 日
	 */
	public static String getDayOfDate(String date) {
		return date.substring(6, 8);
	}

	/**
	 * 指定日期的月份获取
	 * 
	 * @param date
	 *            日期
	 * @return 月
	 */
	public static String getMonthOfDate(String date) {
		return date.substring(4, 6);
	}

	/**
	 * 指定日期的年份获取
	 * 
	 * @param date
	 *            日期
	 * @return 年
	 */
	public static String getYearOfDate(String date) {
		return date.substring(0, 4);
	}

	/**
	 * 日期字符格式化
	 * 
	 * @param date
	 * @return 日期 yyyy年MM月dd日HH:mm
	 */
	public static String format(String date) {
		return StringUtil.concat(date.substring(0, 4), "年",
				date.substring(4, 6), "月", date.substring(6, 8), "日",
				date.substring(8, 10), ":", date.substring(10, 12));
	}

	/**
	 * 检查是否过期
	 * 
	 * @param outTime
	 *            过期时间
	 * @return false:没有过期，true 过期
	 */
	public static boolean checkOutTime(String outTime) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN_MIN);
		Date systemDate = new Date();
		try {
			if (outTime == null) {
				return false; //
			}
			return dateFormat.parse(outTime.substring(0, 8)).before(systemDate)
					&& !dateFormat.parse(outTime.substring(0, 8)).equals(
							dateFormat.parse(dateFormat.format(systemDate)));
		} catch (ParseException e) {
			e.printStackTrace();
			return true;
		}
	}

	public static String date2TimeStamp(String date_str,String format){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(date_str).getTime()/1000);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}

	public static String dayAfter(String date_str, String format, Integer after) {
		Calendar c = Calendar.getInstance();
		Date date;
		try {
			date = new SimpleDateFormat(format).parse(date_str);
			c.setTime(date);
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day+after);
			String dayAfter=new SimpleDateFormat(format).format(c.getTime());
			return dayAfter;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

}
