package com.eshore.yxt.media.web.mdeia;

import com.eshore.yxt.media.core.util.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaTaskSyncControllerTest {

    /**
     * POST方式发起http请求
     */
    @Test
    public void requestByPostMethod1(){
        String url = "http://127.0.0.1/mediaTask/createtask";
        Map<String, String> reqMap = new HashMap<String, String>();
        reqMap.put("taskId","2017082111_fb3ea265-0e95-4664-b594-ca5da08f5f55");
        reqMap.put("type","1");
        reqMap.put("fileId","5321");
        reqMap.put("videoName","before-code-1.flv");
        reqMap.put("videoSize","20266260");
        reqMap.put("videoMd5","9682ed715a116139de4bed0d89ed08e9");
        reqMap.put("videoUrl","/media");
        reqMap.put("callbackUrl","http://127.0.0.1/mediaTask/result");

        String result =  HttpUtils.requestByPostMethod(url,reqMap);
        System.out.println("result----->"+result);

    }

    /**
     * POST方式发起http请求
     */
    @Test
    public void requestByPostMethod2(){
        String url = "http://127.0.0.1/mediaTask/getTaskResult";
        Map<String, String> reqMap = new HashMap<String, String>();
        reqMap.put("taskId","2017082111_fb3ea265-0e95-4664-b594-ca5da08f5f55");
        String result =  HttpUtils.requestByPostMethod(url,reqMap);
        System.out.println("result----->"+result);
    }
}
