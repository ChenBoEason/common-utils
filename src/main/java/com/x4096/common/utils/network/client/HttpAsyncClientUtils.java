package com.x4096.common.utils.network.client;

import com.x4096.common.utils.network.client.config.HttpAsyncConfig;
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
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLContexts;
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
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.CodingErrorAction;
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


    private static final String DEFAULT_CHARSET = "UTF-8";


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

        SSLContext sslcontext = SSLContexts.createDefault();

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

        PoolingNHttpClientConnectionManager conMgr = new PoolingNHttpClientConnectionManager(
                ioReactor, sessionStrategyRegistry);

        if (httpAsyncConfig.getPoolMaxSize() > 0) {
            conMgr.setMaxTotal(httpAsyncConfig.getPoolMaxSize());
        }

        if (httpAsyncConfig.getMaxPerRoute() > 0) {
            conMgr.setDefaultMaxPerRoute(httpAsyncConfig.getMaxPerRoute());
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
            closeableHttpAsyncClient = HttpAsyncClients.custom().setConnectionManager(conMgr)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                    .setProxy(new HttpHost(httpAsyncConfig.getProxyHost(), httpAsyncConfig.getProxyPort()))
                    .setDefaultCookieStore(new BasicCookieStore())
                    .setDefaultRequestConfig(requestConfig).build();
        } else {
            closeableHttpAsyncClient = HttpAsyncClients.custom().setConnectionManager(conMgr)
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                    .setDefaultCookieStore(new BasicCookieStore()).build();
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
        try {
            closeableHttpAsyncClient.close();
        } catch (IOException e) {
            LOGGER.error("关闭 HttpAsyncClientUtils 资源异常", e);
        }
    }



    /**
     * 向指定的url发送一次异步post请求,参数是字符串
     * @param requestUrl    请求地址
     * @param postString 请求参数,格式是json.toString()
     * @param urlParams  请求参数,格式是String
     * @param callback   回调方法,格式是FutureCallback
     * @return 返回结果, 请求失败时返回null
     * @apiNote http接口处用 @RequestParam接收参数
     */
    public static void httpAsyncPost(String requestUrl, Map<String, String> headers, String postString,
                                     String urlParams, FutureCallback callback)  {

        if (StringUtils.isBlank(requestUrl)) {
            throw new NullPointerException("请求 URL 不能为空");
        }

        HttpPost httpPost = buildHttpPost(requestUrl, headers, postString, urlParams);

        postResult(httpPost, callback);
    }


    /**
     * 向指定的url发送一次异步post请求,参数是字符串
     *
     * @param requestUrl   请求地址
     * @param urlParams 请求参数,格式是List<BasicNameValuePair>
     * @param callback  回调方法,格式是FutureCallback
     * @return 返回结果, 请求失败时返回null
     * @apiNote http接口处用 @RequestParam接收参数
     */
    public static void httpAsyncPost(String requestUrl, Map<String, String> headers, List<BasicNameValuePair> postBody,
                                     List<BasicNameValuePair> urlParams, FutureCallback callback) {
        if (StringUtils.isBlank(requestUrl)) {
            throw new NullPointerException("请求 URL 不能为空");
        }

        HttpPost httpPost = buildHttpPost(requestUrl, headers, postBody, urlParams);

        postResult(httpPost, callback);
    }

    /**
     * 向指定的url发送一次异步get请求,参数是String
     *
     * @param requestUrl   请求地址
     * @param urlParams 请求参数,格式是String
     * @param callback  回调方法,格式是FutureCallback
     * @return 返回结果, 请求失败时返回null
     * @apiNote http接口处用 @RequestParam接收参数
     */
    public static void httpAsyncGet(String requestUrl, String urlParams, FutureCallback callback) {

        if ( StringUtils.isBlank(requestUrl)) {
            throw new NullPointerException("请求 URL 不能为空");
        }

        HttpGet httpGet = buildHttpGet(requestUrl, urlParams);
        getResult(httpGet, callback);
    }


    /**
     * 向指定的url发送一次异步get请求,参数是List<BasicNameValuePair>
     *
     * @param requestUrl   请求地址
     * @param urlParams 请求参数,格式是List<BasicNameValuePair>
     * @param callback  回调方法,格式是FutureCallback
     * @return 返回结果, 请求失败时返回null
     * @apiNote http接口处用 @RequestParam接收参数
     */
    public static void httpAsyncGet(String requestUrl, Map<String, String> headers, List<BasicNameValuePair> urlParams, FutureCallback callback)  {

        if (StringUtils.isBlank(requestUrl)) {
            throw new NullPointerException("请求 URL 不能为空");
        }

        HttpGet httpGet = buildHttpGet(requestUrl, headers, urlParams);

        getResult(httpGet, callback);
    }


    /**
     * 执行异步 POST 请求
     *
     * @param httpPost
     * @param futureCallback
     */
    private static void postResult(HttpPost httpPost, FutureCallback futureCallback){
        CloseableHttpAsyncClient client = getCloseableHttpAsyncClient();
        client.start();

        client.execute(httpPost, futureCallback);
    }

    /**
     *
     * @param httpGet
     * @param futureCallback
     */
    private static void getResult(HttpGet httpGet, FutureCallback futureCallback){
        CloseableHttpAsyncClient client = getCloseableHttpAsyncClient();
        client.start();

        client.execute(httpGet, futureCallback);
    }



    /**
     * 构建 HttpPost
     *
     * @param requestUrl
     * @param headers
     * @param postString
     * @param urlParams
     * @return
     */
    private static HttpPost buildHttpPost(String requestUrl, Map<String, String> headers, String postString,
                                  String urlParams){

        HttpPost httpPost = new HttpPost(requestUrl);

        if (null != headers) {
            for (String header : headers.keySet()) {
                httpPost.setHeader(header, headers.get(header));
            }
        }

        try {
            if (null != postString) {
                StringEntity entity = new StringEntity(postString, DEFAULT_CHARSET);
                entity.setContentEncoding(DEFAULT_CHARSET);
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }

            if (null != urlParams) {
                httpPost.setURI(new URI(httpPost.getURI().toString() + "?" + urlParams));
            }

        } catch (Exception e) {
            LOGGER.error("构建 HttpPost 异常", e);
        }

        return httpPost;
    }


    /**
     * 构建 HttpPost
     *
     * @param requestUrl
     * @param headers
     * @param postBody
     * @param urlParams
     * @return
     */
    private static HttpPost buildHttpPost(String requestUrl, Map<String, String> headers, List<BasicNameValuePair> postBody,
                                  List<BasicNameValuePair> urlParams){
        HttpPost httpPost = new HttpPost(requestUrl);

        if (null != headers) {
            for (String header : headers.keySet()) {
                httpPost.setHeader(header, headers.get(header));
            }
        }

        try {
            if (null != postBody) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                        postBody, DEFAULT_CHARSET);
                httpPost.setEntity(entity);
            }
            if (null != urlParams) {
                String getUrl = EntityUtils.toString(new UrlEncodedFormEntity(urlParams));
                httpPost.setURI(new URI(httpPost.getURI().toString() + "?" + getUrl));
            }

        } catch (Exception e) {
            LOGGER.error("构建 HttpPost 异常", e);
        }

        return httpPost;
    }


    /**
     * 构建 HttpGet
     *
     * @param requestUrl
     * @param urlParams
     * @return
     */
    private static HttpGet buildHttpGet(String requestUrl, String urlParams){

        HttpGet httpGet = new HttpGet(requestUrl);

        if (StringUtils.isNotBlank(urlParams)) {
            try {
                httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + urlParams));
            } catch (URISyntaxException e) {
                LOGGER.error("构建 HttpGet 异常", e);
            }
        } else {
            try {
                httpGet.setURI(new URI(httpGet.getURI().toString()));
            } catch (URISyntaxException e) {
                LOGGER.error("构建 HttpGet 异常", e);
            }
        }
        return httpGet;
    }

    /**
     * 构建 HttpGet
     *
     * @param requestUrl
     * @param headers
     * @param urlParams
     * @return
     */
    private static HttpGet buildHttpGet(String requestUrl, Map<String, String> headers, List<BasicNameValuePair> urlParams){

        HttpGet httpGet = new HttpGet(requestUrl);

        if (null != headers) {
            for (String header : headers.keySet()) {
                httpGet.setHeader(header, headers.get(header));
            }
        }

        if (null != urlParams) {

            String getUrl = null;
            try {
                getUrl = EntityUtils.toString(new UrlEncodedFormEntity(urlParams));
            } catch (IOException e) {
                LOGGER.error("构建 HttpGet 异常", e);
            }

            try {
                httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + getUrl));
            } catch (URISyntaxException e) {
                LOGGER.error("构建 HttpGet 异常", e);
            }
        }

        return httpGet;
    }

}