package com.github.x4096.common.utils.encrypt;


import com.github.x4096.common.utils.constant.CharsetConstants;
import com.github.x4096.common.utils.encrypt.enums.AESECBPaddingEnum;
import com.github.x4096.common.utils.encrypt.enums.AESModeEnum;
import com.github.x4096.common.utils.encrypt.enums.AESPaddingEnum;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: AES加密, 对称加密
 */
public class AESEncryptUtils {

    private AESEncryptUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(AESEncryptUtils.class);

    /**
     * 加密 KEY 必须为 16 位
     */
    private static final String ENCRYPT_KEY = "1024204840968192";

    /**
     * 默认字符集编码
     */
    private static final String DEFAULT_ENCODING = CharsetConstants.UTF_8;

    /**
     * 加密算法
     */
    private static final String ALGORITHM = "AES";


    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    /**
     * 加密
     *
     * @param content 待加密内容
     * @return 密文
     */
    public static String encrypt(String content) {
        return encrypt(content, ENCRYPT_KEY, AESECBPaddingEnum.PKCS5);
    }


    /**
     * 加密 默认 AES/ECB/PKCS5Padding
     *
     * @param content    待加密内容
     * @param encryptKey 加密 KEY
     * @return 密文
     */
    public static String encrypt(String content, String encryptKey) {
        return encrypt(content, encryptKey, AESECBPaddingEnum.PKCS5);
    }


    /**
     * 加密
     *
     * @param content           待加密内容
     * @param encryptKey        加密 KEY
     * @param aesecbPaddingEnum 加密填充类型
     * @return 密文
     */
    public static String encrypt(String content, String encryptKey, AESECBPaddingEnum aesecbPaddingEnum) {
        if (content == null || encryptKey == null || null == aesecbPaddingEnum) {
            throw new IllegalArgumentException("加密内容 content 和 encryptKey 和 aesecbPaddingEnum 不能为 null");
        }

        keySizeCheck(encryptKey);

        try {
            Cipher cipher = Cipher.getInstance(aesecbPaddingEnum.getPadding());
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(DEFAULT_ENCODING), ALGORITHM));
            /* 解决Base64加密换行问题 */
            return Base64.encodeBase64String(cipher.doFinal(content.getBytes(DEFAULT_ENCODING)));
        } catch (NoSuchAlgorithmException | InvalidKeyException
                | NoSuchPaddingException | BadPaddingException
                | IllegalBlockSizeException | UnsupportedEncodingException e) {
            logger.error("加密异常, content: {}, encryptKey: {}, aesPadding: {}", content, encryptKey, aesecbPaddingEnum.getPadding(), e);
        }

        return null;
    }


    /**
     * 加密
     *
     * @param content        待加密内容
     * @param encryptKey     加密 KEY
     * @param initVector     初始向量
     * @param aesModeEnum    加密模式
     * @param aesPaddingEnum 加密填充类型
     * @return 密文
     * @apiNote 不支持EBC模式
     */
    public static String encrypt(String content, String encryptKey, String initVector, AESModeEnum aesModeEnum, AESPaddingEnum aesPaddingEnum) {
        if (null == content
                || null == aesModeEnum
                || null == aesPaddingEnum
                || StringUtils.isAnyBlank(encryptKey, initVector)) {
            throw new IllegalArgumentException("content 和 aesPaddingEnum 不能为 null, encryptKey 和 iv 不能为空 ");
        }

        keySizeCheck(encryptKey);

        if (aesModeEnum == AESModeEnum.ECB) {
            throw new IllegalArgumentException("当前加密不支持 ECB 模式");
        }

        try {
            Cipher cipher = Cipher.getInstance("AES/" + aesModeEnum + "/" + aesPaddingEnum.getPadding());
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector.getBytes(DEFAULT_ENCODING));
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(DEFAULT_ENCODING), ALGORITHM), ivParameterSpec);
            /* 解决Base64加密换行问题 */
            return Base64.encodeBase64String(cipher.doFinal(content.getBytes(DEFAULT_ENCODING)));
        } catch (NoSuchAlgorithmException | InvalidKeyException
                | InvalidAlgorithmParameterException | NoSuchPaddingException
                | BadPaddingException | IllegalBlockSizeException
                | UnsupportedEncodingException e) {
            logger.error("加密异常, content: {}, encryptKey: {}, iv: {}, aesPadding: {}",
                    content, encryptKey, initVector, aesPaddingEnum.getPadding(), e);
        }

        return null;
    }


    /**
     * 解密
     *
     * @param content 待解密内容
     * @return 明文
     */
    public static String decrypt(String content) {
        return decrypt(content, ENCRYPT_KEY, AESECBPaddingEnum.PKCS5);
    }


    /**
     * 解密 默认 AES/ECB/PKCS5Padding-128
     *
     * @param content    待解密内容
     * @param decryptKey 解密的 KEY
     * @return 明文
     * @apiNote 注意: 不支持CBC模式
     */
    public static String decrypt(String content, String decryptKey) {
        return decrypt(content, decryptKey, AESECBPaddingEnum.PKCS5);
    }


    /**
     * 解密
     *
     * @param content           待解密内容
     * @param decryptKey        解密的 KEY
     * @param aesecbPaddingEnum 填充类型
     * @return 明文
     */
    public static String decrypt(String content, String decryptKey, AESECBPaddingEnum aesecbPaddingEnum) {
        if (null == content || null == decryptKey || null == aesecbPaddingEnum) {
            throw new IllegalArgumentException("content 或 decryptKey 或 aesecbPaddingEnum 不能为null");
        }

        keySizeCheck(decryptKey);

        try {
            Cipher cipher = Cipher.getInstance(aesecbPaddingEnum.getPadding());
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(DEFAULT_ENCODING), ALGORITHM));
            byte[] bytes = Base64.decodeBase64(content);
            bytes = cipher.doFinal(bytes);
            return new String(bytes, DEFAULT_ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("解密异常, 解密前内容: {}, 解密key: {}", content, decryptKey, e);
        }

        return null;
    }


    /**
     * 解密
     *
     * @param content        要解密的内容
     * @param decryptKey     消息秘钥
     * @param initVector     秘钥初始化向量
     * @param aesModeEnum    加密模式
     * @param aesPaddingEnum 填充类型
     * @return 明文
     * @apiNote 注意: 不支持EBC模式
     */
    public static String decrypt(String content, String decryptKey, String initVector, AESModeEnum aesModeEnum, AESPaddingEnum aesPaddingEnum) {
        if (null == content
                || null == aesModeEnum
                || null == aesPaddingEnum
                || StringUtils.isAnyBlank(decryptKey, initVector)) {
            throw new IllegalArgumentException("content 和 aesModeEnum 和 aesPaddingEnum 不能为 null, decryptKey 和 iv 不能为空 ");
        }

        keySizeCheck(decryptKey);

        if (aesModeEnum == AESModeEnum.ECB) {
            throw new IllegalArgumentException("该加密方法不支持 ECB 模式!");
        }

        try {
            Cipher cipher = Cipher.getInstance("AES/" + aesModeEnum + "/" + aesPaddingEnum.getPadding());
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector.getBytes(DEFAULT_ENCODING));
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(DEFAULT_ENCODING), ALGORITHM), ivParameterSpec);
            byte[] bytes = content.getBytes(DEFAULT_ENCODING);
            bytes = cipher.doFinal(bytes);
            return new String(bytes, DEFAULT_ENCODING);
        } catch (NoSuchAlgorithmException | BadPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | NoSuchPaddingException
                | IllegalBlockSizeException | IOException e) {
            logger.error("解密异常, content: {}, decryptKey: {}, initVector: {}, aesModeEnum: {}, aesPaddingEnum: {}",
                    content, decryptKey, initVector, aesModeEnum.name(), aesPaddingEnum.getPadding(), e);
        }

        return null;
    }


    private static void keySizeCheck(String key) {
        int length = key.length();
        if (!(length == 16 || length == 24 || length == 32)) {
            throw new IllegalArgumentException("加解密key的长度只能为16, 24 或者 32");
        }
    }

}
