package com.x4096.common.utils.test.mapper;

import com.x4096.common.utils.mapper.XmlMapperUtils;
import com.x4096.common.utils.test.pojo.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-07 14:36
 * @Description:
 */
public class XmlMapperUtilsTest {

    /**
     * 普通对象,即不使用相关注解
         <com.x4096.common.utils.test.pojo.Student>
         <username>我梦</username>
         <age>18</age>
         </com.x4096.common.utils.test.pojo.Student>
     */

    public static void main(String[] args) {
        Student student = new Student();
        student.setAge(18);
        student.setUsername("我梦");

        String xml = XmlMapperUtils.toXml(student, false);
        System.err.println(xml);


        List<Student> studentList = new ArrayList<>();
        studentList.add(student);

        Student student1 = new Student();
        student1.setAge(23);
        student1.setUsername("我梦23");
        studentList.add(student1);


        xml = XmlMapperUtils.toXml(studentList, "6666", Student.class);

        System.err.println(xml);

    }

}
