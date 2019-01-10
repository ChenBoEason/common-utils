package com.github.common.utils.network.factory;

import com.github.common.utils.network.client.HttpAsyncClient;
import com.github.common.utils.network.client.HttpSyncClient;
import com.github.common.utils.network.client.OkClient;

/**
 * HttpClient 工厂类
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: 参考代码: https://github.com/wsdl-king/AsyncHttpClientPool/blob/master/src/main/java/com/server/java/util/http/factory/HttpClientFactory.java
 */
public class HttpClientFactory {

//    private static HttpAsyncClient httpAsyncClient = new HttpAsyncClient();

//    private static HttpSyncClient httpSyncClient = new HttpSyncClient();

//    private static HttpClientFactory httpClientFactory = new HttpClientFactory();

//    private static OkClient okClient = new OkClient();


    private HttpClientFactory() {
    }

    public static HttpClientFactory getInstance() {
        new HttpSyncClient();
        return new HttpClientFactory();
    }

    public HttpAsyncClient getHttpAsyncClientPool() {
        return new HttpAsyncClient();
    }

    public HttpSyncClient getHttpSyncClientPool() {
        return new HttpSyncClient();
    }

    public OkClient getOkClientPool() {
        return new OkClient();
    }
}
