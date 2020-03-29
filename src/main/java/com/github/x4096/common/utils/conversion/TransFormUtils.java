package com.github.x4096.common.utils.conversion;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-07-10 00:19
 * @Description: obj, map, xml 等互转
 */
public class TransFormUtils {

    private TransFormUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(TransFormUtils.class);

    private static XmlMapper xmlMapper = new XmlMapper();
    private static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 对象转map
     *
     * @param bean
     * @return
     */
    public static Map<String, String> beanToMap(Object bean) {
        Map<String, String> map;
        try {
            map = BeanUtils.describe(bean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("", e);
            return null;
        }
        map.remove("class");
        return map;
    }


    /**
     * map转对象
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, String> map, Class<T> clazz) {
        T bean = null;
        try {
            bean = clazz.newInstance();
            BeanUtils.populate(bean, map);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("", e);
        }
        return bean;
    }

    /**
     * 对象转JSON
     */
    public static String beanToJSON(Object bean) {
        String str = null;
        try {
            str = objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            logger.error("", e);
        }
        return str;
    }

    /**
     * JSON转对象
     *
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> clazz) {
        T bean = null;
        try {
            bean = clazz.newInstance();
            bean = objectMapper.readValue(json, clazz);
        } catch (InstantiationException | IllegalAccessException | IOException e) {
            logger.error("", e);
        }
        return bean;
    }

    /**
     * XML转JSON
     */
    public static String xmlToJSON(String xml) {
        StringWriter stringWriter = new StringWriter();
        JsonParser jsonParser;
        try {
            jsonParser = xmlMapper.getFactory().createParser(xml);
            JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
            while (jsonParser.nextToken() != null) {
                jsonGenerator.copyCurrentEvent(jsonParser);
            }
            jsonParser.close();
            jsonGenerator.close();
        } catch (Exception e) {
            logger.error("", e);
        }

        return stringWriter.toString();
    }

    /**
     * JSON转XML
     */
    public static String jsonToXml(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            String xml = xmlMapper.writeValueAsString(root);
            return xml;
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * 对象转XML
     */
    public static String beanToXml(Object bean) {
        XStream xStream = initXStream();
        xStream.alias("xml", bean.getClass());
        return xStream.toXML(bean);
    }

    /**
     * XML转对象
     */
    public static Object xmlToBean(String xml, Object bean) {
        XStream xStream = initXStream();
        xStream.alias("xml", bean.getClass());
        return xStream.fromXML(xml);
    }

    /**
     * 输入流转对象
     */
    public static Object inputStreamToBean(InputStream in, Object bean) {
        XStream xStream = initXStream();
        xStream.alias("xml", bean.getClass());
        return xStream.fromXML(in);
    }


    private static XStream initXStream() {
        return new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamReader createReader(Reader in) {
                return super.createReader(in);
            }

            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    @Override
                    public String encodeNode(String name) {
                        return name;
                    }

                    protected void writeText(QuickWriter writer, String text) {
                        writer.write("<![CDATA[" + text + "]]>");
                    }
                };
            }
        });
    }


}
