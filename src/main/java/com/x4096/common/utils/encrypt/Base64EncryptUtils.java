package com.x4096.common.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/5/5 23:09
 * @Description: Base64 加解密工具类
 */
public class Base64EncryptUtils {

    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;



    /**
     * BASE64字符串解码为二进制数据
     *
     * @param str
     * @return
     */
    public static byte[] decode(String str) {
        if(str == null){
            throw new IllegalArgumentException("字符串内容不能为null");
        }
        return new Base64().decode(str.getBytes());
    }


    /**
     * 字节数组转换为字符串
     *
     * @param bytes
     * @return
     */
    public static String encode(byte[] bytes){
        return new String(new Base64().encode(bytes));
    }



    public static void main(String[] args) {
        System.out.println(decode(""));
    }


}
