package com.x4096.common.utils.network.client;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * An HTTP & HTTP/2 client for Android and Java application
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: 参考代码: https://github.com/wsdl-king/AsyncHttpClientPool/blob/master/src/main/java/com/server/java/util/http/client/OkClient.java
 */
public class OkClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(OkClientUtils.class);

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private static OkHttpClient okHttpClient;



    public void init() {
        ConnectionPool pool = new ConnectionPool(5, 10, TimeUnit.MINUTES);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .followRedirects(true)
                .readTimeout(3, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .writeTimeout(3, TimeUnit.MINUTES)
                .connectionPool(pool)
                .build();
        okHttpClient = client;
    }

    public void close(){

    }

    private static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }



    public static String okSyncPost(String url, String json) throws IOException {

        OkHttpClient okClient = getOkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = okClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void okAsyncPost(String url, String json) {
        OkHttpClient okClient = getOkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });

    }


    public static void okAsyncPost(String url, Map<String, String> map) throws IOException {
        OkHttpClient okClient = getOkHttpClient();

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formBodyBuilder.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder()
                .url(url)
                .post(formBodyBuilder.build())
                .build();
        Call call = okClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                LOGGER.warn("OkAsyncPost回调:" + response.body().string());
            }
        });

    }

    public static void OkAsyncPost(String url, Map<String, String> map, Callback callback) {
        OkHttpClient okClient = getOkHttpClient();

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formBodyBuilder.add(entry.getKey(), entry.getValue());
        }

        Request request = new Request.Builder()
                .url(url)
                .post(formBodyBuilder.build())
                .build();
        Call call = okClient.newCall(request);
        call.enqueue(callback);
    }

    public static String OkSyncGet(String url) throws IOException {

        OkHttpClient okClient = getOkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = okClient.newCall(request).execute()) {

            return response.body().string();
        }
    }

    public static void OkAsyncGet(String url) throws IOException {

        OkHttpClient okClient = getOkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

}
