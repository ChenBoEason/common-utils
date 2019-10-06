package com.x4096.common.utils.test.string;

import com.x4096.common.utils.mapper.JacksonMapperUtils;
import com.x4096.common.utils.test.Student;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-09-14 01:08
 * @Description:
 */
public class JSONUtilsTest {


    public static void main(String[] args) {
        Student student = new Student();
        student.setAge(18);
        student.setUsername("0x4096");

        String res = JacksonMapperUtils.obj2String(student);

        System.err.println(res);

        student = JacksonMapperUtils.string2Obj(res, Student.class);

        System.err.println(student);

    }



}
