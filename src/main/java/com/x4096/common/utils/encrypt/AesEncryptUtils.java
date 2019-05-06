package com.x4096.common.utils.encrypt;


import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: AES加密,对称加密
 */
public class AesEncryptUtils {


    /**
     * 加密 KEY 必须为 16 位
     */
    private static final String KEY = "1024204840968192";

    /**
     * 默认字符集编码
     */
    private static final String DEFAULT_ENCODING = "utf-8";

    /**
     * 填充方式
     */
    private static final String PADDING = "AES/ECB/PKCS5Padding";

    /**
     * 加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 加解密key长度
     */
    private static final int KEY_LENGTH = 16;


    /**
     * 加密
     *
     * @param content
     * @param encryptKey
     * @return
     */
    public static String encrypt(String content, String encryptKey) {
        if (content == null || encryptKey == null) {
            throw new IllegalArgumentException("加密内容或加密key不能为null");
        }
        if( encryptKey.length() != KEY_LENGTH ){
            throw new IllegalArgumentException("加密的encryptKey必须为16位");
        }

        try {
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(DEFAULT_ENCODING), KEY_ALGORITHM));
            byte[] bytes = cipher.doFinal(content.getBytes(DEFAULT_ENCODING));
            /* 解决Base64加密换行问题 */
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密,使用默认KEY
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        return encrypt(content, KEY);
    }

    /**
     * 解密
     * @param content
     * @param decryptKey
     * @return
     */
    public static String decrypt(String content,String decryptKey) {
        if (content == null || decryptKey == null) {
            throw new IllegalArgumentException("解密内容或解密key不能为null");
        }
        if( decryptKey.length() != KEY_LENGTH){
            throw new IllegalArgumentException("解密的decryptKey必须为16位");
        }

        try{
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(DEFAULT_ENCODING), KEY_ALGORITHM));
            byte[] bytes = new BASE64Decoder().decodeBuffer(content);
            bytes = cipher.doFinal(bytes);
            return new String(bytes, DEFAULT_ENCODING);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密,使用默认KEY
     * @param content
     * @return
     */
    public static String decrypt(String content) {
        return decrypt(content, KEY);
    }

}
