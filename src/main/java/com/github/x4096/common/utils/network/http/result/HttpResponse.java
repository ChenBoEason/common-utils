package com.github.x4096.common.utils.network.http.result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Map;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-07 12:01
 * @Description: http请求结果封装
 */
public class HttpResponse {

    /**
     * 使用协议 exp: HTTP/1.1
     */
    private String protocol;

    /**
     * 状态码 200 404 500  -1 表示请求失败,失败原因可参考 reasonPhrase
     */
    private int statusCode;

    /**
     * 原因与statusCode相关 exp: statusCode为404时 reasonPhrase为 Not Found
     */
    private String reasonPhrase;

    /**
     * 响应体内容
     */
    private String body;

    /**
     * 响应体类型
     */
    private String contentType;

    /**
     * 响应头
     */
    private Map<String, String> headers;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}
