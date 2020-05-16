package com.github.x4096.common.utils.mapper;

import com.github.x4096.common.utils.reflect.AnnotationUtils;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.Validate;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-06 16:42
 * @Description: 使用Jaxb2.0实现XML<->Java Object的Mapper
 */
public class XmlMapperUtils {

    private XmlMapperUtils() {
    }

    private static final ConcurrentMap<Class, JAXBContext> jaxbContexts = new ConcurrentHashMap<>();

    private static final XStream xStream = new XStream();

    /**
     * Java Object->Xml 默认 UTF-8 编码
     */
    public static String toXml(Object object) {
        return toXml(object, true);
    }


    /**
     * Java Object->Xml 默认 UTF-8 编码
     *
     * @param object             Java对象
     * @param isGenerateDocument 是否生成文档描述,即 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     */
    public static String toXml(Object object, boolean isGenerateDocument) {
        return toXml(object, StandardCharsets.UTF_8.name(), isGenerateDocument, true);
    }


    /**
     * Java Object->Xml 默认 UTF-8 编码
     *
     * @param object             Java对象
     * @param isGenerateDocument 是否生成文档描述,即 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     * @param isFormat           是否格式化, 即生成的XML是否换行
     */
    public static String toXml(Object object, boolean isGenerateDocument, boolean isFormat) {
        return toXml(object, StandardCharsets.UTF_8.name(), isGenerateDocument, isFormat);
    }


    /**
     * Java Object->Xml with encoding
     *
     * @param object   Java对象
     * @param encoding 字符集编码
     */
    public static String toXml(Object object, String encoding) {
        return toXml(object, encoding, true, true);
    }


    /**
     * Java Object->Xml with encoding
     *
     * @param object             Java对象
     * @param encoding           字符集编码
     * @param isGenerateDocument 是否生成文档描述,即 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     * @param isFormat           是否格式化, 即生成的XML是否换行
     */
    public static String toXml(Object object, String encoding, boolean isGenerateDocument, boolean isFormat) {
        boolean isXmlRootElement = AnnotationUtils.isAnnotation(object.getClass(), XmlRootElement.class);
        if (isXmlRootElement) {
            // Class clazz = BQClassUtils.unwrapCglib(object); 没有必要进行代理的类处理
            return toXml(object, object.getClass(), encoding, isGenerateDocument, isFormat);
        } else {
            xStream.processAnnotations(object.getClass());
            return xStream.toXML(object);
        }
    }


    /**
     * Java Object->Xml with encoding
     *
     * @param object             需要生成XML的集合
     * @param clazz              处理的类
     * @param encoding           字符集编码
     * @param isGenerateDocument 是否生成文档描述,即 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     */
    public static String toXml(Object object, Class clazz, String encoding, boolean isGenerateDocument) {
        StringWriter writer = new StringWriter();
        try {
            createMarshaller(clazz, encoding, isGenerateDocument, true).marshal(object, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Java Object->Xml with encoding
     *
     * @param object             需要生成XML的集合
     * @param clazz              处理的类
     * @param encoding           字符集编码
     * @param isGenerateDocument 是否生成文档描述,即 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     * @param isFormat           是否格式化, 即生成的XML是否换行
     */
    public static String toXml(Object object, Class clazz, String encoding, boolean isGenerateDocument, boolean isFormat) {
        StringWriter writer = new StringWriter();
        try {
            createMarshaller(clazz, encoding, isGenerateDocument, isFormat).marshal(object, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形
     *
     * @param root     需要生成XML的集合
     * @param rootName 生成XML根节点名称
     * @param clazz    处理的类
     */
    public static String toXml(Collection<?> root, String rootName, Class clazz) {
        return toXml(root, rootName, clazz, StandardCharsets.UTF_8.name(), true, true);
    }


    /**
     * Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形
     *
     * @param root               需要生成XML的集合
     * @param rootName           生成XML根节点名称
     * @param clazz              处理的类
     * @param isGenerateDocument 是否生成文档描述,即 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     */
    public static String toXml(Collection<?> root, String rootName, Class clazz, boolean isGenerateDocument) {
        return toXml(root, rootName, clazz, StandardCharsets.UTF_8.name(), isGenerateDocument, true);
    }


    /**
     * Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形
     *
     * @param root     需要生成XML的集合
     * @param rootName 生成XML根节点名称
     * @param clazz    处理的类
     * @param encoding 字符集编码
     */
    public static String toXml(Collection<?> root, String rootName, Class clazz, String encoding) {
        return toXml(root, rootName, clazz, encoding, true, true);
    }


    /**
     * Java Collection->Xml with encoding, 特别支持Root Element是Collection的情形
     *
     * @param root               需要生成XML的集合
     * @param rootName           生成XML根节点名称
     * @param clazz              处理的类
     * @param encoding           字符集编码
     * @param isGenerateDocument 是否生成文档描述,即 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     */
    public static String toXml(Collection<?> root, String rootName, Class clazz, String encoding, boolean isGenerateDocument, boolean isFormat) {
        CollectionWrapper wrapper = new CollectionWrapper();
        wrapper.collection = root;

        JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<>(new QName(rootName), CollectionWrapper.class, wrapper);
        StringWriter writer = new StringWriter();

        try {
            createMarshaller(clazz, encoding, isGenerateDocument, isFormat).marshal(wrapperElement, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Xml->Java Object
     *
     * @param xml   XML
     * @param clazz 需要映射的对象
     */
    public static <T> T fromXml(String xml, Class<T> clazz) {
        return fromXml(xml, clazz, true);
    }


    /**
     * Xml->Java Object, 支持大小写敏感或不敏感
     *
     * @param xml           XML
     * @param clazz         需要映射的对象
     * @param caseSensitive 大小写是否敏感
     */
    public static <T> T fromXml(String xml, Class<T> clazz, boolean caseSensitive) {
        try {
            String fromXml = xml;
            if (!caseSensitive) {
                fromXml = xml.toLowerCase();
            }
            StringReader reader = new StringReader(fromXml);
            return (T) createUnmarshaller(clazz).unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 开放当前方法, 若无法满足你的转换, 你可以自定义设置 Marshaller
     *
     * @param clazz class
     */
    public static JAXBContext getJaxbContext(Class clazz) {
        Validate.notNull(clazz, "'clazz' must not be null");
        JAXBContext jaxbContext = jaxbContexts.get(clazz);
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(clazz, CollectionWrapper.class);
                jaxbContexts.putIfAbsent(clazz, jaxbContext);
            } catch (JAXBException ex) {
                throw new RuntimeException(
                        "Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
            }
        }
        return jaxbContext;
    }


    /**
     * 创建Marshaller并设定encoding(可为null).
     * 线程不安全，需要每次创建或pooling。
     */
    private static Marshaller createMarshaller(Class clazz, String encoding, boolean isGenerateDocument, boolean isFormat) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, isFormat);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            /* 是否移除 <?xml version="1.0" encoding="UTF-8" standalone="yes"?> */
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, !isGenerateDocument);
            return marshaller;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 创建UnMarshaller.
     * 线程不安全，需要每次创建或pooling。
     */
    private static Unmarshaller createUnmarshaller(Class clazz) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 封装Root Element 是 Collection的情况.
     */
    private static class CollectionWrapper {
        @XmlAnyElement
        protected Collection<?> collection;
    }

}
