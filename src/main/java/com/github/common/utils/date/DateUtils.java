package com.github.common.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions:
 */
public class DateUtils {


    /** 格式化日期的标准字符串 */
    public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 使用ThreadLocal定义一个全局的SimpleDateFormat
     * 因为 SimpleDateFormat 不是线程安全
     */
    private static ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_POOL = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMAT);
        }
    };



    /**
     * 获取北京时区
     * @return
     */
    public static TimeZone getBeijingTimeZone(){
        return TimeZone.getTimeZone("GMT+8:00");
    }


    /**
     * 将日期字符串转换为Date对象
     * @param date 日期字符串，必须为"yyyy-MM-dd HH:mm:ss"
     * @param format 格式化字符串
     * @return 日期字符串的Date对象表达形式
     * */
    public static Date parseToDate(String date, String format){
        Date dt = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try{
            dt = dateFormat.parse(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 将date----->String
     * 将Date对象转换为指定格式的字符串
     * @param date Date对象
     * @param format 格式化字符串
     * @return Date对象的字符串表达形式
     * */
    public static String formatDateToStr(Date date, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }


    /**
     * 根据当前时间 获取未来多少天的时间
     * @param feture
     * @return
     */
    public static Date getFetureDate(int feture) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + feture);
        Date fetureDate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String result = format.format(fetureDate);
        try {
            fetureDate =  format.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fetureDate;
    }

    /**
     * 根据当前时间获取过去几天的日期
     * @param past
     * @return
     */
    public static Date getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date pastDate = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String result = format.format(pastDate);
        try {
            pastDate =  format.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pastDate;
    }

    /**
     * 判断当天是否为周末
     * @return
     */
    public static boolean isWorkDayToDay(){
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return w==0||w==6;
    }



    /**
     * 获取星期几
     * @param date
     * @return
     */
    public static int getWeekNumber(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(7);
    }


    /**
     * 比较日期
     * @param standDate
     * @param desDate
     * @return
     */
    public static boolean dateIsBefore(String standDate, String desDate) {
        try {
            return SIMPLE_DATE_FORMAT_THREAD_POOL.get().parse(desDate).before(SIMPLE_DATE_FORMAT_THREAD_POOL.get().parse(standDate));
        } catch (ParseException var3) {
            var3.printStackTrace();
            return false;
        }
    }


    /**
     * 将年月日时分秒转成Long类型
     * @param dateTime
     * @return
     */
    public static Long dateTimeToTimeStamp(String dateTime) {
        try {
            Date e = SIMPLE_DATE_FORMAT_THREAD_POOL.get().parse(dateTime);
            return Long.valueOf(e.getTime() / 1000L);
        } catch (ParseException var2) {
            var2.printStackTrace();
            return Long.valueOf(0L);
        }
    }

    /**
     * 相差多少分钟
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long minutesBetweenTwoDate(String beginDate, String endDate) {
        long millisBegin = dateTimeToTimeStamp(beginDate).longValue();
        long millisEnd = dateTimeToTimeStamp(endDate).longValue();
        return (millisEnd - millisBegin) / 60L;
    }








}
