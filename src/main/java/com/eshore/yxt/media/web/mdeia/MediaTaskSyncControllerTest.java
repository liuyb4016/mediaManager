package com.eshore.yxt.media.web.mdeia;

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
import java.util.List;

public class MediaTaskSyncControllerTest {

    /**
     * POST方式发起http请求
     */
    @Test
    public void requestByPostMethod(){
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPost post = new HttpPost("http://127.0.0.1/mediaTask/createtask");          //这里用上本机的某个工程做测试
            //创建参数列表
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            /**
             * private String type;//数据id  1 院线通片花
             private String fileId;//文件ID
             private String videoName;//视频名称
             private Long videoSize;//视频大小
             private String videoMd5;//视频md5
             private String videoUrl;//视频下载路径
             private String callbackUrl;//处理结果回调  提供一个接收结果的接口
             */
            list.add(new BasicNameValuePair("type","1"));
            list.add(new BasicNameValuePair("fileId","5321"));
            list.add(new BasicNameValuePair("videoName","before-code-1.flv"));
            list.add(new BasicNameValuePair("videoSize","20266260"));
            list.add(new BasicNameValuePair("videoMd5","9682ed715a116139de4bed0d89ed08e9"));
            list.add(new BasicNameValuePair("videoUrl","/media"));
            list.add(new BasicNameValuePair("callbackUrl","http://127.0.0.1/mediaTask/result"));

            //url格式编码
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list,"UTF-8");
            post.setEntity(uefEntity);
            System.out.println("POST 请求...." + post.getURI());
            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            try{
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity){
                    System.out.println("-------------------------------------------------------");
                    System.out.println(EntityUtils.toString(uefEntity));
                    System.out.println("-------------------------------------------------------");
                }
            } finally{
                httpResponse.close();
            }

        } catch( UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                closeHttpClient(httpClient);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private CloseableHttpClient getHttpClient(){
        return HttpClients.createDefault();
    }

    private void closeHttpClient(CloseableHttpClient client) throws IOException{
        if (client != null){
            client.close();
        }
    }
}
