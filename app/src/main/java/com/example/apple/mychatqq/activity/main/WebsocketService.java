package com.example.apple.mychatqq.activity.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.model.ChattingModel;
import com.example.apple.mychatqq.model.UserinfoModel;
import com.example.apple.mychatqq.utils.SqlliteHelper;
import com.example.apple.mychatqq.utils.MyUtils;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by apple on 2017/4/7.
 */

public class WebsocketService extends Service {
    private static final String TAG = "WebsocketService";
    private UserinfoModel userinfoModel;
    private int num = 0;
    NotificationManager manager;
    private static WebSocketClient webSocketClient;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("bind","excuted");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getUserinfo();
        manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        initWebsocket();

    }
    public void getUserinfo(){
        SqlliteHelper database = new SqlliteHelper(getApplicationContext());
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from userinfo where islogin = 1",null);
        Log.e("curor",cursor.getCount()+"");
        if (cursor.moveToNext()){
            Log.e("service","执行");
            userinfoModel= new UserinfoModel();
            userinfoModel.setUsername(cursor.getString(0));
            userinfoModel.setSessionID(cursor.getString(1));
            userinfoModel.setNickname(cursor.getString(2));
            userinfoModel.setUser_picture(cursor.getString(3));
            Intent intent = new Intent();
            intent.setAction("getuserinfo");
            Bundle bundle = new Bundle();
            bundle.putSerializable("userinfo",userinfoModel);
            intent.putExtras(bundle);
            WebsocketService.this.sendBroadcast(intent);
        }
        cursor.close();
    }

    public String getFriendImage(String username){
        String pict = null;
        SqlliteHelper database = new SqlliteHelper(getApplicationContext());
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from friendinfo where username = ?",new String[]{username});
        while (cursor.moveToNext()){
            pict = cursor.getString(2);
        }
        cursor.close();
        return pict;
    }
    public void initWebsocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                webSocketClient = new WebSocketClient(URI.create("ws://182.254.152.99:8080/MyChat1/websocket/"+userinfoModel.getUsername()), new Draft_17()) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Log.e(TAG, "连接成功");
                    }

                    @Override
                    public void onMessage(String message) {
                        Log.e(TAG, message);
                        ChattingModel chattingModel = new Gson().fromJson(message, ChattingModel.class);
                        chattingModel.setImage(getFriendImage(chattingModel.getFromUser()));
                        chattingModel.setType(1);
                        chattingModel.setTime(System.currentTimeMillis());
                        noti(chattingModel);
                        MyUtils.writeTOLocal(chattingModel,userinfoModel.getUsername(),chattingModel.getFromUser());
                        Intent intent = new Intent();
                        intent.setAction("receive_message");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("chattingModel",chattingModel);
                        intent.putExtras(bundle);
                        WebsocketService.this.sendBroadcast(intent);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.e(TAG, reason + code + remote);
                        Log.e(TAG, "连接关闭");
                    }

                    @Override
                    public void onError(Exception ex) {
                        ex.printStackTrace();
                        Log.e(TAG, "连接错误");
                    }
                };
                webSocketClient.connect();
                Log.e("websocketclient",webSocketClient.toString());
            }
        }).start();
    }

    public void noti(ChattingModel chattingModel) {
        //1、创建通知对象
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        //2、设置通知对象的各种信息
        //注意：以下6点信息的设置，必须要写小图标，其余的可选择性省略
        nb.setContentTitle(chattingModel.getFromUser());
        //设置大图标
        nb.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        nb.setContentText(chattingModel.getFromUser()+":"+chattingModel.getMessage());
        //设置info信息，即设置显示在时间右下角的文字
//        nb.setContentInfo("info信息");
        //必须要设置的小图标
        nb.setSmallIcon(R.mipmap.ic_launcher);
        //设置通知时间
        nb.setWhen(System.currentTimeMillis());

        //设置声音和振动
        nb.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

//        nb.setTicker("设置滚动提示的文字");
        //不能手动移除，模态，需要代码控制
        //nb.setOngoing(true);
        num++;
        manager.notify(num, nb.build());   //发送通知
    }

    public static class sendMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("service","收到发送消息通知");
            ChattingModel chattingModel = (ChattingModel) intent.getSerializableExtra("chattingModel");
            webSocketClient.send(new Gson().toJson(chattingModel));
            Log.e(TAG, chattingModel.toString());
        }
    }
}
//{"fromUser":"piao","image":"/storage/emulated/0/mychatqq/friendinfo/piao.jpg","message":"大家好","time":1491712680645,"toUser":"cheng","type":1}
//        ��
//        E/WeixinFragment: {"fromUser":"cheng","image":"/storage/emulated/0/mychatqq/cheng.jpg","message":"hhyvvn大家","time":1491712669088,"toUser":"6","type":0}
//        E/WeixinFragment: {"fromUser":"cheng","image":"/storage/emulated/0/mychatqq/cheng.jpg","message":"cfddf","time":1491712294036,"toUser":"4","type":0}