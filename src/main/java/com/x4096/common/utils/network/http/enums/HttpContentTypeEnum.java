package com.x4096.common.utils.network.http.enums;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-06 18:30
 * @Description: ContentType 枚举
 */
public enum HttpContentTypeEnum {

    APPLICATION_JSON("application/json;charset=UTF-8", ""),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded", ""),

    ;

    private final String type;

    private final String desc;

    HttpContentTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

}
