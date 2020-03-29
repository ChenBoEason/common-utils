package com.github.x4096.common.utils.network.http;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * An HTTP & HTTP/2 http for Android and Java application
 * @author: 0x4096.peng@gmail.com
 * @date: 2018/12/18
 * @instructions: 参考代码: https://github.com/wsdl-king/AsyncHttpClientPool/blob/master/src/main/java/com/server/java/util/http/client/OkClient.java
 */
public class OkHttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpClientUtils.class);

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

                logger.warn("OkAsyncPost回调:" + response.body().string());
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


    /**
     * 同步 GET 方法请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String OkSyncGet(String url) {
        if(StringUtils.isBlank(url)) {
            throw new NullPointerException("请求 url 不能为空");
        }
        OkHttpClient okClient = getOkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = okClient.newCall(request).execute();
            response.body().string();
        } catch (IOException e) {
            logger.error("同步 GET 请求异常", e);
        }finally {
            if(response != null){
                response.close();
            }
        }
        return null;
    }

    public static void OkAsyncGet(String url) {

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
