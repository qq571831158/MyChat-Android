package com.example.apple.mychatqq.activity.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.model.ChattingModel;
import com.example.apple.mychatqq.model.FriendinfoModel;
import com.example.apple.mychatqq.model.UserinfoModel;
import com.example.apple.mychatqq.utils.MyUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by apple on 2017/4/2.
 */

public class ChattingActivity extends AppCompatActivity {
    ListView listView = null;
    EditText editText = null;
    public RecivieMessageReceiver recivieMessageReceiver;
    private static final String TAG = "ChattingActivity";
    private FriendinfoModel friendinfoModel;
    public static UserinfoModel userinfoModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        Intent intent = getIntent();
        friendinfoModel = (FriendinfoModel) intent.getSerializableExtra("friendModel");
        recivieMessageReceiver = new RecivieMessageReceiver();
        final IntentFilter filter = new IntentFilter();
        filter.addAction("receive_message");
        registerReceiver(recivieMessageReceiver, filter);
        editText = (EditText) findViewById(R.id.layout_chatting_editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    Log.e(TAG,"点击了");
                    ChattingModel chattingModel = new ChattingModel();
                    chattingModel.setFromUser(userinfoModel.getUsername());
                    chattingModel.setToUser(friendinfoModel.getUsername());
                    chattingModel.setMessage(editText.getText().toString());
                    chattingModel.setType(0);
                    chattingModel.setTime(System.currentTimeMillis());
                    chattingModel.setImage(userinfoModel.getUser_picture());
                    showOnScreen(chattingModel);
                    Intent sendmsg = new Intent();
                    sendmsg.setAction("send_message");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("chattingModel", chattingModel);
                    sendmsg.putExtras(bundle);
                    sendBroadcast(sendmsg);
                    MyUtils.writeTOLocal(chattingModel,userinfoModel.getUsername(),friendinfoModel.getUsername());
                    editText.setText("");
                }
                return false;
            }

        });
        initData();
    }


    public void initData() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/mychatqq/"+userinfoModel.getUsername()
                +"/chattingRecord/" + friendinfoModel.getUsername() + ".txt");
        if (file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                try {
                    String str;
                    while ((str = br.readLine()) != null) {
                        showOnScreen(new Gson().fromJson(str,ChattingModel.class));
                        Log.e("读取的数据为", str);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }




    public void showOnScreen(ChattingModel model) {
        listView = (ListView) findViewById(R.id.layout_chatting_list);
        ChattingAdapter adapter = new ChattingAdapter(this, model);
        listView.setAdapter(adapter);
    }


    private class RecivieMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ChattingModel chattingModel = (ChattingModel) intent.getSerializableExtra("chattingModel");
            if (chattingModel.getFromUser().equals(friendinfoModel.getUsername())){
                showOnScreen(chattingModel);
            }
//            writeTOLocal(chattingModel);
            Log.e(TAG, chattingModel.toString());
        }
    }

    public static class UserinfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            userinfoModel = (UserinfoModel) intent.getSerializableExtra("userinfo");
            Log.e(TAG, userinfoModel.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recivieMessageReceiver);
        Log.e("destory","hasdestory");
        Intent intent = new Intent();
        intent.setAction("view_dismiss");
        sendBroadcast(intent);
    }
}
