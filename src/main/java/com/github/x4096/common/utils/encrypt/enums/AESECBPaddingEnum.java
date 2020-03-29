package com.github.x4096.common.utils.encrypt.enums;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/2/2 17:40
 * @description
 * @readme
 */
public enum AESECBPaddingEnum {

    PKCS5("AES/ECB/PKCS5Padding"),
    PKCS7("AES/ECB/PKCS7Padding"),
    NO("AES/ECB/NoPadding"),

    ;

    private final String padding;

    AESECBPaddingEnum(String padding) {
        this.padding = padding;
    }

    public String getPadding() {
        return padding;
    }

}
