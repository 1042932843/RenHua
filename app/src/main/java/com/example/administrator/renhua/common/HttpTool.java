package com.example.administrator.renhua.common;

import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by K on 2016/3/27.
 */
public class HttpTool {
    public static String SendPhone(String phone, String name, String type)
            throws ClientProtocolException, IOException {
        String strResult = null;
        String httpUrl = "http://ggfw.xinxing.gov.cn:80/repair/toSaveRepair.xhtml";
        // HttpPost连接对象
        HttpPost httpRequest = new HttpPost(httpUrl);
        // 使用NameValuePair来保存要传递的Post参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // 添加要传递的参数
        params.add(new BasicNameValuePair("repair.repairType", type));
        params.add(new BasicNameValuePair("repair.relationName", name));
        params.add(new BasicNameValuePair("repair.relationPhone", phone));
        params.add(new BasicNameValuePair("repair.source", "1"));
        // 设置字符集
        HttpEntity httpentity = new UrlEncodedFormEntity(params, "utf-8");
        // 请求httpRequest
        httpRequest.setEntity(httpentity);
        // 取得默认的HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 取得HttpResponse
        HttpResponse httpResponse = httpclient.execute(httpRequest);
        // HttpStatus.SC_OK表示连接成功
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 取得返回的字符串;
            strResult = EntityUtils.toString(httpResponse.getEntity());
        }
        return strResult;
    }

    public static String SendCounsel(String userId, String question, String discription)
            throws ClientProtocolException, IOException, JSONException {
        String strResult = null;
        Log.d("reg", "1111111");
        String httpUrl = Constant.DOMAIN+"questionController.do?saveQuestion";
//        String httpUrl = "http://192.168.1.171:8090/lcggfw/phoneQuestionController.do?saveQuestion";
        // HttpPost连接对象
        HttpPost httpRequest = new HttpPost(httpUrl);
        // 使用NameValuePair来保存要传递的Post参数
        JSONObject jo=new JSONObject();
        jo.put("userId", userId);
        jo.put("qTitle", question);
        jo.put("qContent", discription);
        ByteArrayEntity e= new ByteArrayEntity(jo.toString().getBytes());
        Log.d("reg", "提交问题："+jo.toString());
        Log.d("reg", "提交问题："+e.toString());
        // 请求httpRequest
        httpRequest.setEntity(e);
        // 取得默认的HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 取得HttpResponse
        HttpResponse httpResponse = httpclient.execute(httpRequest);
        // HttpStatus.SC_OK表示连接成功
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 取得返回的字符串;
            strResult = EntityUtils.toString(httpResponse.getEntity());
            Log.d("RegOne","strResult:"+strResult);
        }
        return strResult;
    }

    public static String SendRequest(String userId, String question, String discription) {
        String adress_Http="http://ggfw.fengxi.gov.cn/phone/question/saveQuestion.xhtml";
        String strJson="{'userId':“+userId+”,'question':"+question+",'discription':"+discription+"}";
        String returnLine = "";
        try {
            Log.d("RegOne","**************开始http通讯**************");
            Log.d("RegOne","**************调用的接口地址为**************" + adress_Http);
            Log.d("RegOne","**************请求发送的数据为**************" + strJson);
            URL my_url = new URL(adress_Http);
            HttpURLConnection connection = (HttpURLConnection) my_url.openConnection();
            connection.setDoOutput(true);

            connection.setDoInput(true);

            connection.setRequestMethod("POST");

            connection.setUseCaches(false);

            connection.setInstanceFollowRedirects(true);

            connection.setRequestProperty("Content-Type", "text/html");

            connection.connect();
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());

            byte[] content = strJson.getBytes("utf-8");

            out.write(content, 0, content.length);
            out.flush();
            out.close(); // flush and close

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

            //StringBuilder builder = new StringBuilder();

            String line = "";

            System.out.println("Contents of post request start");

            while ((line = reader.readLine()) != null) {
                // line = new String(line.getBytes(), "utf-8");
                returnLine += line;

                System.out.println(line);

            }

            System.out.println("Contents of post request ends");

            reader.close();
            connection.disconnect();
            System.out.println("========返回的结果的为========" + returnLine);

        } catch (Exception e) {
            Log.e("RegOne","err",e);
        }

        return returnLine;

    }

    public static String SendPhoto(String phone, String photo)
            throws ClientProtocolException, IOException {
        String strResult = null;
        String httpUrl = "http://ggfw.xinxing.gov.cn:80/repair/toSaveRepair.xhtml";
        // HttpPost连接对象
        HttpPost httpRequest = new HttpPost(httpUrl);
        // 使用NameValuePair来保存要传递的Post参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // 添加要传递的参数
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("pictureBase64Source", photo));
        // 设置字符集
        HttpEntity httpentity = new UrlEncodedFormEntity(params, "utf-8");
        // 请求httpRequest
        httpRequest.setEntity(httpentity);
        // 取得默认的HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 取得HttpResponse
        HttpResponse httpResponse = httpclient.execute(httpRequest);
        // HttpStatus.SC_OK表示连接成功
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 取得返回的字符串;
            strResult = EntityUtils.toString(httpResponse.getEntity());
        }
        return strResult;
    }
}
