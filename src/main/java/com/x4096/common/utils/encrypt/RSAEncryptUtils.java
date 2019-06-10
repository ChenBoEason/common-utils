package com.x4096.common.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/5/17 23:44
 * @Description: RSA 加密算法工具类, 世界上第一个非对称加密
 */
public class RSAEncryptUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAEncryptUtils.class);


    private static final int KEY_SIZE_1024 = 1024;

    private static final int KEY_SIZE_2048 = 2048;

    /**
     * 字符编码集
     */
    private static final String CHAR_ENCODING = "UTF-8";


    /**
     * RSA 算法填充方式
     */
    public static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";


    /**
     * 生成 1024 keysize 的密匙对
     *
     * @return
     */
    public static KeyPair getKeyPair1024(){
        return getKeyPair(KEY_SIZE_1024);
    }


    /**
     *  生成 2048 keysize 的密匙对
     *
     * @return
     */
    public static KeyPair getKeyPair2048(){
        return getKeyPair(KEY_SIZE_2048);
    }


    /**
     * 获取公钥字符串
     *
     * @param keyPair
     * @return
     */
    public static String getPublicKeyString(KeyPair keyPair){
        Key publicKey = keyPair.getPublic();
        return bytesToString(publicKey.getEncoded());
    }

    /**
     * 获取私钥字符串
     *
     * @param keyPair
     * @return
     */
    public static String getPrivateKeyString(KeyPair keyPair){
        Key privateKey = keyPair.getPrivate();
        return bytesToString(privateKey.getEncoded());
    }



    /**
     * 通过公钥加密
     *
     * @param content       待加密内容
     * @param publicKey     RSA 公钥
     * @return              加密后内容
     */
    public static String encryptByPublicKey(String content, String publicKey) {
        return encrypt(content, publicKey, true);
    }


    /**
     * 通过私钥加密
     *
     * @param content       待加密内容
     * @param publicKey     RSA 私钥
     * @return              加密后内容
     */
    public static String encryptByPrivateKey(String content, String publicKey) {
        return encrypt(content, publicKey, false);
    }


    /**
     * 通过公钥解密
     *
     * @param cryptograph       待解密密文
     * @param publicKey         RSA 公钥
     * @return                  解密后内容
     */
    public static String decryptByPublicKey(String cryptograph, String publicKey) {
        return decrypt(cryptograph, publicKey, true);
    }


    /**
     * 通过私钥解密
     *
     * @param cryptograph       待解密密文
     * @param privateKey        RSA 私钥
     * @return                  解密后内容
     */
    public static String decryptByPrivateKey(String cryptograph, String privateKey) {
        return decrypt(cryptograph, privateKey, false);
    }


    /**
     * 得到公钥
     *
     * @param publicKey         公钥密钥字符串
     */
    public static PublicKey getPublicKey(String publicKey) {
        KeyFactory keyFactory = getKeyFactory();
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        try {
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            LOGGER.error("", e);
        }
        return null;
    }


    /**
     * 得到私钥
     *
     * @param privateKey        私钥密钥字符串
     */
    public static PrivateKey getPrivateKey(String privateKey) {
        KeyFactory keyFactory = getKeyFactory();
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            LOGGER.error("", e);
        }
        return null;
    }


    /**
     * 获取 KeyFactory
     *
     * @return
     */
    private static KeyFactory getKeyFactory(){
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("", e);
        }
        return keyFactory;
    }


    /**
     * 获取签名
     *
     * @param content       内容
     * @param privateKey    私钥
     * @return
     */
    public static String sign(String content, String privateKey) {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes()));
        KeyFactory keyf = null;
        try {
            keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(priKey);
            signature.update(content.getBytes(CHAR_ENCODING));
            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | UnsupportedEncodingException | SignatureException e) {
            LOGGER.error("RSA 获取签名异常, 待签名内容content: {}, 私钥privateKey: {}",content, privateKey, e);
        }
        return null;
    }


    /**
     * 验证签名
     *
     * @param content       待验证签名内容
     * @param sign          签名
     * @param publicKey     公钥
     * @return              验证签名是否通过
     */
    public static boolean checkSign(String content, String sign, String publicKey){
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = new Base64().decode(content.getBytes());
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature
                    .getInstance("SHA256WithRSA");

            signature.initVerify(pubKey);
            signature.update(content.getBytes(CHAR_ENCODING));

            return signature.verify(new Base64().decodeBase64(sign.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | UnsupportedEncodingException | SignatureException e) {
            LOGGER.error("RSA 校验签名异常, ", e);
        }
        return false;
    }


    /**
     * 签名
     *
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }


    /**
     * get KeyPair
     *
     * @param length
     * @return
     */
    private static KeyPair getKeyPair(int length){
        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("不支持的算法", e);
        }
        keyPairGenerator.initialize(length, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }


    /**
     * 字节数组转字符串
     *
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes){
        try {
            return new String(Base64.encodeBase64(bytes), CHAR_ENCODING);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param content
     * @param encryptKey
     * @param isPublicKey
     * @return
     */
    private static String encrypt(String content, String encryptKey, boolean isPublicKey){
        Key key = null;
        if(isPublicKey){
            key = getPublicKey(encryptKey);
        }else{
            key = getPrivateKey(encryptKey);
        }

        try {
            /** 得到Cipher对象来实现对源数据的RSA加密 */
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            /** 执行加密操作 */
            byte[] bytes = cipher.doFinal(content.getBytes());
            return new String(Base64.encodeBase64(bytes), CHAR_ENCODING);
        }catch (Exception e){
            LOGGER.error("RSA加密失败,加密内容: {}, 加密key: {}", content, encryptKey);
        }
        return null;
    }


    /**
     * 解密
     *
     * @param cryptograph
     * @param decryptKey
     * @param isPublicKey
     * @return
     */
    private static String decrypt(String cryptograph, String decryptKey, boolean isPublicKey) {
        Key key = null;
        if(isPublicKey){
            key = getPublicKey(decryptKey);
        }else{
            key = getPrivateKey(decryptKey);
        }

        try{
            /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            /** 执行解密操作 */
            byte[] bytes = cipher.doFinal(Base64.decodeBase64(cryptograph.getBytes()));
            return new String(bytes, CHAR_ENCODING);
        }catch (Exception e){
            LOGGER.error("RSA解密失败,密文: {}, 解密key: {}", cryptograph, decryptKey, e);
        }
        return null;
    }





    public static void main(String[] args) {

        KeyPair keyPair = getKeyPair2048();
        String publicKey = getPublicKeyString(keyPair);
        String privateKey = getPrivateKeyString(keyPair);

        System.out.println(publicKey);
        System.out.println(privateKey);

        // String 密文 = encryptByPublicKey("哈哈哈哈，1024", publicKey);
        // System.out.println(密文);
        // System.out.println(decryptByPrivateKey(密文, privateKey));

        String 密文2 = encryptByPrivateKey("哈哈哈哈，2048", privateKey);
        System.out.println(密文2);
        System.out.println(decryptByPublicKey(密文2, publicKey));



        // String sign = sign("数据体", privateKey);
        // System.out.println(sign);

        // System.out.println(checkSign("数据体", sign, ""));



    }


}
