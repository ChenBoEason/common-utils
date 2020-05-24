package com.github.x4096.common.utils.test.mapper;

import com.alibaba.fastjson.JSON;
import com.github.x4096.common.utils.mapper.BeanMapperUtils;
import com.github.x4096.common.utils.mapper.XmlMapperUtils;
import com.github.x4096.common.utils.test.pojo.Student;
import com.github.x4096.common.utils.test.pojo.dto.TestDTO;
import com.github.x4096.common.utils.test.pojo.vo.TestVO;
import com.github.x4096.common.utils.text.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/2/12 22:28
 * @description
 * @readme
 */
public class BeanMapperUtilsTest {

    public static void main(String[] args) {
        TestDTO testDTO = new TestDTO();
        testDTO.setAge(18);
        testDTO.setUsername("0x4096");

        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("key", "value");

        testDTO.setStringMap(stringMap);

        long startTime = Clock.systemUTC().millis();

        TestVO testVO = BeanMapperUtils.copyProperties(testDTO, TestVO.class);

        long end = Clock.systemUTC().millis();

        System.err.println(end - startTime);
        System.err.println(JSON.toJSONString(testVO));


        startTime = Clock.systemUTC().millis();
        TestVO testVO2 = new TestVO();
        BeanUtils.copyProperties(testDTO, testVO2);

        end = Clock.systemUTC().millis();

        System.err.println(end - startTime);
        System.err.println(JSON.toJSONString(testVO2));


        System.err.println(RandomStringUtils.uuid32());
    }


    @Test
    public void t1() {
        Student student = new Student();
        student.setUsername("name");
        student.setAge(1);


        String xml = XmlMapperUtils.toXml(student, false, false);

        System.err.println(xml);

        String json = BeanMapperUtils.xmlToJSON(xml);

        System.err.println(json);

    }

}
