package com.x4096.common.utils.date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: 时间工具类
 */
public class DateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);


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


    private static String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };



    /**
     * 获取北京时区
     *
     * @return
     */
    public static TimeZone getBeijingTimeZone(){
        return TimeZone.getTimeZone("GMT+8:00");
    }


    /**
     * 将日期字符串转换为Date对象
     *
     * @param date      必须为 yyyy-MM-dd HH:mm:ss 格式
     * @return
     */
    public static Date parseStrToDate(String date){
        return parseStrToDate(date, DATE_FORMAT);
    }

    /**
     * 将日期字符串转换为Date对象
     *
     * @param date      日期字符串，必须与 format 对应格式
     * @param format    格式化字符串
     * @return          日期字符串的Date对象表达形式
     * */
    public static Date parseStrToDate(String date, String format){
        Date dt = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try{
            dt = dateFormat.parse(date);
        }catch(ParseException e){
            LOGGER.error("时间转换异常", e);
        }
        return dt;
    }

    /**
     * 将Date对象转换为指定格式的字符串
     *
     * @param date      Date对象
     * @param format    格式化字符串
     * @return          Date对象的字符串表达形式
     * */
    public static String formatDateToStr(Date date, String format){
        if(date == null || StringUtils.isBlank(format)){
            throw new IllegalArgumentException("参数非法, date 和 format 不能为空");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }


    /**
     * 将Date对象转换为指定格式的字符串
     *
     * @param date                  Date对象
     * @param dateFormatEnums       格式化字符串枚举类,封装了常用的格式
     * @return                      Date对象的字符串表达形式
     */
    public static String formatDateToStr(Date date, DateFormatEnums dateFormatEnums){
        if(date == null || dateFormatEnums == null){
            throw new IllegalArgumentException("参数非法, date 和 dateFormatEnums 不能为空");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatEnums.getFormat());
        return dateFormat.format(date);
    }



    /**
     * 根据当前时间 获取过去或未来多少天的时间
     *
     * @param days      过去或未来几天 正数为将来时间,负数为过去时间
     * @return          过去或者未来几天的当前时间
     */
    public static Date getPastOrFetureDateByNow(int days) {
        return getPastOrFetureDateByConditions(new Date(), DateEnums.DAY, days);
    }




    /**
     * 获取指定时间在过去或将来的时间
     *
     * @param date          指定时间
     * @param dateEnums     指定增加的单位 exp: 秒,分钟,小时
     * @param days          指定天数 若负数则为过去时间 正数则为将来时间
     * @return
     */
    public static Date getPastOrFetureDateByConditions(Date date, DateEnums dateEnums, int days) {
        if(date == null || dateEnums == null){
            throw new IllegalArgumentException("参数非法, date 和 timeUnit 不能为 null");
        }

        Calendar beforeTime = Calendar.getInstance();
        beforeTime.setTime(date);

        beforeTime.add(dateEnums.getCode(), days);
        return beforeTime.getTime();
    }




    /**
     * 判断当天是否为周末
     *
     * @return
     */
    public static boolean isWeekendToDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return w == 0 || w == 6;
    }

    /**
     * 今天星期几
     *
     * @return      今天是星期几
     */
    public static String getWeekDayToDay(){
        return getWeekDay(new Date());
    }



    /**
     * 获取指定时间是星期几
     *
     * @param date      指定时间
     * @return          定时间是星期几
     */
    public static String getWeekDay(Date date) {
        if(date == null){
            throw new IllegalArgumentException("参数非法, date 不能为 null");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return weekDays[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }




    /**
     * 时间戳转 Date
     *
     * @param timeStamp
     * @return
     */
    public static Date timeStampToDate(long timeStamp){
        if(timeStamp < 0){
            throw new IllegalArgumentException("参数非法, timeStamp 不能小于 0");
        }
        SimpleDateFormat simpleDateFormat = SIMPLE_DATE_FORMAT_THREAD_POOL.get();

        try {
            return simpleDateFormat.parse(simpleDateFormat.format(timeStamp));
        } catch (ParseException e) {
            LOGGER.error("时间转换异常", e);
        }
        return null;
    }


    /**
     * Date 转时间戳
     *
     * @param date
     * @return
     */
    public static long dateToTimeStamp(Date date){
        if(date == null){
            throw new IllegalArgumentException("参数非法, date 不能为 null");
        }
        return date.getTime();
    }


}
