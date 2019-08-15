package com.x4096.common.utils.network.client;

import com.x4096.common.utils.charset.CharsetConstants;
import com.x4096.common.utils.common.ValidateUtils;
import com.x4096.common.utils.network.client.config.HttpAsyncConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 异步的HTTP请求对象，可设置代理
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: https://github.com/wsdl-king/AsyncHttpClientPool/blob/master/src/main/java/com/server/java/util/http/client/HttpAsyncClientUtils.java
 */
public class HttpAsyncClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAsyncClientUtils.class);


    private static final String DEFAULT_CHARSET = CharsetConstants.UTF_8;


    private static PoolingNHttpClientConnectionManager poolingNHttpClientConnectionManager;

    /**
     * 异步httpclient
     */
    private static CloseableHttpAsyncClient closeableHttpAsyncClient;


    /**
     * 初始化 HttpAsyncClientUtils
     *
     * @param httpAsyncConfig
     * @param proxy
     */
    public static void init(HttpAsyncConfig httpAsyncConfig, boolean proxy) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(httpAsyncConfig.getConnectionRequestTimeout())
                .setConnectTimeout(httpAsyncConfig.getConnectTimeout())
                .setSocketTimeout(httpAsyncConfig.getSocketTimeout()).build();

        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom().loadTrustMaterial(null,
                    new TrustSelfSignedStrategy())
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            LOGGER.error("HttpSyncClientUtils 初始化异常", e);
        }

        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                httpAsyncConfig.getProxyUsername(), httpAsyncConfig.getProxyPassword());

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
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            LOGGER.error("HttpAsyncClientUtils 初始化异常", e);
        }

        poolingNHttpClientConnectionManager = new PoolingNHttpClientConnectionManager(
                ioReactor, sessionStrategyRegistry);
        poolingNHttpClientConnectionManager.setMaxTotal(httpAsyncConfig.getPoolMaxSize());
        poolingNHttpClientConnectionManager.setDefaultMaxPerRoute(httpAsyncConfig.getMaxPerRoute());


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
        poolingNHttpClientConnectionManager.setDefaultConnectionConfig(connectionConfig);

        if (proxy) {
            closeableHttpAsyncClient = HttpAsyncClients.custom().setConnectionManager(poolingNHttpClientConnectionManager)
                    .setConnectionManagerShared(true)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                    .setProxy(new HttpHost(httpAsyncConfig.getProxyHost(), httpAsyncConfig.getProxyPort()))
                    .setDefaultCookieStore(new BasicCookieStore())
                    .setDefaultRequestConfig(requestConfig).build();
        } else {
            closeableHttpAsyncClient = HttpAsyncClients.custom().setConnectionManager(poolingNHttpClientConnectionManager)
                    .setConnectionManagerShared(true)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                    .setDefaultCookieStore(new BasicCookieStore())
                    .setDefaultRequestConfig(requestConfig).build();
        }

    }


    private static CloseableHttpAsyncClient getCloseableHttpAsyncClient() {
        return closeableHttpAsyncClient;
    }

    /**
     * 关闭资源
     *
     */
    public void close(){
        poolingNHttpClientConnectionManager.closeExpiredConnections();
        try {
            closeableHttpAsyncClient.close();
        } catch (IOException e) {
            LOGGER.error("关闭 HttpAsyncClientUtils 资源异常", e);
        }
    }


    /**
     * 异步 POST 请求
     *
     * @param requestUrl        请求地址
     * @param callback          回调方法
     */
    public static void post(String requestUrl, FutureCallback callback)  {
        post(requestUrl, "", null, callback);
    }

    /**
     * 异步 POST 请求
     *
     * @param requestUrl        请求地址
     * @param jsonString        请求参数 json 格式 exp: {"key1": "value1", "key2": "value2"}
     * @param callback          回调方法
     */
    public static void post(String requestUrl, String jsonString, FutureCallback callback)  {
        post(requestUrl, jsonString, null, callback);
    }


    /**
     * 异步 POST 请求
     *
     * @param requestUrl        请求地址
     * @param jsonString        请求参数 json 格式 exp: {"key1": "value1", "key2": "value2"}
     * @param requestHeader           http 请求头
     * @param callback          回调方法
     */
    public static void post(String requestUrl, String jsonString, Map<String, String> requestHeader, FutureCallback callback)  {
        if (StringUtils.isBlank(requestUrl)) {
            throw new NullPointerException("请求 URL 不能为空");
        }

        if(ValidateUtils.isNotUrl(requestUrl)){
            throw new IllegalArgumentException("请求 URL 格式错误");
        }

        HttpPost httpPost = buildHttpPost(requestUrl, null, jsonString, true, requestHeader );

        result(httpPost, callback);
    }


    /**
     * 异步 POST 请求
     *
     * @param requestUrl        请求地址
     * @param paramsMap         请求参数
     * @param callback          回调方法
     */
    public static void post(String requestUrl, Map<String, String> paramsMap, FutureCallback callback)  {
        post(requestUrl, paramsMap, null, callback);
    }

    /**
     * 异步 POST 请求
     *
     * @param requestUrl        请求地址
     * @param paramsMap         请求参数
     * @param requestHeader           http 请求头
     * @param callback          回调方法
     */
    public static void post(String requestUrl, Map<String, String> paramsMap, Map<String, String> requestHeader, FutureCallback callback)  {
        if (StringUtils.isBlank(requestUrl)) {
            throw new NullPointerException("请求 URL 不能为空");
        }

        if(ValidateUtils.isNotUrl(requestUrl)){
            throw new IllegalArgumentException("请求 URL 格式错误");
        }

        List<BasicNameValuePair> postBody = null;

        if(MapUtils.isNotEmpty(paramsMap)){
            postBody = new ArrayList<>(paramsMap.size());
            for(String key : paramsMap.keySet()){
                BasicNameValuePair basicNameValuePair = new BasicNameValuePair(key, paramsMap.get(key));
                postBody.add(basicNameValuePair);
            }
        }

        HttpPost httpPost = buildHttpPost(requestUrl, postBody, null, false, requestHeader);

        result(httpPost, callback);
    }



    /**
     * 异步 GET 请求
     *
     * @param requestUrl        请求 URL
     * @param callback          回调方法
     */
    public static void get(String requestUrl, FutureCallback callback) {
        get(requestUrl, null, null, callback);
    }

    /**
     * 异步 GET 请求
     *
     * @param requestUrl        请求 URL
     * @param urlParams         请求参数,格式: key1=value1&key2=value2
     * @param callback          回调方法
     */
    public static void get(String requestUrl, String urlParams, FutureCallback callback) {
        get(requestUrl, urlParams, null, callback);
    }


    /**
     * 异步 GET 请求
     *
     * @param requestUrl        请求 URL
     * @param urlParams         请求参数,格式: key1=value1&key2=value2
     * @param requestHeader           http 请求头
     * @param callback          回调方法
     */
    public static void get(String requestUrl, String urlParams, Map<String, String> requestHeader, FutureCallback callback)  {

        if (StringUtils.isBlank(requestUrl)) {
            throw new NullPointerException("请求 URL 不能为空");
        }

        if(ValidateUtils.isNotUrl(requestUrl)){
            throw new IllegalArgumentException("请求 URL 格式错误");
        }

        HttpGet httpGet = buildHttpGet(requestUrl, urlParams, requestHeader);

        result(httpGet, callback);
    }




    /**
     * 执行异步 POST 请求
     *
     * @param httpUriRequest
     * @param futureCallback
     */
    private static void result(HttpUriRequest httpUriRequest, FutureCallback futureCallback){
        CloseableHttpAsyncClient client = getCloseableHttpAsyncClient();
        client.start();
        client.execute(httpUriRequest, futureCallback);
    }


    /**
     * 构建 HttpPost
     *
     * @param requestUrl
     * @param postBody
     * @param jsonString
     * @param isJsonString
     * @param requestHeader
     * @return
     */
    private static HttpPost buildHttpPost(String requestUrl, List<BasicNameValuePair> postBody, String jsonString, boolean isJsonString, Map<String, String> requestHeader){
        HttpPost httpPost = new HttpPost(requestUrl);

        if (MapUtils.isNotEmpty(requestHeader)) {
            requestHeader.forEach((k, v) -> {
                httpPost.addHeader(k, v);
            });
        }

        if (CollectionUtils.isNotEmpty(postBody)) {
            UrlEncodedFormEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(postBody, DEFAULT_CHARSET);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("构建 HttpPost 异常", e);
            }
            httpPost.setEntity(entity);
        }


        if(isJsonString && StringUtils.isNotBlank(jsonString)){
            StringEntity entity = new StringEntity(jsonString, DEFAULT_CHARSET);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        }


        return httpPost;
    }



    /**
     * 构建 HttpGet
     *
     * @param requestUrl
     * @param requestHeader
     * @param urlParams
     * @return
     */
    private static HttpGet buildHttpGet(String requestUrl, String urlParams, Map<String, String> requestHeader){
        HttpGet httpGet = new HttpGet(requestUrl);

        if (MapUtils.isNotEmpty(requestHeader)) {
            requestHeader.forEach((k, v) -> {
                httpGet.addHeader(k, v);
            });
        }

        if (StringUtils.isNotBlank(urlParams)) {
            try {
                httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + urlParams));
            } catch (URISyntaxException e) {
                LOGGER.error("构建 HttpGet 异常", e);
            }
        }

        return httpGet;
    }

}