package com.example.apple.mychatqq.activity.contacts;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.model.FriendinfoModel;

/**
 * Created by apple on 2017/4/1.
 */
@SuppressWarnings("all")
public class DetailinfoActivity extends AppCompatActivity {
    ImageView imageView = null;
    TextView beizhuName = null;
    TextView nickname = null;
    Button sendBtn = null;
    Button videoBtn = null;
    FriendinfoModel friendinfoModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailinfo);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.customactionbar);
        }
        imageView = (ImageView)findViewById(R.id.layout_detailinfo_image);
        beizhuName = (TextView)findViewById(R.id.layout_detailinfo_beizhu);
        nickname = (TextView)findViewById(R.id.layout_detailinfo_nickname);
        sendBtn = (Button) findViewById(R.id.layout_detailinfo_sendBtn);
        videoBtn = (Button) findViewById(R.id.layout_detailinfo_videoChat);
        friendinfoModel = (FriendinfoModel)getIntent().getExtras().getSerializable("friendModel");
        imageView.setImageBitmap(BitmapFactory.decodeFile(friendinfoModel.getUser_picture()));
        beizhuName.setText(friendinfoModel.getNickname());
        nickname.setText("昵称：" + friendinfoModel.getNickname());
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailinfoActivity.this,ChattingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("friendModel",friendinfoModel);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("abc","a");
            }
        });

    }
}
