package com.example.apple.mychatqq.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by apple on 2017/3/30.
 */

public class Download {
    public Bitmap GetImageInputStream(String imageurl){
        URL url;
        HttpURLConnection connection=null;
        Bitmap bitmap=null;
        try {
            url = new URL(imageurl);
            connection=(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream=connection.getInputStream();
            bitmap= BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
//    Handler handler=new Handler(){
//        public void handleMessage(android.os.Message msg) {
//            if(msg.what==0x123){
////                image.setImageBitmap(bitmap);
//            }
//        };
//    };


    /**
     * 异步线程下载图片
     *
     */


    /**
     * 保存位图到本地
     * @param bitmap
     * @param path 本地路径
     * @return void
     */
    public String SavaImage(Bitmap bitmap, String path,String username){
        String returnPath = null;
        File file=new File(path);
        FileOutputStream fileOutputStream=null;
        //文件夹不存在，则创建它
        if(!file.exists()){
            boolean b = file.mkdirs();
            if (b){
                Log.e("创建","成功");
            }
            else
                Log.e("创建","失败");
        }
        try {
            fileOutputStream=new FileOutputStream(path+"/"+username+".jpg");
            returnPath = path+"/"+username+".jpg";
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnPath;
    }
}
