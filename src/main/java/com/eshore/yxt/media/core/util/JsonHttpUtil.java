package com.eshore.yxt.media.core.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class JsonHttpUtil {
	
	public static void main(String[] args) {
		System.out.println("test  start");
		String doPost = "";
		//请求
		try {

			String aa = "{\"cmd\":\"company disptch\",\"cmdtype\":\"\",\"data\":{\"dt\":\"company_disptch_req\",\"email\":\"eeeeeee\",\"mobile\":\"18999999999\",\"userName\":\"test1021_01\"}}";

			doPost = JsonHttpUtil.doPost("http://127.0.0.1:81/request", aa);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(doPost);
		System.out.println("test  end");
	}
	
	
	public static String doPost(String httpUrl,String json) {

        try {
            //创建连接
        	byte[] data = json.getBytes("UTF-8");
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.setConnectTimeout(5*1000);

            //POST请求
            OutputStream out = connection.getOutputStream();

            out.write(data);
            out.flush();
            out.close();

            //读取响应
            int responseCode = connection.getResponseCode();
            System.out.println("responseCode===:"+responseCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"utf-8"));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            connection.disconnect();
            return sb.toString();
            // 断开连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
	
	
	
	public static String doPost2(String httpUrl,String json) {

        try {
            //创建连接
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(20000);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");
            connection.setRequestProperty("accept", "application/json");
            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());

            out.writeBytes(json);
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"utf-8"));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            connection.disconnect();
            return sb.toString();
            // 断开连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

