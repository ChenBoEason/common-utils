package com.github.x4096.common.utils.properties;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/5/16 22:22
 * @description yaml 工具类
 * @readme
 */
public class YamlUtils {

    private YamlUtils() {
    }

    /**
     * yaml 格式字符串转 Map
     *
     * @param yamlStr yml 格式字符串
     * @return properties Map
     */
    public static Map<String, String> yaml2Map(String yamlStr) {
        if (StringUtils.isBlank(yamlStr)) {
            return Maps.newHashMap();
        }

        Map<String, String> result = new LinkedHashMap<>();
        Yaml yaml = new Yaml();
        Map<String, Object> param = yaml.loadAs(yamlStr, LinkedHashMap.class);
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            forEachYaml(entry.getKey(), (Map<String, Object>) entry.getValue(), result);
        }
        return result;
    }


    /**
     * yaml 格式字符串转 properties
     *
     * @param yamlStr yml 格式字符串
     * @return properties 格式字符串
     */
    public static String yaml2properties(String yamlStr) {
        Map<String, String> stringMap = yaml2Map(yamlStr);
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }


    /**
     * 根据文件名获取yml的文件内容
     */
    public static Map<String, String> fileToMap(String file) {
        Objects.requireNonNull(file, "文件名不能为null");
        Map<String, String> result = new HashMap<>();

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
        Yaml props = new Yaml();
        Map<String, Object> param = props.loadAs(in, Map.class);
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            forEachYaml(entry.getKey(), (Map<String, Object>) entry.getValue(), result);
        }

        return result;
    }


    /**
     * 遍历yml文件，获取map集合
     */
    private static void forEachYaml(String keyStr, Map<String, Object> obj, Map<String, String> result) {
        String key;
        Object value;

        String keyName;
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();

            keyName = keyStr + "." + key;
            if (value instanceof Map) {
                forEachYaml(keyName, (Map<String, Object>) value, result);
            } else {
                result.put(keyName, Optional.ofNullable(value).orElse("").toString());
            }
        }
    }

}
