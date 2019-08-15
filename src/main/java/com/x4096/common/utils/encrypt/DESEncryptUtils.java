package com.x4096.common.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2019/1/10
 * @instructions: Des加密工具类, 对称加密
 */
public class DESEncryptUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DESEncryptUtils.class);

    private DESEncryptUtils() {
    }

    /**
     * 加密算法DES
     */
    private static final String DES = "DES";

    /**
     * 填充方式
     */
    private static final String PADDING = "DES/ECB/PKCS5Padding";

    /**
     * 默认编码集
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * 加密 KEY
     */
    private static final String KEY = "1024204840968192";


    /**
     * 加解密key长度
     */
    private static final int KEY_LENGTH = 16;


    public static String encrypt(String content, String encryptKey) {
        if (content == null || encryptKey == null) {
            throw new NullPointerException("加密内容或加密key不能为null");
        }

        SecureRandom secureRandom = new SecureRandom();
        try {
            DESKeySpec desKeySpec = new DESKeySpec(encryptKey.getBytes(DEFAULT_ENCODING));
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = secretKeyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, securekey, secureRandom);
            return Base64.encodeBase64String(cipher.doFinal(content.getBytes(DEFAULT_ENCODING)));
        } catch (Exception e) {
            LOGGER.error("加密异常, 加密前内容: {}, 加密key: {}", content, encryptKey, e);
        }
        return null;
    }


    public static String decrypt(String content, String decryptKey) {
        if (content == null || decryptKey == null) {
            throw new NullPointerException("解密内容或解密key不能为null");
        }

        SecureRandom secureRandom = new SecureRandom();
        try {
            byte[] bytes = decryptKey.getBytes(DEFAULT_ENCODING);
            DESKeySpec desKeySpec = new DESKeySpec(bytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.DECRYPT_MODE, securekey, secureRandom);
            byte[] decodeBuffer = new BASE64Decoder().decodeBuffer(content);
            decodeBuffer = cipher.doFinal(decodeBuffer);
            return new String(decodeBuffer, DEFAULT_ENCODING);
        } catch (Exception e) {
            LOGGER.error("解密异常, 解密前内容: {}, 解密key: {}", content, decryptKey, e);
        }
        return null;
    }


    public static void main(String[] args) {

        String encrypt = encrypt("hhhh", KEY);

        System.out.println(encrypt);


    }

}
