package com.github.x4096.common.utils.text;

import com.github.x4096.common.utils.constant.CharsetConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-06 16:50
 * @Description: 转义工具集 1. URL 转义 2. html 转义 3. xml 转义
 */
public class EscapeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EscapeUtils.class);

    /**
     * URL 编码 默认字符集编码 UTF-8
     */
    public static String urlEncode(String url) {
        return urlEncodeOrDecode(url, true);
    }


    /**
     * URL 解码 默认字符集编码 UTF-8
     */
    public static String urlDecode(String url) {
        return urlEncodeOrDecode(url, false);
    }


    /**
     * Xml转码，将字符串转码为符合XML1.1格式的字符串.
     * 比如 "bread" & "butter" 转化为 &quot;bread&quot; &amp; &quot;butter&quot;
     */
    public static String escapeXml(String xml) {
        return StringEscapeUtils.escapeXml11(xml);
    }


    /**
     * Xml转码，XML格式的字符串解码为普通字符串.
     * 比如 &quot;bread&quot; &amp; &quot;butter&quot; 转化为"bread" & "butter"
     */
    public static String unescapeXml(String xml) {
        return StringEscapeUtils.unescapeXml(xml);
    }


    /**
     * Html转码，将字符串转码为符合HTML4格式的字符串.
     * 比如 "bread" & "butter" 转化为 &quot;bread&quot; &amp; &quot;butter&quot;
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }


    /**
     * Html解码，将HTML4格式的字符串转码解码为普通字符串.
     * 比如 &quot;bread&quot; &amp; &quot;butter&quot;转化为"bread" & "butter"
     */
    public static String unescapeHtml(String html) {
        return StringEscapeUtils.unescapeHtml4(html);
    }


    private static String urlEncodeOrDecode(String url, boolean isEncode) {
        if (StringUtils.isBlank(url)) {
            return "";
        }

        if (isEncode) {
            try {
                return URLEncoder.encode(url, CharsetConstants.UTF_8);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("url编码异常,请求入参: " + url, e);
                return null;
            }
        }
        try {
            return URLDecoder.decode(url, CharsetConstants.UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("url解密异常,请求入参: " + url, e);
            return null;
        }
    }

}
