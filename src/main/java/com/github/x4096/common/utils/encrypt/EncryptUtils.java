package com.github.x4096.common.utils.encrypt;

import com.google.common.base.Preconditions;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


/**
 * @Author: peng.zhup
 * @Project: common-utils
 * @DateTime: 2019/6/8 0:19
 * @Description: 加密工具类, 包括 MD5, sha1 等
 */
public class EncryptUtils {

    private EncryptUtils() {
    }

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static final char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    public static String MD2(String content) {
        Preconditions.checkNotNull(content, "待加密内容不能为 null");
        return DigestUtils.md2Hex(content);
    }


    public static String MD5(String content) {
        Preconditions.checkNotNull(content, "待加密内容不能为 null");
        return DigestUtils.md5Hex(content);
    }


    public static String SHA1(String content) {
        Preconditions.checkNotNull(content, "待加密内容不能为 null");
        return DigestUtils.sha1Hex(content);
    }


    public static String SHA256(String content) {
        Preconditions.checkNotNull(content, "待加密内容不能为 null");
        return DigestUtils.sha256Hex(content);
    }


    public static String SHA384(String content) {
        Preconditions.checkNotNull(content, "待加密内容不能为 null");
        return DigestUtils.sha384Hex(content);
    }


    public static String SHA512(String content) {
        Preconditions.checkNotNull(content, "待加密内容不能为 null");
        return DigestUtils.sha512Hex(content);
    }

    /**
     * HMAC-SHA1加密方案<br>
     * @param content-待加密内容
     * @param secretKey-密钥
     * @return HMAC_SHA1加密后的字符串
     */
    public static String HMACSHA1(String content, String secretKey) {
        Preconditions.checkNotNull(content, "content 不能为null");
        Preconditions.checkNotNull(secretKey, "secretKey 不能为null");
        byte[] secretKeyBytes = secretKey.getBytes();
        SecretKey secretKeyObj = new SecretKeySpec(secretKeyBytes, HMAC_SHA1_ALGORITHM);

        try {
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(secretKeyObj);
            byte[] text = content.getBytes(StandardCharsets.UTF_8);
            byte[] encryptContentBytes = mac.doFinal(text);
            //SHA1算法得到的签名长度，都是160位二进制码，换算成十六进制编码字符串表示
            return bytesToHexString(encryptContentBytes);
        } catch (Exception e) {

        }
        return content;
    }


    /**
     * 获取字节数组的16进制字符串表示形式
     * 范例：0xff->'ff'
     * @param bytes 字节数组
     * @return string-16进制的字符串表示形式
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for(byte ib : bytes) {
            char[] ob = new char[2];
            ob[0] = Digit[(ib >>> 4) & 0X0f];
            ob[1] = Digit[ib & 0X0F];
            hexString.append(ob);
        }
        return hexString.toString();
    }

}
