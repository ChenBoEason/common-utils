package com.x4096.common.utils.test.mapper;

import com.x4096.common.utils.mapper.JacksonMapperUtils;
import com.x4096.common.utils.test.pojo.Student;

import java.util.ArrayList;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-07 15:16
 * @Description:
 */
public class JacksonMapperUtilsTest {

    public static void main(String[] args) {
        Student student = new Student();
        student.setStringList(new ArrayList<>());
        System.err.println(JacksonMapperUtils.toJsonString(student, true));


    }
}
