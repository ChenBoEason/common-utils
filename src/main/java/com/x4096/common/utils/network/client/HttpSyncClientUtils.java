package com.x4096.common.utils.network.client;

import com.x4096.common.utils.network.client.config.HttpSyncConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * 同步的HTTP请求对象，支持post与get方法以及可设置代理
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: 参考代码地址: https://github.com/wsdl-king/AsyncHttpClientPool/blob/master/src/main/java/com/server/java/util/http/client/HttpSyncClient.java
 */
public class HttpSyncClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSyncClientUtils.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static PoolingHttpClientConnectionManager poolConnManager;

    private static CloseableHttpClient closeableHttpClient;


    /**
     * 初始化 httpClient
     *
     * @param httpSyncConfig
     */
    public static void init(HttpSyncConfig httpSyncConfig) {
        SSLContext sslcontext = null;

        try {
            sslcontext = SSLContexts.custom().loadTrustMaterial(null,
                    new TrustSelfSignedStrategy())
                    .build();
        } catch (NoSuchAlgorithmException |KeyManagementException | KeyStoreException e) {
            LOGGER.error("HttpSyncClientUtils 初始化异常", e);
        }

        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslsf)
                .build();

        poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolConnManager.setMaxTotal(httpSyncConfig.getMaxTotalPool());
        poolConnManager.setDefaultMaxPerRoute(httpSyncConfig.getMaxConPerRoute());

        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(httpSyncConfig.getSocketTimeout()).build();
        poolConnManager.setDefaultSocketConfig(socketConfig);

        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(httpSyncConfig.getConnectionRequestTimeout())
                .setConnectTimeout(httpSyncConfig.getConnectTimeout()).setSocketTimeout(httpSyncConfig.getSocketTimeout()).build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolConnManager).setDefaultRequestConfig(requestConfig).build();

        if (poolConnManager != null && poolConnManager.getTotalStats() != null) {
            LOGGER.info("now client pool " + poolConnManager.getTotalStats().toString());
        }
        closeableHttpClient = httpClient;
    }


    /**
     * 关闭资源
     *
     */
    public static void close(){
        poolConnManager.close();
    }

    private static CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }


    /**
     * 同步post请求 向指定的url发送一次post请求,参数是List<NameValuePair>
     * @param requestUrl 请求地址
     * @param paramsList    请求参数,格式是List<NameValuePair>
     * @return 返回结果, 请求失败时返回null
     * @apiNote http接口处用 @RequestParam接收参数
     */
    public static String httpSyncPost(String requestUrl, List<BasicNameValuePair> paramsList) {

        if(StringUtils.isBlank(requestUrl)){
            throw new NullPointerException("baseUrl不能为空");
        }
        if(paramsList == null){
            throw new NullPointerException("请求参数不能为null");
        }

        HttpPost httpPost = new HttpPost(requestUrl);

        return postResult(httpPost);
    }


    /**
     * 同步POST请求 向指定的url发送一次post请求,参数是json格式字符串
     *
     * @param requestUrl    请求地址
     * @param jsonString 请求参数,格式是json
     * @return 返回结果, 请求失败时返回null
     */
    public static String httpSyncPost(String requestUrl, String jsonString) {

        if(StringUtils.isBlank(requestUrl) || StringUtils.isBlank(jsonString)){
            throw new NullPointerException("baseUrl或jsonString不能为空");
        }

        HttpPost httpPost = new HttpPost(requestUrl);

        StringEntity stringEntity = new StringEntity(jsonString, DEFAULT_CHARSET);
        stringEntity.setContentEncoding(DEFAULT_CHARSET);
        stringEntity.setContentType("application/json");

        httpPost.setEntity(stringEntity);

        return postResult(httpPost);
    }

    /**
     * 同步post请求
     *
     * @param requestUrl
     * @param paramsMap 请求参数,格式是Map<String,String> params
     * @return
     */
    public static String httpSyncPost(String requestUrl, Map<String,String> paramsMap) {

        if(StringUtils.isBlank(requestUrl)){
            throw new NullPointerException("baseUrl不能为空");
        }
        if(paramsMap == null){
            throw new NullPointerException("请求参数不能为null");
        }

        List<BasicNameValuePair> list = new ArrayList<>();

        Iterator<Map.Entry<String, String>> it = paramsMap.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<String, String> entry = it.next();
            BasicNameValuePair basicNameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
            list.add(basicNameValuePair);
        }
        return httpSyncPost(requestUrl, list);
    }



    /**
     * 同步GET请求 向指定的url发送一次get请求
     *
     * @param requestUrl
     * @return
     */
    public static String httpSyncGet(String requestUrl) {
        return httpSyncGet(requestUrl, new ArrayList<BasicNameValuePair>());
    }


    /**
     * 同步GET请求 向指定的url发送一次get请求,参数是字符串
     * @param requestUrl   请求地址
     * @param urlParams 请求参数,格式是String
     * @return 返回结果, 请求失败时返回null
     * @apiNote http接口处用 @RequestParam接收参数
     */
    public static String httpSyncGet(String requestUrl, String urlParams) {

        if(StringUtils.isBlank(requestUrl) || StringUtils.isBlank(urlParams)){
            throw new NullPointerException("baseUrl或urlParams不能为空");
        }

        HttpGet httpGet = new HttpGet(requestUrl);
        CloseableHttpResponse response = null;

        if (StringUtils.isNotBlank(urlParams)) {
            try {
                httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + urlParams));
            } catch (URISyntaxException e) {
                LOGGER.error("GET 请求URI异常: ", e);
            }
        } else {
            try {
                httpGet.setURI(new URI(httpGet.getURI().toString()));
            } catch (URISyntaxException e) {
                LOGGER.error("GET 请求URI异常: ", e);
            }
        }

        return getResult(httpGet);
    }

    /**
     * 同步GET请求 向指定的url发送一次get请求,参数是List<NameValuePair>
     *
     * @param requestUrl 请求地址
     * @param paramsList    请求参数,格式是List<NameValuePair>
     * @return 返回结果, 请求失败时返回null
     * @apiNote http接口处用 @RequestParam接收参数
     */
    public static String httpSyncGet(String requestUrl, List<BasicNameValuePair> paramsList) {

        if(StringUtils.isBlank(requestUrl)){
            throw new NullPointerException("baseUrl不能为空");
        }

        HttpGet httpGet = new HttpGet(requestUrl);

        if (paramsList != null) {
            String getUrl = null;
            try {
                getUrl = EntityUtils.toString(new UrlEncodedFormEntity(paramsList));
            } catch (IOException e) {
                LOGGER.error("get 请求, 请求体异常: ", e);
            }

            try {
                httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + getUrl));
            } catch (URISyntaxException e) {
                LOGGER.error("get 请求URI异常： ", e);
            }
        } else {
            try {
                httpGet.setURI(new URI(httpGet.getURI().toString()));
            } catch (URISyntaxException e) {
                LOGGER.error("get 请求URI异常： ", e);
            }
        }

        return getResult(httpGet);
    }


    /**
     * post 执行结果字符串封装返回
     *
     * @param httpPost
     * @return
     */
    private static String postResult(HttpPost httpPost){

        CloseableHttpClient httpClient = getCloseableHttpClient();
        CloseableHttpResponse response = null;
        String result = null;

        try{
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, DEFAULT_CHARSET);
            }
            EntityUtils.consume(entity);
        }catch (IOException e){
            LOGGER.error("执行方法异常: ", e);
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    /**
     * get 执行结果字符串封装返回
     *
     * @param httpGet
     * @return
     */
    public static String getResult(HttpGet httpGet){
        CloseableHttpClient httpClient = getCloseableHttpClient();

        CloseableHttpResponse response = null;
        String result = null;

        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, DEFAULT_CHARSET);
            }
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            LOGGER.error("get 请求异常: ", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("get 请求关闭响应异常: ", e);
                }
            }
        }
        return result;
    }

}
