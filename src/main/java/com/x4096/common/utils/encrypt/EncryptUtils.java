package com.x4096.common.utils.encrypt;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author: peng.zhup
 * @Project: common-utils
 * @DateTime: 2019/6/8 0:19
 * @Description: 加密工具类, 包括 MD5, sha1 等
 */
public class EncryptUtils {

    private EncryptUtils() {
    }


    public static String MD2(String content) {
        if (content == null) {
            throw new NullPointerException("待加密内容不能为 null");
        }
        return DigestUtils.md2Hex(content);
    }


    public static String MD5(String content) {
        if (content == null) {
            throw new NullPointerException("待加密内容不能为 null");
        }
        return DigestUtils.md5Hex(content);
    }


    public static String SHA1(String content) {
        if (content == null) {
            throw new NullPointerException("待加密内容不能为 null");
        }
        return DigestUtils.sha1Hex(content);
    }


    public static String SHA256(String content) {
        if (content == null) {
            throw new NullPointerException("待加密内容不能为 null");
        }
        return DigestUtils.sha256Hex(content);
    }


    public static String SHA384(String content) {
        if (content == null) {
            throw new NullPointerException("待加密内容不能为 null");
        }
        return DigestUtils.sha384Hex(content);
    }


    public static String SHA512(String content) {
        if (content == null) {
            throw new NullPointerException("待加密内容不能为 null");
        }
        return DigestUtils.sha512Hex(content);
    }

}
