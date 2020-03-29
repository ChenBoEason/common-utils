package com.github.x4096.common.utils.encrypt.enums;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/1/14 21:14
 * @description
 * @readme
 */
public enum AESPaddingEnum {

    PKCS5("PKCS5Padding"),
    PKCS7("PKCS7Padding"),
    NO("NoPadding");

    private final String padding;

    AESPaddingEnum(String padding) {
        this.padding = padding;
    }

    public String getPadding() {
        return padding;
    }

}
