package com.x4096.common.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
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


    /**
     * 指定key的大小
     */
    private static final int KEY_SIZE = 2048;

    /**
     * 字符编码集
     */
    public static final String CHAR_ENCODING = "UTF-8";


    /**
     * AES 算法填充方式
     */
    public static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * RSA 算法填充方式
     */
    public static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";



    /**
     * 生成密钥对
     */
    public static Map<String, String> generateKeyPair() {
        String publicKeyString = null;
        String privateKeyString = null;

        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom sr = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator kpg = null;
        KeyPair kp = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
            kpg.initialize(KEY_SIZE, sr);
            /** 生成密匙对 */
            kp = kpg.generateKeyPair();
            /** 得到公钥 */
            Key publicKey = kp.getPublic();
            byte[] publicKeyBytes = publicKey.getEncoded();
            publicKeyString = new String(Base64.encodeBase64(publicKeyBytes), CHAR_ENCODING);
            /** 得到私钥 */
            Key privateKey = kp.getPrivate();
            byte[] privateKeyBytes = privateKey.getEncoded();
            privateKeyString = new String(Base64.encodeBase64(privateKeyBytes), CHAR_ENCODING);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("publicKey", publicKeyString);
        map.put("privateKey", privateKeyString);
        RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
        BigInteger bint = rsp.getModulus();
        byte[] b = bint.toByteArray();
        byte[] deBase64Value = Base64.encodeBase64(b);
        String retValue = new String(deBase64Value);
        map.put("modulus", retValue);
        return map;
    }

    /**
     * 加密方法 source： 源数据
     */
    public static String encrypt(String source, String publicKey)
            throws Exception {
        Key key = getPublicKey(publicKey);
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        return new String(Base64.encodeBase64(b1), CHAR_ENCODING);
    }

    /**
     * 解密算法 cryptograph:密文
     */
    public static String decrypt(String cryptograph, String privateKey)
            throws Exception {
        Key key = getPrivateKey(privateKey);
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = Base64.decodeBase64(cryptograph.getBytes());
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * 得到公钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                Base64.decodeBase64(key.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException |InvalidKeySpecException e) {
            LOGGER.error("获取公钥异常, ", e);
        }
        return null;
    }

    /**
     * 得到私钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
                Base64.decodeBase64(key.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error("获取私钥异常", e);
        }
        return null;
    }


    /**
     * 获取签名
     *
     * @param content       内容
     * @param privateKey    私钥
     * @return
     */
    public static String sign(String content, String privateKey) {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                Base64.decodeBase64(privateKey.getBytes()));
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
     * @param content
     * @param sign
     * @param publicKey
     * @return
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

            return signature.verify(new Base64().decode(sign.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | UnsupportedEncodingException | SignatureException e) {
            LOGGER.error("RSA 校验签名异常, ", e);
        }
        return false;
    }

    public static void main(String[] args) {
        Map<String, String> keys = generateKeyPair();
        System.out.println(keys.get("publicKey"));
        System.out.println(keys.get("privateKey"));
        System.out.println(keys.get("modulus"));
    }


}
