package com.x4096.common.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2019/1/10
 * @instructions: Des加密工具类,对称加密
 */
public class DESEncryptUtils {

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


    /**
     * 加密
     *
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        return encrypt(content, KEY);
    }


    /**
     * 加密
     *
     * @param content 待加密内容
     * @param encryptKey 加密的密钥
     * @return
     */
    public static String encrypt(String content,String encryptKey) {
//        if (content == null || encryptKey == null) {
//            throw new IllegalArgumentException("加密内容或加密key不能为null");
//        }
//        if( encryptKey.length() != 16 ){
//            throw new IllegalArgumentException("加密的encryptKey必须为16位");
//        }

        try {
            return Base64.encodeBase64String(encrypt(content.getBytes(DEFAULT_ENCODING), encryptKey.getBytes(DEFAULT_ENCODING)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 加密
     * @param content
     * @param encryptKey
     * @return
     */
    public static byte[] encrypt(byte[] content, byte[] encryptKey) {
        if(content == null || encryptKey == null){
            throw new NullPointerException("content或encryptKey不能为null");
        }
        SecureRandom sr = new SecureRandom();
        try{
            DESKeySpec dks = new DESKeySpec(encryptKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            return cipher.doFinal(content);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 加密
     * @param content
     * @return
     */
    // public final static String decrypt(String content ) {
    //     return decrypt(content,KEY);
    // }


    /**
     * 解密
     * @param content
     * @param decryptKey
     * @return
     */
    // public final static String decrypt(String content, String decryptKey ) {
    //     if (content == null || decryptKey == null) {
    //         throw new IllegalArgumentException("解密内容或解密key不能为null");
    //     }
    //     if( decryptKey.length() != KEY_LENGTH ){
    //         throw new IllegalArgumentException("解密的encryptKey必须为16位");
    //     }
    //
    //     try {
    //         return new String(decrypt(Base64.decodeBase64(content),decryptKey.getBytes(DEFAULT_ENCODING)), DEFAULT_ENCODING);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }


    public static void main(String[] args) {

        String ss = encrypt("666","1234567899874447774654");
        System.out.println(ss);
//        System.out.println(decrypt(ss));

    }

}
