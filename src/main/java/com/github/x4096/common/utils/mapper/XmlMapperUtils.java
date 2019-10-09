package com.github.x4096.common.utils.mapper;

import com.github.x4096.common.utils.constant.CharsetConstants;
import com.github.x4096.common.utils.reflect.AnnotationUtils;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.Validate;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;
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

    private static ConcurrentMap<Class, JAXBContext> jaxbContexts = new ConcurrentHashMap<>();

    private static final XStream xStream = new XStream();

    /**
     * Java Object->Xml 默认 UTF-8 编码
     */
    public static String toXml(Object object) {
        return toXml(object, CharsetConstants.UTF_8, true);
    }


    /**
     * Java Object->Xml 默认 UTF-8 编码
     */
    public static String toXml(Object object, boolean generateDocument) {
        return toXml(object, CharsetConstants.UTF_8, generateDocument);
    }


    /**
     * Java Object->Xml with encoding.
     */
    public static String toXml(Object object, String encoding, boolean generateDocument) {
        boolean isXmlRootElement = AnnotationUtils.isAnnotation(object.getClass(), XmlRootElement.class);
        if (isXmlRootElement) {
            // Class clazz = ClassUtils.unwrapCglib(root); 没有必要进行代理类擦除
            return toXml(object, object.getClass(), encoding, generateDocument);
        } else {
            xStream.processAnnotations(object.getClass());
            return xStream.toXML(object);
        }
    }


    /**
     * Java Object->Xml with encoding.
     */
    public static String toXml(Object root, Class clazz, String encoding) {
        StringWriter writer = new StringWriter();
        try {
            createMarshaller(clazz, encoding, true).marshal(root, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Java Object->Xml with encoding.
     */
    public static String toXml(Object root, Class clazz, String encoding, boolean generateDocument) {
        StringWriter writer = new StringWriter();
        try {
            createMarshaller(clazz, encoding, generateDocument).marshal(root, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形.
     */
    public static String toXml(Collection<?> root, String rootName, Class clazz) {
        return toXml(root, rootName, clazz, CharsetConstants.UTF_8, true);
    }


    /**
     * Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形.
     */
    public static String toXml(Collection<?> root, String rootName, Class clazz, boolean generateDocument) {
        return toXml(root, rootName, clazz, CharsetConstants.UTF_8, generateDocument);
    }


    /**
     * Java Collection->Xml with encoding, 特别支持Root Element是Collection的情形.
     */
    public static String toXml(Collection<?> root, String rootName, Class clazz, String encoding, boolean generateDocument) {
        CollectionWrapper wrapper = new CollectionWrapper();
        wrapper.collection = root;

        JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<>(new QName(rootName), CollectionWrapper.class, wrapper);
        StringWriter writer = new StringWriter();

        try {
            createMarshaller(clazz, encoding, generateDocument).marshal(wrapperElement, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Xml->Java Object.
     */
    public static <T> T fromXml(String xml, Class<T> clazz) {
        return fromXml(xml, clazz, true);
    }


    /**
     * Xml->Java Object, 支持大小写敏感或不敏感.
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
     * 创建Marshaller并设定encoding(可为null).
     * 线程不安全，需要每次创建或pooling。
     * generateDocument 是否生成文档描述（默认生成）即 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     */
    private static Marshaller createMarshaller(Class clazz, String encoding, boolean generateDocument) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            /* 是否移除 <?xml version="1.0" encoding="UTF-8" standalone="yes"?> */
            if (!generateDocument) {
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            }
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


    private static JAXBContext getJaxbContext(Class clazz) {
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
     * 封装Root Element 是 Collection的情况.
     */
    private static class CollectionWrapper {
        @XmlAnyElement
        protected Collection<?> collection;
    }

}
