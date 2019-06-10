package com.x4096.common.utils.date;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/6/1 22:04
 * @Description: 时间枚举类
 */
public enum DateEnums {

    /** 秒 */
    SECOND(13),

    /** 分钟 */
    MINUTE(12),

    /** 小时 */
    HOUR(11),

    /** 天 */
    DAY(6),

    /** 月 */
    MONTH(2),


    ;

    private final int code;

    DateEnums(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
