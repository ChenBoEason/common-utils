package com.github.x4096.common.utils.network.http.config;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/4/22 13:32
 * @Description: 同步HTTP请求配置
 */
public class HttpSyncConfig {

    /**
     * 连接池最大连接数,默认100
     */
    private int maxTotalPool = 100;

    /**
     * 每个主机的并发最大数,默认20
     */
    private int maxConPerRoute = 20;

    /**
     * 设置等待数据超时时间默认5秒钟,单位毫秒
     */
    private int socketTimeout = 5_000;

    /**
     * 连接池取连接的超时时间,单位毫秒,默认5秒
     */
    private int connectionRequestTimeout = 5_000;

    /**
     * 连接超时,单位毫秒,默认5秒
     */
    private int connectTimeout = 5_000;

    public HttpSyncConfig() {
    }

    public HttpSyncConfig(int maxTotalPool, int maxConPerRoute, int socketTimeout, int connectionRequestTimeout, int connectTimeout) {
        this.maxTotalPool = maxTotalPool;
        this.maxConPerRoute = maxConPerRoute;
        this.socketTimeout = socketTimeout;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.connectTimeout = connectTimeout;
    }

    public int getMaxTotalPool() {
        return maxTotalPool;
    }

    public void setMaxTotalPool(int maxTotalPool) {
        this.maxTotalPool = maxTotalPool;
    }

    public int getMaxConPerRoute() {
        return maxConPerRoute;
    }

    public void setMaxConPerRoute(int maxConPerRoute) {
        this.maxConPerRoute = maxConPerRoute;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @Override
    public String toString() {
        return "HttpSyncConfig{" +
                "maxTotalPool=" + maxTotalPool +
                ", maxConPerRoute=" + maxConPerRoute +
                ", socketTimeout=" + socketTimeout +
                ", connectionRequestTimeout=" + connectionRequestTimeout +
                ", connectTimeout=" + connectTimeout +
                '}';
    }

}
