package com.github.x4096.common.utils.network.http;

import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-29 23:21
 * @Description:
 */
public class HttpRequestHeaderUtils {


    public static Map<String, String> get(HttpServletRequest request, String key) {
        return getAll(request, false, "");
    }

    public static Map<String, String> getAll(HttpServletRequest request) {
        return getAll(request, true, "");
    }


    private static Map<String, String> getAll(HttpServletRequest request, boolean isAll, String key) {
        Map<String, String> headers = Maps.newHashMap();
        if (isAll) {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String element = headerNames.nextElement();
                headers.put(element, request.getHeader(element));
            }
        }

        return headers;
    }

}
