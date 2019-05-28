package com.x4096.common.utils.encrypt;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
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
public class AESEncryptUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AESEncryptUtils.class);

    /**
     * 加密 KEY 必须为 16 位
     */
    private static final String KEY = "1024204840968192";

    /**
     * 默认字符集编码
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

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
     * @param content       待加密内容
     * @param encryptKey    解密 KEY
     * @return
     */
    public static String encrypt(String content, String encryptKey) {
        if (StringUtils.isBlank(content) ||  StringUtils.isBlank(encryptKey)) {
            throw new IllegalArgumentException("加密内容或加密key不能为null");
        }
        if( encryptKey.length() != KEY_LENGTH ){
            throw new IllegalArgumentException("加密的encryptKey必须为16位");
        }

        try {
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(DEFAULT_ENCODING), KEY_ALGORITHM));
            /* 解决Base64加密换行问题 */
            return Base64.encodeBase64String(cipher.doFinal(content.getBytes(DEFAULT_ENCODING)));
        } catch (Exception e) {
            LOGGER.error("加密异常, 加密前内容: {}, 加密key: {}", content, encryptKey, e);
        }
        return null;
    }

    /**
     * 加密,使用默认KEY
     *
     * @param content      待加密内容
     * @return
     */
    public static String encrypt(String content) {
        return encrypt(content, KEY);
    }

    /**
     * 解密
     *
     * @param content       待解密内容
     * @param decryptKey    解密的 KEY
     * @return
     */
    public static String decrypt(String content, String decryptKey) {
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
            LOGGER.error("解密异常, 解密前内容: {}, 解密key: {}",content, decryptKey, e);
        }
        return null;
    }

    /**
     * 解密,使用默认KEY
     *
     * @param content       待解密内容
     * @return
     */
    public static String decrypt(String content) {
        return decrypt(content, KEY);
    }

}
