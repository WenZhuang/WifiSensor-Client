package com.bupt.Utils;


import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.*;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.*;


/**
 * Created by JohnVenn on 2015/9/23.
 *
 * 用Post方式跟服务器传递数据
 */
public class HttpUtils {

    public static final String LOGIN_URL = "http://10.103.26.192:8080/WifiSensor/servlet/LoginServlet";
    public static final String SIGNUP_URL = "http://10.103.26.192:8080/WifiSensor/servlet/SignUpServlet";
    public static final String Wifi_URL = "http://10.103.26.192:8080/WifiSensor/servlet/WifiServlet";
    public static final String PIC_URL = "http://10.103.26.192:8080/WifiSensor/city.jpg";

    public static String doPost(String url, Map<String,String> map) {
        String responseStr = "Fail";
        try {
            HttpPost httpRequest = new HttpPost(url);

            //设置请求参数
            HttpParams params = new BasicHttpParams();
            // 从连接池中获取连接的超时时间
            ConnManagerParams.setTimeout(params, 1000);
            // 通过网络与服务器建立连接的超时时间
            HttpConnectionParams.setConnectionTimeout(params, 3000);
            // 读响应数据的超时时间
            HttpConnectionParams.setSoTimeout(params, 5000);
            httpRequest.setParams(params);

            // 下面开始跟服务器传递数据，使用BasicNameValuePair
            List<BasicNameValuePair> paramsList = new ArrayList<>();


            for(String key : map.keySet()){
                paramsList.add(new BasicNameValuePair(key , map.get(key)));
            }
            httpRequest.setEntity(new UrlEncodedFormEntity(
                    paramsList, HTTP.UTF_8));

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            final int ret = httpResponse.getStatusLine().getStatusCode();
            if (ret == HttpStatus.SC_OK) {
                 responseStr = EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);
            } else {
                responseStr = String.valueOf(ret);
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseStr;
    }

    public static String doGet(String url){
        String responseStr = "Fail";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpRequest = new HttpGet(url);

            //设置请求参数
            HttpParams params = new BasicHttpParams();
            // 从连接池中获取连接的超时时间
            ConnManagerParams.setTimeout(params, 1000);
            // 通过网络与服务器建立连接的超时时间
            HttpConnectionParams.setConnectionTimeout(params, 3000);
            // 读响应数据的超时时间
            HttpConnectionParams.setSoTimeout(params, 5000);
            httpRequest.setParams(params);


            HttpResponse httpResponse = httpClient.execute(httpRequest);
            final int ret = httpResponse.getStatusLine().getStatusCode();
            if (ret == HttpStatus.SC_OK) {
                responseStr = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
            } else {
                responseStr = String.valueOf(ret);
            }

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseStr;
    }
}