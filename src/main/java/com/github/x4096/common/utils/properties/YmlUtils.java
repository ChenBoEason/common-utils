package com.github.x4096.common.utils.properties;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/5/16 22:22
 * @description yml 工具类
 * @readme
 */
public class YmlUtils {


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
