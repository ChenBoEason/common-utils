package com.github.x4096.common.utils.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/2/2 16:36
 * @description
 * @readme
 */
public class BeanMapperUtils {

    private BeanMapperUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(BeanMapperUtils.class);

    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    private static final XmlMapper xmlMapper = new XmlMapper();

    private static final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * XML转JSON
     *
     * @param xml xml
     * @return XML -> JSON
     * @apiNote 若发生异常, 则返回 null
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
            return null;
        }

        return stringWriter.toString();
    }


    /**
     * JSON转XML
     *
     * @param jsonStr jsonStr
     * @return JSON转XML
     * @apiNote 若发生异常, 则返回 null
     */
    public static String jsonToXml(String jsonStr) {
        try {
            JsonNode root = objectMapper.readTree(jsonStr);
            return xmlMapper.writeValueAsString(root);
        } catch (IOException e) {
            logger.error("", e);
        }

        return null;
    }


    /**
     * 简单的复制出新类型对象.
     */
    public static <S, D> D copyProperties(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }


    /**
     * 简单的复制出新对象ArrayList
     */
    public static <S, D> List<D> copyProperties(Iterable<S> sourceList, Class<D> destinationClass) {
        if (null == sourceList) {
            return Collections.emptyList();
        }

        Preconditions.checkNotNull(destinationClass, "目标对象不能为 null");

        List<D> destinationList = new ArrayList<>();
        for (S source : sourceList) {
            if (source != null) {
                destinationList.add(mapper.map(source, destinationClass));
            }
        }
        return destinationList;
    }

    /**
     * 简单复制出新对象数组
     */
    // public static <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass) {
    //     D[] destinationArray = (D[]) Array.newInstance(destinationClass, sourceArray.length);
    //
    //     int i = 0;
    //     for (S source : sourceArray) {
    //         if (source != null) {
    //             destinationArray[i] = mapper.map(sourceArray[i], destinationClass);
    //             i++;
    //         }
    //     }
    //
    //     return destinationArray;
    // }

}
