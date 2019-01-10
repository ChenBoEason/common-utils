package com.github.common.utils.network.client;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.auth.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.nio.charset.CodingErrorAction;

/**
 * 异步的HTTP请求对象，可设置代理
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: https://github.com/wsdl-king/AsyncHttpClientPool/blob/master/src/main/java/com/server/java/util/http/client/HttpAsyncClient.java
 */
public class HttpAsyncClient implements InitializingBean {

    /**
     * 设置等待数据超时时间,单位秒,默认5秒
     */
    @Value("${http.async.socketTimeout:5}")
    private Integer socketTimeout;

    /**
     * 连接超时,单位秒,默认5秒
     */
    @Value("${http.async.connectTimeout:5}")
    private Integer connectTimeout;

    /**
     * 连接池最大连接数,默认100
     */
    @Value("${http.async.poolMaxSize:100}")
    private Integer poolMaxSize;

    /**
     * 每个主机的并发最大数,默认2000
     */
    @Value("${http.async.maxPerRoute:2000}")
    private Integer maxPerRoute;

    /**
     * 从连接池中后去连接的timeout时间,单位秒,默认5秒
     */
    @Value("${http.async.connectionRequestTimeout:5}")
    private Integer connectionRequestTimeout;

    /**
     * http代理相关参数
     */

    @Value("${http.proxy.host}")
    private String host;

    /**
     * 端口
     */
    @Value("${http.proxy.port}")
    private Integer port;

    /**
     * 用户名
     */
    @Value("${http.proxy.username}")
    private String username;

    /**
     * 密码
     */
    @Value("${http.proxy.password}")
    private String password;

    /**
     * 异步httpclient
     */
    private CloseableHttpAsyncClient asyncHttpClient;

    public HttpAsyncClient(){
        System.out.println("HttpAsyncClient初始化...");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.asyncHttpClient = createAsyncClient(false);
    }

    private CloseableHttpAsyncClient createAsyncClient(boolean proxy)throws Exception {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout).build();

        SSLContext sslcontext = SSLContexts.createDefault();

        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                username, password);

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        /**
         * 设置协议http和https对应的处理socket链接工厂的对象
         */
        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder
                .<SchemeIOSessionStrategy>create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", new SSLIOSessionStrategy(sslcontext))
                .build();

        /**
         * 配置io线程
         */
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setSoKeepAlive(false).setTcpNoDelay(true)
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .build();
        /**
         * 设置连接池大小
         */
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        PoolingNHttpClientConnectionManager conMgr = new PoolingNHttpClientConnectionManager(
                ioReactor, sessionStrategyRegistry);

        if (poolMaxSize > 0) {
            conMgr.setMaxTotal(poolMaxSize);
        }

        if (maxPerRoute > 0) {
            conMgr.setDefaultMaxPerRoute(maxPerRoute);
        } else {
            conMgr.setDefaultMaxPerRoute(10);
        }

        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8).build();

        Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder
                .<AuthSchemeProvider>create()
                .register(AuthSchemes.BASIC, new BasicSchemeFactory())
                .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
                .register(AuthSchemes.NTLM, new NTLMSchemeFactory())
                .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
                .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
                .build();
        conMgr.setDefaultConnectionConfig(connectionConfig);

        if (proxy) {
            return HttpAsyncClients.custom().setConnectionManager(conMgr)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                    .setProxy(new HttpHost(host, port))
                    .setDefaultCookieStore(new BasicCookieStore())
                    .setDefaultRequestConfig(requestConfig).build();
        } else {
            return HttpAsyncClients.custom().setConnectionManager(conMgr)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                    .setDefaultCookieStore(new BasicCookieStore()).build();
        }

    }


    public CloseableHttpAsyncClient getAsyncHttpClient() {
        return asyncHttpClient;
    }


    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getPoolMaxSize() {
        return poolMaxSize;
    }

    public void setPoolMaxSize(Integer poolMaxSize) {
        this.poolMaxSize = poolMaxSize;
    }

    public Integer getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(Integer maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public Integer getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAsyncHttpClient(CloseableHttpAsyncClient asyncHttpClient) {
        this.asyncHttpClient = asyncHttpClient;
    }
}
