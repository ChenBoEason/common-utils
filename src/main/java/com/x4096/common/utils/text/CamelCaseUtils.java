package com.x4096.common.utils.text;

import org.apache.commons.lang3.StringUtils;


/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-06 23:11
 * @Description: 驼峰转换工具类
 */
public class CamelCaseUtils {

    /**
     * 蛇形命名法(snake case) -> 驼峰命名法(camel case)字符转换
     * hello_world -> helloWorld
     * hi_hello_world -> hiHelloWorld
     * hello_ -> hello
     * _hello_ -> hello
     *
     * @param snakeCase
     * @return
     */
    public static String snake2Camel(String snakeCase) {
        if (StringUtils.isBlank(snakeCase)) {
            return "";
        }
        int index = StringUtils.indexOf(snakeCase, "_");
        if (index != -1) {
            StringBuilder sb = new StringBuilder();
            String[] strings = StringUtils.split(snakeCase, "_");
            for (int i = 0, length = strings.length; i < length; i++) {
                if (i == 0) {
                    sb.append(strings[0]);
                } else {
                    sb.append(toUpperCaseFristChar(strings[i]));
                }
            }
            return sb.toString();
        }
        return snakeCase;
    }


    /**
     * 驼峰命名法 -> 蛇形命名法
     * helloWorld -> hello_world
     * hiHelloWorld -> hi_hello_world
     * Hello -> hello
     *
     * @param camelCase
     * @return
     */
    public static String camel2Snake(String camelCase) {
        if (StringUtils.isBlank(camelCase)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        char[] chars = camelCase.toCharArray();
        for (int i = 0, length = chars.length; i < length; i++) {
            char c = chars[i];
            if (c >= 'A' && c <= 'Z') {
                if (i == 0) {
                    sb.append((char) (c + 32));
                } else {
                    sb.append("_").append((char) (c + 32));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        System.err.println(snake2Camel("_hello_"));
        System.err.println(camel2Snake("Hello"));

    }

    /**
     * 将字符串首字母转换为大写
     * hello -> Hello
     *
     * @param string
     * @return
     */
    public static String toUpperCaseFristChar(String string) {
        char[] charArray = string.toCharArray();
        charArray[0] -= 32;
        return String.valueOf(charArray);
    }


    /**
     * 将首字母转换为小写
     * Hello -> hello
     *
     * @param string
     * @return
     */
    public static String toLowerCaseFristChar(String string) {
        char[] charArray = string.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }

}
