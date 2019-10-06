package com.x4096.common.utils.date;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/6/1 22:28
 * @Description: 时间格式转换枚举类, 常用时间格式化
 */
public enum DateFormatEnum {


    CHINESE_DATE_12("yyyy年MM月dd日 hh时mm分ss秒"),

    CHINESE_DATE_24("yyyy年MM月dd日 HH时mm分ss秒"),

    CHINESE_DATE_12_WEEK("yyyy年MM月dd日 hh时mm分ss秒 E"),

    CHINESE_DATE_24_WEEK("yyyy年MM月dd日 HH时mm分ss秒 E"),

    YEAR_MONTH_DAY_12("yyyy-MM-dd hh:mm:ss"),

    YEAR_MONTH_DAY_24("yyyy-MM-dd HH:mm:ss"),


    ;

    private final String format;

    DateFormatEnum(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

}
