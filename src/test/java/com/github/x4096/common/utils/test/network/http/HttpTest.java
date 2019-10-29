package com.github.x4096.common.utils.test.network.http;

import com.github.x4096.common.utils.network.http.HttpSyncClientUtils;
import com.github.x4096.common.utils.network.http.enums.HttpContentTypeEnum;
import com.github.x4096.common.utils.network.http.result.HttpResponse;
import com.github.x4096.common.utils.test.pojo.OrderDetailReqVO;
import com.github.x4096.common.utils.text.RandomStringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-10-15 23:45
 * @Description:
 */
public class HttpTest {

    static {
        HttpSyncClientUtils.init();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String reqUrl = "http://127.0.0.1:9099/test3";



        Map<String, String> header = new HashMap<>();
        header.put("key1", "value2");
        header.put("key2", "value2");
        header.put("user-agent", "gggg");


        Map<String, Object> bizMap = new HashMap<>();

        OrderDetailReqVO orderDetailReqVO = new OrderDetailReqVO();
        orderDetailReqVO.setName("哈哈哈");
        orderDetailReqVO.setOrderNo(RandomStringUtils.uuid32());

        // bizMap.put("orderDetailReqVO", orderDetailReqVO);
        bizMap.put("age", 1);
        bizMap.put("name", "我梦");


        HttpResponse httpResponse = HttpSyncClientUtils.post(reqUrl, bizMap, header, HttpContentTypeEnum.APPLICATION_X_WWW_FORM_URLENCODED);

        System.err.println(httpResponse.toString());

        HttpPost httpPost = new HttpPost("https://www.google.com/");
        httpPost.setEntity(new StringEntity("http://127.0.0.1:9009"));

        RequestConfig requestConfig = RequestConfig.custom().
                setSocketTimeout(20_000).
                setConnectTimeout(10_000).
                setConnectionRequestTimeout(0).
                setProxy(new HttpHost("127.0.0.1", 1087)).
                build();
        httpPost.setConfig(requestConfig);


        HttpResponse httpResponse2 = HttpSyncClientUtils.post(httpPost);
        System.err.println(httpResponse2.getBody());


    }

}
