package com.example.apple.mychatqq.activity.main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.model.BaseModel;
import com.example.apple.mychatqq.model.UserinfoModel;
import com.example.apple.mychatqq.utils.MyUtils;
import com.example.apple.mychatqq.utils.SqlliteHelper;

import java.io.File;

public class LogoActivity extends AppCompatActivity {
    private static final int ISLOGIN = 1;
    private static final int NOTLOGIN = 0;
    static UserinfoModel userinfoModel;
    public static final String TAG = "LogoActivity";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ISLOGIN) {
                Intent intent = new Intent(LogoActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (msg.what == NOTLOGIN) {
                Intent intent = new Intent(LogoActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logo);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SqlliteHelper database = new SqlliteHelper(LogoActivity.this);//这段代码放到Activity类中才用this
                SQLiteDatabase db = database.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from userinfo where islogin = 1 ",null);
                if (cursor.moveToNext()){
                    Message message = Message.obtain();
                    message.what = ISLOGIN;
                    handler.sendMessage(message);
                }
               else {
                    Message message = Message.obtain();
                    message.what = NOTLOGIN;
                    handler.sendMessage(message);
                }
                cursor.close();
            }
        }).start();
    }
}
