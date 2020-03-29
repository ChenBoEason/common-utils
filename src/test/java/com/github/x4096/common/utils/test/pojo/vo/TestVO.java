package com.github.x4096.common.utils.test.pojo.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/2/12 22:27
 * @description
 * @readme
 */
@Data
public class TestVO {

    private String username;

    private String age;

    private Map<String, String> stringMap = new HashMap<>();

}
