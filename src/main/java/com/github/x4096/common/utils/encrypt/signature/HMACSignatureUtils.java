package com.github.x4096.common.utils.encrypt.signature;

import com.google.common.base.Preconditions;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-11-29 23:15
 * @Description: HMAC 签名工具类
 */
public class HMACSignatureUtils {

    private HMACSignatureUtils() {
    }

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static final char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * HMAC-SHA1加密方案
     *
     * @param content   待加密内容
     * @param secretKey 密钥
     * @return HMAC_SHA1加密后的字符串
     */
    public static String HMACSHA1(String content, String secretKey) {
        Preconditions.checkNotNull(content, "content 不能为 null");
        Preconditions.checkNotNull(secretKey, "secretKey 不能为 null");
        byte[] secretKeyBytes = secretKey.getBytes();
        SecretKey secretKeyObj = new SecretKeySpec(secretKeyBytes, HMAC_SHA1_ALGORITHM);

        try {
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(secretKeyObj);
            byte[] text = content.getBytes(StandardCharsets.UTF_8);
            byte[] encryptContentBytes = mac.doFinal(text);
            return bytesToHexString(encryptContentBytes);
        } catch (Exception e) {

        }
        return content;
    }


    /**
     * 获取字节数组的16进制字符串表示形式
     * 范例：0xff->'ff'
     *
     * @param bytes 字节数组
     * @return string-16进制的字符串表示形式
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte ib : bytes) {
            char[] ob = new char[2];
            ob[0] = Digit[(ib >>> 4) & 0X0f];
            ob[1] = Digit[ib & 0X0F];
            hexString.append(ob);
        }
        return hexString.toString();
    }

}
