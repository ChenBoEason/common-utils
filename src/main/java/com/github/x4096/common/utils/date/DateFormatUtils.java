package com.github.x4096.common.utils.date;

import com.github.x4096.common.utils.date.enums.DateFormatEnum;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.Clock;
import java.util.Date;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-28 22:48
 * @Description:
 */
public class DateFormatUtils {

    private DateFormatUtils() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DateFormatUtils.class);

    /**
     * 格式化日期的标准字符串 更多查看 {@link DateFormatEnum}
     */
    public static final String PATTERN_DEFAULT_ON_SECOND = DateFormatEnum.YEAR_MONTH_DAY_24.getFormat();


    /**
     * 解析时间字符串
     *
     * @param dateString 时间字符串,默认解析格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date parse(String dateString) {
        return parse(dateString, DateFormatEnum.YEAR_MONTH_DAY_24.getFormat());
    }


    /**
     * 解析时间字符串
     *
     * @param dateString     时间字符串
     * @param dateFormatEnum 解析样式
     * @return
     */
    public static Date parse(String dateString, DateFormatEnum dateFormatEnum) {
        return parse(dateString, dateFormatEnum.getFormat());
    }


    /**
     * 解析时间字符串
     *
     * @param dateString 时间字符串
     * @param pattern    解析的样式 参考 {@link DateFormatEnum}
     * @return
     */
    public static Date parse(String dateString, String pattern) {
        try {
            return FastDateFormat.getInstance(pattern).parse(dateString);
        } catch (ParseException e) {
            LOGGER.error("时间解析异常, 请求入参: pattern: {}, dateString: {}", pattern, dateString, e);
            return null;
        }
    }


    /**
     * 格式化日期
     * 默认格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date 时间
     * @return
     */
    public static String format(Date date) {
        return format(date, PATTERN_DEFAULT_ON_SECOND);
    }


    /**
     * 格式化日期
     *
     * @param date    时间
     * @param pattern 格式样式 参考 {@link DateFormatEnum}
     * @return
     */
    public static String format(Date date, String pattern) {
        return FastDateFormat.getInstance(pattern).format(date);
    }


    /**
     * 格式化日期
     *
     * @param date           时间
     * @param dateFormatEnum 格式样式
     * @return
     */
    public static String format(Date date, DateFormatEnum dateFormatEnum) {
        return FastDateFormat.getInstance(dateFormatEnum.getFormat()).format(date);
    }


    /**
     * 打印用户友好的，与当前时间相比的时间差，如刚刚，5分钟前，今天XXX，昨天XXX
     */
    public static String formatFriendlyTimeSpanByNow(Date date) {
        return formatFriendlyTimeSpanByNow(date.getTime());
    }


    /**
     * 打印用户友好的，与当前时间相比的时间差，如刚刚，5分钟前，今天XXX，昨天XXX
     */
    public static String formatFriendlyTimeSpanByNow(long timeStampMillis) {
        long now = Clock.systemDefaultZone().millis();
        long span = now - timeStampMillis;
        if (span < 0) {
            // 'c' 日期和时间，被格式化为 "%ta %tb %td %tT %tZ %tY"，例如 "Sun Jul 20 16:17:00 EDT 1969"。
            return String.format("%tc", timeStampMillis);
        }
        if (span < DateUtils.MILLIS_PER_SECOND) {
            return "刚刚";
        } else if (span < DateUtils.MILLIS_PER_MINUTE) {
            return String.format("%d秒前", span / DateUtils.MILLIS_PER_SECOND);
        } else if (span < DateUtils.MILLIS_PER_HOUR) {
            return String.format("%d分钟前", span / DateUtils.MILLIS_PER_MINUTE);
        }
        // 获取当天00:00
        long wee = DateUtils.beginOfDate(new Date(now)).getTime();
        if (timeStampMillis >= wee) {
            // 'R' 24 小时制的时间，被格式化为 "%tH:%tM"
            return String.format("今天%tR", timeStampMillis);
        } else if (timeStampMillis >= wee - DateUtils.MILLIS_PER_DAY) {
            return String.format("昨天%tR", timeStampMillis);
        } else {
            // 'F' ISO 8601 格式的完整日期，被格式化为 "%tY-%tm-%td"。
            return String.format("%tF", timeStampMillis);
        }
    }

}
