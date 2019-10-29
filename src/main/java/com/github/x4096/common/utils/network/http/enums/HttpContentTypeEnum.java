package com.github.x4096.common.utils.network.http.enums;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-06 18:30
 * @Description: ContentType 枚举
 */
public enum HttpContentTypeEnum {

    TEXT_HTML("text/html", "HTML格式"),
    TEXT_PLAIN("text/plain", "纯文本格式"),
    TEXT_XML("text/xml", "XML格式"),

    APPLICATION_JSON("application/json", "JSON数据格式"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded", "表单默认的提交数据的格式"),

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
