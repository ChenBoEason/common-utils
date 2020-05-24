package com.github.x4096.common.utils.test.properties;

import com.github.x4096.common.utils.properties.PropertiesUtils;
import com.github.x4096.common.utils.properties.YamlUtils;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/5/24 23:16
 * @description
 * @readme
 */
public class YamlUtilsTest {

    public static void main(String[] args) {
        String properties = YamlUtils.yaml2properties("logging:\n" +
                "  level:\n" +
                "    com:\n" +
                "      x4096:\n" +
                "        learn:\n" +
                "          mybatis:\n" +
                "            mapper: \"DEBUG\"\n" +
                "      wy:\n" +
                "        mybatis:\n" +
                "          generator:\n" +
                "            mapper: \"DEBUG\"\n" +
                "pagehelper:\n" +
                "  params: \"count=countSql\"\n" +
                "  supportMethodsArguments: true\n" +
                "  helperDialect: \"mysql\"\n" +
                "  reasonable: \"true\"");

        System.err.println(properties);

        System.err.println("\n------------\n");

        String yml = PropertiesUtils.properties2Yaml(properties);
        System.err.println(yml);
    }

}
