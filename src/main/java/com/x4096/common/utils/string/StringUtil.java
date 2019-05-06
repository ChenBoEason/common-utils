package com.x4096.common.utils.string;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: 字符串工具类
 */
public class StringUtil {

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    /**
     * 将以逗号分隔的字符串转换成字符串集合
     *
     * @param str
     * @return
     */
    public static List<String> splitToListString(String str) {
        if(StringUtils.isEmpty(str)){
            return Lists.newArrayList();
        }
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        return strList.stream().map(strItem -> (strItem)).collect(Collectors.toList());
    }

    /**
     * 随机生成32位字符串
     * @return
     */
    public static String randomUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 随机生成 8 位 字符串
     *
     * @return
     */
    public static String random8UUID() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = randomUUID();
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }


}
