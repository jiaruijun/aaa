package com.overcloud.pay.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	 private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
	    private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

	    private static final char[] zeroArray = "0000000000000000000000000000000000000000000000000000000000000000"
	            .toCharArray();

	    /**
	     * Formats a Date as a fifteen character long String made up of the Date's
	     * padded millisecond value.
	     * 
	     * @return a Date encoded as a String.
	     */
	    public static String dateToMillis(Date date) {
	        return zeroPadString(Long.toString(date.getTime()), 15);
	    }

	    /**
	     * yyyy-MM-dd HH:mm:ss
	     * 
	     * @param date
	     * @return
	     */
	    public static String format(Date date) {
	        if (date == null) {
	            return "";
	        }
	        return sdf.format(date);
	    }

	    /**
	     * yyyyMMdd
	     * 
	     * @param date
	     * @return
	     */
	    public static String formatNumber(Date date) {
	        return sdf2.format(date);
	    }

	    /**
	     * yyyy-MM-dd
	     * 
	     * @param date
	     * @return
	     */
	    public static String formatDate(Date date) {
	        return sdf3.format(date);
	    }

		public static String formatDate(String findTime) {
			return formatDate(parse(findTime));
		}
	    
	    /**
	     * 按照指定时间获取日期
	     * 
	     * @param year
	     * @param month
	     * @param day
	     * @param hour
	     * @return
	     */
	    public static Date getDate(Integer year, Integer month, Integer day, Integer hour) {

	        Calendar calendar = Calendar.getInstance();

	        calendar.set(Calendar.YEAR, year);
	        calendar.set(Calendar.MONTH, month);
	        calendar.set(Calendar.DAY_OF_MONTH, day);
	        calendar.set(Calendar.HOUR_OF_DAY, hour);

	        return calendar.getTime();
	    }

	    /**
	     * 获取当前时间戳
	     * 
	     * @return
	     */
	    public static long getTimestmap() {
	        return Calendar.getInstance().getTimeInMillis();
	    }

	    public static Date parse(String dateStr) {
	        try {
	            return sdf.parse(dateStr);
	        }
	        catch (ParseException e) {
	        }
	        return null;
	    }
	    
		public static int daySub(Date date) {
			if (date != null) {
				long ms = new Date().getTime() - date.getTime();
				int day = (int) (ms / 86400000);
				return day;
			}
			return 0;
		}
		
		public static void main(String[] args) {
			System.out.println(daySub(parse("2015-04-13 22:12:23")));
		}
		
	    /**
	     * Pads the supplied String with 0's to the specified length and returns the
	     * result as a new String. For example, if the initial String is "9999" and
	     * the desired length is 8, the result would be "00009999". This type of
	     * padding is useful for creating numerical values that need to be stored
	     * and sorted as character data. Note: the current implementation of this
	     * method allows for a maximum <tt>length</tt> of 64.
	     * 
	     * @param string
	     *            the original String to pad.
	     * @param length
	     *            the desired length of the new padded String.
	     * @return a new String padded with the required number of 0's.
	     */
	    public static String zeroPadString(String string, int length) {
	        if (string == null || string.length() > length) {
	            return string;
	        }
	        StringBuilder buf = new StringBuilder(length);
	        buf.append(zeroArray, 0, length - string.length()).append(string);
	        return buf.toString();
	    }


}
