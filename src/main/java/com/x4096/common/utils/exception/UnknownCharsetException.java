package com.x4096.common.utils.exception;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/5/21 23:00
 * @Description: 字节数组编码集自定义异常
 */
public class UnknownCharsetException extends RuntimeException {

    public UnknownCharsetException(){
        super("未知字符集编码");
    }

}
