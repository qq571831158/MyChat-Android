package com.example.apple.mychatqq.activity.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.apple.mychatqq.model.UserinfoModel;

/**
 * Created by apple on 2017/4/8.
 */

public class UserinfoReceiver extends BroadcastReceiver{
    private static final String TAG = "UserinfoReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG,intent.toString());
        UserinfoModel userinfoModel = (UserinfoModel) intent.getSerializableExtra("userinfo");
        Log.e(TAG, userinfoModel.toString());
    }
}
