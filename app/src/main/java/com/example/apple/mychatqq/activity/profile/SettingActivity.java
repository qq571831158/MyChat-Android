package com.example.apple.mychatqq.activity.profile;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.activity.main.LoginActivity;
import com.example.apple.mychatqq.utils.SqlliteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
//        ListView listView = (ListView)findViewById(R.id.layout_setting_list1);
        ListView listView2 = (ListView)findViewById(R.id.layout_setting_list2);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==2){
                    showExitDialog();
                }
            }
        });
    }


    public void showExitDialog(){
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        ListView listview = new ListView(this);

        layout.addView(listview);
        List<Map<String, Object>> nameList = new ArrayList<>();//建立一个数组存储listview上显示的数据
        int []images = new int[]{R.drawable.a,R.drawable.b};
        String[] text  = new String[]{"退出当前账号","关闭微信"};
        for (int m = 0; m < 2; m++) {//initData为一个list类型的数据源
                Map<String, Object> nameMap = new HashMap<>();
                nameMap.put("image", images[m]);
                nameMap.put("text",text[m]);
                nameList.add(nameMap);
        }
        SimpleAdapter adapter = new SimpleAdapter(SettingActivity.this,
                nameList, R.layout.dialogcell,
                new String[] { "image","text" },
                new int[] { R.id.dialogcell_image,R.id.dialogcell_text });
        listview.setAdapter(adapter);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(layout).create();//在这里把写好的这个listview的布局加载dialog
        dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
        dialog.show();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {//响应listview中的item的点击事件
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2==0){
                    dialog.cancel();
                    showConfirmDialog();

                }else if (arg2==1){
                    dialog.cancel();
                    android.os.Process.killProcess(android.os.Process.myPid());
//                    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//                    am.killBackgroundProcesses("com.example.apple.mychatqq");
                }

            }
        });
    }

    private void showConfirmDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(SettingActivity.this);
//        normalDialog.setIcon(R.drawable.icon_dialog);
//        normalDialog.setTitle("我是一个普通Dialog")
        normalDialog.setMessage("退出当前账号后不会删除任何历史数据，下次登录依然可以使用本账号。");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent user = getIntent();
                        SqlliteHelper sqlliteHelper = new SqlliteHelper(SettingActivity.this);
                        SQLiteDatabase db = sqlliteHelper.getReadableDatabase();
                        db.execSQL("update userinfo set islogin = 0 where username = ?",new String[]{user.getStringExtra("username")});
                        db.close();
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // 显示
        normalDialog.show();
    }

}
