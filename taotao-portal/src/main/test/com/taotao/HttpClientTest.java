package com.taotao;

import com.taotao.common.utils.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientTest {

    public static final Logger Log = LoggerFactory.getLogger(HttpClientTest.class);

    /**
     * 使用 get 请求
     *
     * @throws Exception
     */
    @Test
    public void doGet() throws Exception {
        // 创建一个httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建一个get对象
        HttpGet get = new HttpGet("http://www.sogo.com/web?query=java");
        // 执行请求
        CloseableHttpResponse response = httpClient.execute(get);
        // 得到结果
        int statusCode = response.getStatusLine().getStatusCode();
        Log.info("status = " + statusCode);
        HttpEntity entity = response.getEntity();
        String str = EntityUtils.toString(entity);
        Log.info("str = " + str);
        // 关闭httpclient
        response.close();
        httpClient.close();
    }

    /**
     * 使用带参数的 get 请求
     *
     * @throws Exception
     */
    @Test
    public void doGetWithParam() throws Exception {
        // 创建一个httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建一个uri对象
        URIBuilder uriBuilder = new URIBuilder("http://www.sogo.com/web");
        uriBuilder.addParameter("query", "java");
        HttpGet get = new HttpGet(uriBuilder.build());
        // 执行请求
        CloseableHttpResponse response = httpClient.execute(get);
        // 得到结果
        int statusCode = response.getStatusLine().getStatusCode();
        Log.info("status = " + statusCode);
        HttpEntity entity = response.getEntity();
        String str = EntityUtils.toString(entity);
        Log.info("str = " + str);
        // 关闭httpclient
        response.close();
        httpClient.close();
    }

    /**
     * 使用 post 请求
     *
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {
        // 创建一个httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建一个post对象
        HttpPost post = new HttpPost("http://localhost:8083/httpclient/post.action");
        // 执行请求
        CloseableHttpResponse response = httpClient.execute(post);
        // 得到结果
        int statusCode = response.getStatusLine().getStatusCode();
        Log.info("status = " + statusCode);
        HttpEntity entity = response.getEntity();
        String str = EntityUtils.toString(entity);
        Log.info("str = " + str);
        // 关闭httpclient
        response.close();
        httpClient.close();
    }

    /**
     * 使用带参数的 post 请求
     *
     * @throws Exception
     */
    @Test
    public void doPostWithParam() throws Exception {
        // 创建一个httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建一个post对象
        HttpPost post = new HttpPost("http://localhost:8083/httpclient/postparm.action");
        // 创建一个entity,模拟一个表单
        List<NameValuePair> kvList = new ArrayList<>();
        kvList.add(new BasicNameValuePair("name", "小王"));
        kvList.add(new BasicNameValuePair("pwd", "123456"));
        // 包装成一个Entity对象
        StringEntity entity = new UrlEncodedFormEntity(kvList, "utf-8");
        // 设置请求的内容
        post.setEntity(entity);

        // 执行请求
        CloseableHttpResponse response = httpClient.execute(post);
        // 得到结果
        int statusCode = response.getStatusLine().getStatusCode();
        Log.info("status = " + statusCode);
        HttpEntity resEntity = response.getEntity();
        String str = EntityUtils.toString(resEntity);
        Log.info("str = " + str);
        // 关闭httpclient
        response.close();
        httpClient.close();
    }

    /**
     * 使用带参数的 post 请求
     *
     * @throws Exception
     */
    @Test
    public void doPostWithParam222() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("name", "小王");
        map.put("pwd", "123123");
        String str = HttpClientUtil.doPost("http://localhost:8083/httpclient/postparm.html", map);
        Log.info(str);
    }
}
