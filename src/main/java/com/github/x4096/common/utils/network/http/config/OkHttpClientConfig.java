package com.github.x4096.common.utils.network.http.config;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-09 13:16
 * @Description:
 */
public class OkHttpClientConfig {

    /**
     * 最大闲置连接数
     */
    private int maxIdleConnections = 5;

    /**
     * keepAlive 时间 单位分钟
     */
    private long keepAliveTime = 5;

    /**
     * 连接超时时间 单位毫秒
     */
    private int connectTimeout = 5_000;

    /**
     * 读取超时时间 单位毫秒
     */
    private int readTimeout = 5_000;

    /**
     * 写 超时时间 单位毫秒
     */
    private int writeTimeout = 5_000;




}
