package com.github.x4096.common.utils.network.http.config;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019/4/22 13:21
 * @Description: 异步HTTP请求初始化配置
 */
public class HttpAsyncConfig {

    /**
     * 设置等待数据超时时间,单位毫秒,默认5秒
     */
    private int socketTimeout = 5_000;

    /**
     * 连接超时,单位毫秒,默认5秒
     */
    private int connectTimeout = 5_000;

    /**
     * 连接池最大连接数,默认100
     */
    private int poolMaxSize = 100;

    /**
     * 每个主机的并发最大数,默认200
     */
    private int maxPerRoute = 200;

    /**
     * 从连接池中后去连接的timeout时间,单位毫秒,默认5秒
     */
    private int connectionRequestTimeout = 5_000;

    /**
     * http代理相关参数 主机
     */
    private String proxyHost;

    /**
     * 端口
     */
    private int proxyPort;

    /**
     * 用户名
     */
    private String proxyUsername;

    /**
     * 密码
     */
    private String proxyPassword;

    public HttpAsyncConfig() {
    }

    public HttpAsyncConfig(int socketTimeout, int connectTimeout, int poolMaxSize, int maxPerRoute, int connectionRequestTimeout) {
        this.socketTimeout = socketTimeout;
        this.connectTimeout = connectTimeout;
        this.poolMaxSize = poolMaxSize;
        this.maxPerRoute = maxPerRoute;
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public HttpAsyncConfig(int socketTimeout, int connectTimeout, int poolMaxSize, int maxPerRoute, int connectionRequestTimeout, String proxyHost, int proxyPort, String proxyUsername, String proxyPassword) {
        this.socketTimeout = socketTimeout;
        this.connectTimeout = connectTimeout;
        this.poolMaxSize = poolMaxSize;
        this.maxPerRoute = maxPerRoute;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getPoolMaxSize() {
        return poolMaxSize;
    }

    public void setPoolMaxSize(int poolMaxSize) {
        this.poolMaxSize = poolMaxSize;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    @Override
    public String toString() {
        return "HttpAsyncConfig{" +
                "socketTimeout=" + socketTimeout +
                ", connectTimeout=" + connectTimeout +
                ", poolMaxSize=" + poolMaxSize +
                ", maxPerRoute=" + maxPerRoute +
                ", connectionRequestTimeout=" + connectionRequestTimeout +
                ", proxyHost='" + proxyHost + '\'' +
                ", proxyPort=" + proxyPort +
                ", proxyUsername='" + proxyUsername + '\'' +
                ", proxyPassword='" + proxyPassword + '\'' +
                '}';
    }

}
