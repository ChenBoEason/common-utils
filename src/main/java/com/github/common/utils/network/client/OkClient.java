package com.github.common.utils.network.client;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * An HTTP & HTTP/2 client for Android and Java application
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: 参考代码: https://github.com/wsdl-king/AsyncHttpClientPool/blob/master/src/main/java/com/server/java/util/http/client/OkClient.java
 */
public class OkClient {

    public OkHttpClient getHttpClient() {
        ConnectionPool pool = new ConnectionPool(5, 10, TimeUnit.MINUTES);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .followRedirects(true)
                .readTimeout(3, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .writeTimeout(3, TimeUnit.MINUTES)
                .connectionPool(pool)
                .build();
        return client;
    }


}
