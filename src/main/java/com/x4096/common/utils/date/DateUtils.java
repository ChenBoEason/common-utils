package com.x4096.common.utils.date;

import org.apache.commons.lang3.Validate;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: 时间工具类
 */
public class DateUtils {

    private DateUtils() {
    }

    /**
     * 毫秒
     */
    public static final long MILLIS_PER_SECOND = 1000;

    /**
     * 分钟
     */
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;

    /**
     * 小时
     */
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

    /**
     * 天
     */
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    /**
     * 星期
     */
    public static final String[] WEEKDAYS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 月天数
     */
    private static final int[] MONTH_LENGTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


    /**
     * 获取北京时区
     */
    public static TimeZone getBeijingTimeZone() {
        return TimeZone.getTimeZone("GMT+8");
    }


    /**
     * 获取指定时间是星期几
     *
     * @param date 指定时间
     * @return 定时间是星期几
     */
    public static String getWeekDay(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("参数非法, date 不能为 null");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return WEEKDAYS[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }


    /**
     * 获取当天星期几
     *
     * @return
     */
    public static String getWeekDayToDay() {
        return getWeekDay(new Date());
    }


    /**
     * 判断当天是否为周末
     *
     * @return
     */
    public static boolean isWeekendToDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return w == 0 || w == 6;
    }


    /**
     * 是否同一天
     *
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        return DateUtils.isSameDay(date1, date2);
    }


    /**
     * 是否同一时刻
     *
     * @return
     */
    public static boolean isSameTime(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }


    /**
     * 判断日期是否在范围内，包含相等的日期
     *
     * @param date  需要判断的日期
     * @param start 判断范围起始时间
     * @param end   判断范围结束时间
     * @return
     */
    public static boolean isBetween(Date date, Date start, Date end) {
        if (date == null || start == null || end == null || start.after(end)) {
            throw new IllegalArgumentException("some date parameters is null or dateBein after dateEnd");
        }
        return !date.before(start) && !date.after(end);
    }


    /**
     * 获取指定时间 加减 月、天、周、秒等
     *
     * @param date     需要指定的时间
     * @param dateEnum 操作类型,月、天等
     * @param amount   操作数,如果时间回到过去则填写负数
     * @return
     */
    public static Date conditions(Date date, DateEnum dateEnum, int amount) {
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.setTime(date);
        beforeTime.add(dateEnum.getCode(), amount);
        return beforeTime.getTime();
    }


    /**
     * 加一月
     */
    public static Date addMonths(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMonths(date, amount);
    }


    /**
     * 减一月
     */
    public static Date subMonths(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMonths(date, -amount);
    }


    /**
     * 加一周
     */
    public static Date addWeeks(final Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addWeeks(date, amount);
    }


    /**
     * 减一周
     */
    public static Date subWeeks(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addWeeks(date, -amount);
    }


    /**
     * 加一天
     */
    public static Date addDays(Date date, final int amount) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, amount);
    }


    /**
     * 减一天
     */
    public static Date subDays(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, -amount);
    }


    /**
     * 加一小时
     */
    public static Date addHours(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addHours(date, amount);
    }

    /**
     * 减一小时
     */
    public static Date subHours(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addHours(date, -amount);
    }


    /**
     * 加一分钟
     */
    public static Date addMinutes(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMinutes(date, amount);
    }


    /**
     * 减一分钟
     */
    public static Date subMinutes(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMinutes(date, -amount);
    }


    /**
     * 加一秒
     */
    public static Date addSeconds(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addSeconds(date, amount);
    }


    /**
     * 减一秒.
     */
    public static Date subSeconds(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addSeconds(date, -amount);
    }


    /**
     * 获得日期是一年的第几天，返回值从1开始
     */
    public static int getDayOfYear(Date date) {
        return get(date, Calendar.DAY_OF_YEAR);
    }


    /**
     * 获得日期是一月的第几周，返回值从1开始.
     * 开始的一周，只要有一天在那个月里都算. 已改为中国习惯，1 是Monday，而不是Sunday
     */
    public static int getWeekOfMonth(Date date) {
        return getWithMondayFirst(date, Calendar.WEEK_OF_MONTH);
    }


    /**
     * 获得日期是一周的第几天. 已改为中国习惯，1 是Monday，而不是Sundays.
     */
    public static int getDayOfWeek(Date date) {
        int result = getWithMondayFirst(date, Calendar.DAY_OF_WEEK);
        return result == 1 ? 7 : result - 1;
    }


    /**
     * 获得日期是一年的第几周，返回值从1开始.
     * 开始的一周，只要有一天在那一年里都算.已改为中国习惯，1 是Monday，而不是Sunday
     */
    public static int getWeekOfYear(Date date) {
        return getWithMondayFirst(date, Calendar.WEEK_OF_YEAR);
    }


    /**
     * 2017-1-23 07:33:23, 则返回2017-1-22 00:00:00
     */
    public static Date nextWeek(Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(addDays(date, 8 - getDayOfWeek(date)), Calendar.DATE);
    }


    /**
     * 2016-11-10 07:33:23, 则返回2016-11-10 00:00:00
     */
    public static Date beginOfDate(Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DATE);
    }


    /**
     * 是否闰年.
     */
    public static boolean isLeapYear(Date date) {
        return isLeapYear(get(date, Calendar.YEAR));
    }


    /**
     * 是否闰年，copy from Jodd Core的TimeUtil
     * 参数是公元计数, 如2016
     */
    public static boolean isLeapYear(int y) {
        boolean result = false;
        if (((y % 4) == 0) &&
                ((y < 1582) ||
                        ((y % 100) != 0) ||
                        ((y % 400) == 0))) {
            result = true;
        }
        return result;
    }

    /**
     * 获取某个月有多少天, 考虑闰年等因数, 移植Jodd Core的TimeUtil
     */
    public static int getMonthLength(Date date) {
        int year = get(date, Calendar.YEAR);
        int month = get(date, Calendar.MONTH);
        return getMonthLength(year, month);
    }


    /**
     * 获取某个月有多少天, 考虑闰年等因数, 移植Jodd Core的TimeUtil
     */
    public static int getMonthLength(int year, int month) {
        if ((month < 1) || (month > 12)) {
            throw new IllegalArgumentException("参数非法,月份输入范围1-12,请求month为: " + month);
        }
        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        }

        return MONTH_LENGTH[month];
    }


    private static int get(Date date, int field) {
        Validate.notNull(date, "The date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    private static int getWithMondayFirst(final Date date, int field) {
        Validate.notNull(date, "The date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        return cal.get(field);
    }

}
