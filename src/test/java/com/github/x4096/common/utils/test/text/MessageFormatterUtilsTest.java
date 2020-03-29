package com.github.x4096.common.utils.test.text;

import com.github.x4096.common.utils.test.pojo.Student;
import com.github.x4096.common.utils.text.StringFormatterUtils;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/2/7 16:05
 * @description
 * @readme
 */
public class MessageFormatterUtilsTest {

    public static void main(String[] args) {
        String formattingTuple = StringFormatterUtils.format("哈哈哈{}", "666");
        System.err.println(formattingTuple);

        Student student = new Student();
        student.setAge(10);

        formattingTuple = StringFormatterUtils.format("哈哈哈{}, {}, {}", student,"7777", new Exception("异常啊"));
        System.err.println(formattingTuple);
    }
}
