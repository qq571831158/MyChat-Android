package com.example.apple.mychatqq.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import com.example.apple.mychatqq.model.ChattingModel;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by apple on 2017/4/8.
 */

public class MyUtils {
    public static final String TAG = "WritrToLocal";
    public static void writeTOLocal(ChattingModel chattingModel,String username,String friendName) {
        FileWriter fw ;
        BufferedWriter bf;
        boolean append=false;
        String path = Environment.getExternalStorageDirectory().getPath() + "/mychatqq/"+username+"/chattingRecord/";
        File dir = new File(path);
        if (!dir.exists()) {
            boolean b = dir.mkdirs();
            if (b) {
                Log.e(TAG, "目录创建成功");
            } else {
                Log.e(TAG, "目录创建失败");
            }
        }
        String fileName = friendName + ".txt";
        fileName = path + "/"+fileName;
        File file = new File(fileName);
        try {
            if (file.exists())
                append = true;
            fw = new FileWriter(fileName, append);//同时创建新文件
            //创建字符输出流对象
            bf = new BufferedWriter(fw);
            //创建缓冲字符输出流对象
            bf.append(new Gson().toJson(chattingModel));
            bf.newLine();
            bf.flush();
            bf.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static String readLastLine(File file, String charset) throws IOException {
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            long len = raf.length();
            if (len == 0L) {
                return "";
            } else {
                long pos = len - 1;
                while (pos > 0) {
                    pos--;
                    raf.seek(pos);
                    if (raf.readByte() == '\n') {
                        break;
                    }
                }
                if (pos == 0) {
                    raf.seek(0);
                }
                byte[] bytes = new byte[(int) (len - pos)];
                raf.read(bytes);
                if (charset == null) {
                    return new String(bytes);
                } else {
                    return new String(bytes, charset);
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }
    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}
