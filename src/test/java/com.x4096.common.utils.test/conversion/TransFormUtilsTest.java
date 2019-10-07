package com.x4096.common.utils.test.conversion;

import com.x4096.common.utils.conversion.TransFormUtils;
import com.x4096.common.utils.test.pojo.Student;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-09-14 01:25
 * @Description:
 */
public class TransFormUtilsTest {

    public static void main(String[] args) {
        Student student = new Student();
        student.setAge(18);
        student.setUsername("0x4096");

        String res = TransFormUtils.beanToXml(student);
        System.err.println(res);

    }
}
