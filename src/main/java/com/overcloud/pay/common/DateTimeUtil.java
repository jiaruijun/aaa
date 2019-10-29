package com.overcloud.pay.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式化工具
 * 
 */
public class DateTimeUtil {

    public final static String YEARMONTH_PATTERN = "yyMM";
    public final static String DATE_SIMPLEPATTERN = "yyyyMMdd";
    public final static String TIME_SIMPLEPATTERN = "HHmmss";
    public final static String TIME_STANDARDPATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String TIME_STANDARDPATTERN_ = "yyyy/MM/dd HH:mm:ss";

    /**
     * 格式化当前日期或时间
     * 
     * @param pattern
     * @return
     */
    public static String formatDateOrTime(String pattern) {
        SimpleDateFormat formatObj = new SimpleDateFormat(pattern);
        String date = formatObj.format(new java.util.Date());
        return date;
    }

    /**
     * 获取下个月日期
     * 
     * @param pattern
     * @return
     */
    public static String getNextMonth(String pattern) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        SimpleDateFormat formatObj = new SimpleDateFormat(pattern);
        String date = formatObj.format(c.getTime());
        return date;
    }

    /**
     * 将日期字符串转换成日期类型
     * 
     * @param pattern
     * @param dateTimeStr
     * @return
     * @throws ParseException
     */
    public static Date parseDateOrTime(String pattern, String dateTimeStr) throws ParseException {
        SimpleDateFormat formatObj = new SimpleDateFormat(pattern);
        Date date = formatObj.parse(dateTimeStr);
        return date;
    }

}
