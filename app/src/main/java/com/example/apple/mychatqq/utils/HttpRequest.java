package com.example.apple.mychatqq.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by apple on 2017/3/30.
 */

public class HttpRequest {

    public  static String doGet(String myurl){
        Log.e("当前线程GET",Thread.currentThread().toString());
         InputStream inputStream = null;
         HttpURLConnection urlConnection = null;
        try {

            URL url = new URL(myurl);
            urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        /* optional request header */
            urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(10000);
            int statusCode = urlConnection.getResponseCode();

        /* 200 represents HTTP OK */
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                return getStringFromInputStream(inputStream);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 模板代码 必须熟练
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        os.close();
        return state;
    }

}
