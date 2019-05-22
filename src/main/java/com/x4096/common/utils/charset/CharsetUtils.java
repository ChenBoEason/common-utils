package com.x4096.common.utils.charset;

import com.x4096.common.utils.exception.UnknownCharsetException;
import org.mozilla.universalchardet.UniversalDetector;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/5/21 22:51
 * @Description: 字符集工具类,可用于判断字节数组的编码方式,使用jar介绍: https://code.google.com/archive/p/juniversalchardet/
 */
public class CharsetUtils {

    /**
     * 获取文件字节流的字符集编码
     *
     * @apiNote 注意: 此工具类的正确用法是读取文件的字节流然后进行判断,字节数组不会保存编码集!
     * @param bytes
     * @return
     */
    public String getEncoding(byte[] bytes){
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            throw new UnknownCharsetException();
        }
        return encoding;
    }



}
