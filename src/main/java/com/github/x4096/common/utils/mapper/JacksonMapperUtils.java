package com.github.x4096.common.utils.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-09-14 00:57
 * @Description: 参考来源: rocketmq-console
 */
public class JacksonMapperUtils {

    private JacksonMapperUtils() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonMapperUtils.class);
    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();
    private static final ObjectMapper MAPPER = new ObjectMapper();


    static {
        DEFAULT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        DEFAULT_MAPPER.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        DEFAULT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        DEFAULT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        DEFAULT_MAPPER.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
        DEFAULT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        DEFAULT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    static {
        DEFAULT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    /**
     * 对象 -> jsonString
     * 如果字段内容为 null 则不序列化
     * T 可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]"
     */
    public static <T> String toJsonString(T t) {
        return toJsonString(t, DEFAULT_MAPPER);
    }


    /**
     * 对象 -> jsonString
     * 序列化所有字段,包括字段内容为(isNonNull false控制) null
     * T 可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
     */
    public static <T> String toJsonString(T t, boolean isNonNull) {
        if (isNonNull) {
            return toJsonString(t);
        }
        return toJsonString(t, MAPPER);
    }


    /**
     * 反序列化POJO或简单Collection如List<String>.
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, JavaType)
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return fromJson(jsonString, clazz, null);
    }


    /**
     * 反序列化复杂Collection如List<Bean>, contructCollectionType()或contructMapType()构造类型, 然后调用本函数.
     */
    public static <T> T fromJson(String jsonString, JavaType javaType) {
        return fromJson(jsonString, null, javaType);
    }


    private static <T> T fromJson(String jsonString, Class<T> clazz, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        if (clazz == null && javaType == null) {
            return (T) new NullPointerException("clazz 和 javaType 不能同时为 null");
        }

        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonString, javaType);
            } else {
                return MAPPER.readValue(jsonString, clazz);
            }
        } catch (IOException e) {
            LOGGER.error("解析json异常,请求入参: " + jsonString, e);
            return null;
        }
    }


    public static <T> String toJsonString(T t, ObjectMapper objectMapper) {
        if (t == null || objectMapper == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            LOGGER.error("json转换异常", e);
            return null;
        }
    }


    public static void writeValue(Writer writer, Object obj) {
        try {
            DEFAULT_MAPPER.writeValue(writer, obj);
        } catch (IOException e) {
            Throwables.throwIfUnchecked(e);
        }
    }

    public static <T> String obj2String(T src) {
        if (src == null) {
            return null;
        }

        try {
            return src instanceof String ? (String) src : DEFAULT_MAPPER.writeValueAsString(src);
        } catch (Exception e) {
            LOGGER.error("Parse Object to String error src=" + src, e);
            return null;
        }
    }

    public static <T> byte[] obj2Byte(T src) {
        if (src == null) {
            return null;
        }

        try {
            return src instanceof byte[] ? (byte[]) src : DEFAULT_MAPPER.writeValueAsBytes(src);
        } catch (Exception e) {
            LOGGER.error("Parse Object to byte[] error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (Strings.isNullOrEmpty(str) || clazz == null) {
            return null;
        }
        str = escapesSpecialChar(str);
        try {
            return clazz.equals(String.class) ? (T) str : DEFAULT_MAPPER.readValue(str, clazz);
        } catch (Exception e) {
            LOGGER.error("Parse String to Object error\nString: {}\nClass<T>: {}\nError: {}", str, clazz.getName(), e);
            return null;
        }
    }

    public static <T> T byte2Obj(byte[] bytes, Class<T> clazz) {
        if (bytes == null || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(byte[].class) ? (T) bytes : DEFAULT_MAPPER.readValue(bytes, clazz);
        } catch (Exception e) {
            LOGGER.error("Parse byte[] to Object error\nbyte[]: {}\nClass<T>: {}\nError: {}", bytes, clazz.getName(), e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (Strings.isNullOrEmpty(str) || typeReference == null) {
            return null;
        }
        str = escapesSpecialChar(str);
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : DEFAULT_MAPPER.readValue(str, typeReference));
        } catch (Exception e) {
            LOGGER.error("Parse String to Object error\nString: {}\nTypeReference<T>: {}\nError: {}", str,
                    typeReference.getType(), e);
            return null;
        }
    }

    public static <T> T byte2Obj(byte[] bytes, TypeReference<T> typeReference) {
        if (bytes == null || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(byte[].class) ? bytes : DEFAULT_MAPPER.readValue(bytes,
                    typeReference));
        } catch (Exception e) {
            LOGGER.error("Parse byte[] to Object error\nbyte[]: {}\nTypeReference<T>: {}\nError: {}", bytes,
                    typeReference.getType(), e);
            return null;
        }
    }

    public static <T> T map2Obj(Map<String, String> map, Class<T> clazz) {
        String str = obj2String(map);
        return string2Obj(str, clazz);
    }

    private static String escapesSpecialChar(String str) {
        return str.replace("\n", "\\n").replace("\r", "\\r");
    }


}
