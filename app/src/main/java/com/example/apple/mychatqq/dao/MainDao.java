package com.example.apple.mychatqq.dao;

import android.util.Log;

import com.example.apple.mychatqq.model.FriendinfoModel;
import com.example.apple.mychatqq.utils.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017/3/31.
 */

public class MainDao extends Thread{
    public static List<FriendinfoModel> getFriendInfo(final String url){
        final List<FriendinfoModel>list = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = HttpRequest.doGet(url);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if (code.equals("S01")){
                        Log.e("请求","成功");
                        JSONArray jsonArray = jsonObject.getJSONArray("contents");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObjectSon= (JSONObject)jsonArray.get(i);
                            FriendinfoModel model = new FriendinfoModel();
                            Log.e("请求","成功1111");
                            model.setUsername(jsonObjectSon.getString("username"));
                            model.setNickname(jsonObjectSon.getString("nickname"));
                            model.setUser_picture(jsonObjectSon.getString("user_picture"));
                            list.add(model);
                        }
                    }
                    else{
                        Log.e("请求","失败");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Log.e("线程执行list", list.size()+"");
        Log.e("线程执行", "执行");
        return list;
    }


}
