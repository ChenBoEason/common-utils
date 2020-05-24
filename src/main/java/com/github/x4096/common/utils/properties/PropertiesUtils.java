package com.github.x4096.common.utils.properties;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/5/24 22:00
 * @description properties 工具类
 * @readme
 */
public class PropertiesUtils {

    private PropertiesUtils() {
    }

    public static String properties2Yaml(String propertiesStr) {
        if (StringUtils.isBlank(propertiesStr)) {
            return "";
        }

        JsonParser parser;
        JavaPropsFactory factory = new JavaPropsFactory();
        try {
            parser = factory.createParser(propertiesStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        YAMLGenerator generator = null;
        YAMLFactory yamlFactory = new YAMLFactory();
        try {
            StringWriter stringWriter = new StringWriter();
            generator = yamlFactory.createGenerator(stringWriter);

            JsonToken token = parser.nextToken();
            while (token != null) {
                if (JsonToken.START_OBJECT.equals(token)) {
                    generator.writeStartObject();
                } else if (JsonToken.FIELD_NAME.equals(token)) {
                    generator.writeFieldName(parser.getCurrentName());
                } else if (JsonToken.VALUE_STRING.equals(token)) {
                    generator.writeString(parser.getText());
                } else if (JsonToken.END_OBJECT.equals(token)) {
                    generator.writeEndObject();
                }
                token = parser.nextToken();
            }

            return beautifulStr(generator.getOutputTarget().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                parser.close();
            } catch (IOException e) {
                /*  */
            }

            if (null != generator) {
                try {
                    generator.flush();
                    generator.close();
                } catch (IOException e) {
                    /**/
                }
            }
        }
    }


    private static String beautifulStr(String originYamlStr) {
        StringBuilder sb = new StringBuilder(originYamlStr.length());
        String[] strings = originYamlStr.split("\n");
        String[] strArray;
        for (int i = 0, length = strings.length; i < length; i++) {
            if (i == 0) {
                continue;
            }

            if (strings[i].endsWith("\"true\"")) {
                strArray = strings[i].split(":");
                sb.append(strArray[0]).append(": ").append("true").append("\n");
            } else {
                sb.append(strings[i]).append("\n");
            }
        }

        return sb.toString();
    }

}
