package com.github.common.utils.encrypt;


import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: AES加密
 */
public class AesEncryptUtils {


    private static final Logger logger = LoggerFactory.getLogger(AesEncryptUtils.class);

    /**
     * 加密 KEY 必须为 16 位
     */
    private static final String KEY = "1024204840968192";
    /**
     * charSet字符集编码
     */
    private static final String charsetName = "utf-8";
    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "AES/ECB/PKCS5Padding";
    /**
     * 加密算法
     */
    public static final String KEY_ALGORITHM = "AES";


    /**
     * 加密
     * @param content
     * @param encryptKey
     * @return
     */
    public static String aesEncrypt(String content,String encryptKey) {
        if (content == null || encryptKey == null) {
            throw new IllegalArgumentException("加密内容或加密key不能为null");
        }
        if( encryptKey.length() != 16 ){
            throw new IllegalArgumentException("解密的encryptKey必须为16位");
        }

        try {
            Cipher cipher = Cipher.getInstance(SIGN_ALGORITHMS);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(charsetName), KEY_ALGORITHM));
            byte[] bytes = cipher.doFinal(content.getBytes(charsetName));
            String result = Base64.encodeBase64String(bytes);
            /* 解决Base64加密换行问题 */
            return result;
        } catch (Exception e) {
            logger.error("加密失败:{}",content,e);
        }
        return null;
    }

    /**
     * 加密,使用默认KEY
     * @param content
     * @return
     */
    public static String aesEncrypt(String content) {
        return aesEncrypt(content,KEY);
    }

    /**
     * 解密
     * @param content
     * @param decryptKey
     * @return
     */
    public static String aesDecrypt(String content,String decryptKey) {
        if (content == null || decryptKey == null) {
            throw new IllegalArgumentException("加密内容或加密key不能为null");
        }
        if( decryptKey.length() != 16 ){
            throw new IllegalArgumentException("解密的decryptKey必须为16位");
        }

        try{
            Cipher cipher = Cipher.getInstance(SIGN_ALGORITHMS);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(charsetName), KEY_ALGORITHM));
            byte[] bytes = new BASE64Decoder().decodeBuffer(content);
            bytes = cipher.doFinal(bytes);
            return new String(bytes, charsetName);
        }catch (Exception e){
            logger.error("解密失败:{}",content,e);
        }
        return null;
    }

    /**
     * 解密,使用默认KEY
     * @param content
     * @return
     */
    public static String aesDecrypt(String content) {
        return aesDecrypt(content,KEY);
    }

}
