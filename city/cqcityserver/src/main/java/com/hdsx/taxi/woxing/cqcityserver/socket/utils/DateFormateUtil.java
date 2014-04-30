/**
 * Project Name:main
 * File Name:DateFormateUtil.java
 * Package Name:com.hdsx.taxi.driver.cq.util
 * Date:2014年3月21日下午2:25:21
 * Copyright (c) 2014, Kevinyarm All Rights Reserved.
 * Blog http://www.cnblogs.com/Kevinyarm/
 *
 */

package com.hdsx.taxi.woxing.cqcityserver.socket.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ClassName:DateFormateUtil
 * 
 * Function:
 * 
 * Reason:
 * 
 * Date: 2014年3月21日 下午2:25:21
 * 
 * @author Kevin
 * @see
 */
public class DateFormateUtil {
	public static void main(String[] args) {
		Date d = new Date();
		Date result = DateFormateUtil.getDateAddHours(d, 24);
		System.out.println(result);
	}
	
	
	/**
	 * 
	 * getDateAddMinutes:(获取指定时间之后多少分钟的时间). 
	 *
	 * @author sid
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date getDateAddMinutes(Date date,int minutes){
		
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.add(Calendar.MINUTE	, minutes);
		
		return cl.getTime();
	}
	/**
	 * 
	 * getDateAddHours:(获取指定时间之后多少小时的时间). 
	 *
	 * @author sid
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date getDateAddHours(Date date,int hours){
		return DateFormateUtil.getDateAddMinutes(date, 60*hours);
	}
	/**
	 * 
	 * getDateAddDays:(获取指定时间之后多少小时的时间). 
	 *
	 * @author sid
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date getDateAddDays(Date date,int days){
		return DateFormateUtil.getDateAddMinutes(date, 24*60*days);
	}
	
	/**
	 * 
	 * getNowTime:获取当前时间.
	 * 
	 * 返回值为String格式的时间 yyyy-mm-dd hh:mm:ss
	 * 
	 * @author Kevin
	 * @return String (yyyy-mm-dd hh:mm:ss)
	 */
	public static String getNowTime() {
		return date2MySQLDateTimeString(now);
	}

	/**
	 * 
	 * date2MySQLDateTimeString:Java.util.Date转换MySQLDateTime类型方法.
	 * 
	 * TODO 将Java.util.Date类型转换为MySQLDateTime得字符串类型.
	 * 
	 * @param Date
	 *            date 待转换的java.util.date类型date.
	 * @return String 转换得出的MySQLDateTime类型得字符串格式为YYYY-MM-DD HH:MM:SS
	 * @author Kevin
	 */
	public static String date2MySQLDateTimeString(Date date) {
		final String[] MONTH = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec", };
		StringBuffer ret = new StringBuffer();
		String dateToString = date.toString();
		/**
		 * get Year
		 */
		ret.append(dateToString.substring(24, 24 + 4));
		String sMonth = dateToString.substring(4, 4 + 3);
		/**
		 * get Month
		 * 
		 * between Year and Month
		 * 
		 * if Month(1-9) add "0"
		 */
		for (int i = 0; i < 12; i++) { // append mm
			if (sMonth.equalsIgnoreCase(MONTH[i])) {
				if ((i + 1) < 10)
					ret.append("-0");
				else
					ret.append("-");
				ret.append((i + 1));
				break;
			}
		}
		/**
		 * between Month and Day
		 */
		ret.append("-");
		/**
		 * get Day
		 */
		ret.append(dateToString.substring(8, 8 + 2));
		/**
		 * between Day and time
		 */
		ret.append(" ");
		/**
		 * get Time
		 */
		ret.append(dateToString.substring(11, 11 + 8));

		return ret.toString();
	}

	public static Date dateString2JavaUtilDate(String datestr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			if (datestr.length() == 19) {
				date = sdf.parse(datestr);
			}
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return date;
	}

	/*
	 * 2013-07-26 14:31:14
	 */
	public static String getMonth(String dateString) {
		String[] str = dateString.split("-");
		/* get month */
		String month = str[1];
		if (month.startsWith("0")) {
			month.replace("0", "");
		}
		return month;
	}

	/*
	 * 2013-07-26 14:31:14
	 */
	public static String getDay(String dateString) {
		String[] str = dateString.trim().split(" ");
		/* get day */
		String YMDString = str[0];
		String[] YMDstr = YMDString.split("-");
		String day = YMDstr[2];
		if (day.startsWith("0")) {
			day.replace("0", "");
		}
		return day;
	}

	public static String getTime(String dateString) {
		String[] str = dateString.split(" ");
		String timeString = str[1];
		String[] timeStr = timeString.trim().split(":");
		/* get day */
		String h = timeStr[0];
		String m = timeStr[1];
		String s = timeStr[2];
		if (h.startsWith("0")) {
			h.replace("0", "");
		}
		if (m.startsWith("0")) {
			m.replace("0", "");
		}
		if (s.startsWith("0")) {
			s.replace("0", "");
		}
		// String time = h + "点" + m + "分"+s+"秒";
		String time = h + "点" + m + "分";
		return time;
	}

	/* Hour */
	public static String getHour(String dateString) {
		String[] str = dateString.split(" ");
		String timeString = str[1];
		String[] timeStr = timeString.trim().split(":");
		/* get day */
		String h = timeStr[0];
		if (h.startsWith("0")) {
			h.replace("0", "");
		}

		String time = h + "时";
		return time;
	}

	/* Minute */
	public static String getMinute(String dateString) {
		String[] str = dateString.split(" ");
		String timeString = str[1];
		String[] timeStr = timeString.trim().split(":");
		/* get day */
		String m = timeStr[1];
		// String s = timeStr[2];
		if (m.startsWith("0")) {
			m.replace("0", "");
		}
		// String time = h + "点" + m + "分"+s+"秒";
		String time = m + "分";
		return time;
	}

	static Date now = new Date();

	public DateFormateUtil() {
	}

}
