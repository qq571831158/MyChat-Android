package com.example.apple.mychatqq.activity.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.activity.contacts.ContactsFragment;
import com.example.apple.mychatqq.activity.discover.DiscoverFragment;
import com.example.apple.mychatqq.activity.profile.ProfileFragment;
import com.example.apple.mychatqq.activity.weixin.WeixinFragment;
import com.example.apple.mychatqq.utils.MyUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager viewPager;
    private LinearLayout llChat, llFriends, llContacts, llSettings;
    private ImageView ivChat, ivFriends, ivContacts, ivSettings, ivCurrent;
    private TextView tvChat, tvFriends, tvContacts, tvSettings, tvCurrent;
//    private NavigationBar navigationBar;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Log.i("设备尺寸",metrics.toString());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.customactionbar);
        }
        bindService(new Intent(MainActivity.this,WebsocketService.class),conn,BIND_AUTO_CREATE);
        startService(new Intent(MainActivity.this,WebsocketService.class));
        initView();
        initData();
    }

    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, "onServiceConnected");
        }
        public void onServiceDisconnected(ComponentName name) {
            Log.v(TAG, "onServiceDisconnected");
        }
    };
    //    private void initNav(){
//        navigationBar = (NavigationBar)super.findViewById(R.id.myNav);
//        navigationBar.setTitle("微信1");
//        navigationBar.setCliclCallback(new NavigationBar.ClickCallback() {
//            @Override
//            public void onSearchClick() {
//                Log.d("输出","点击了搜索按钮");
//            }
//
//            @Override
//            public void onAddClick() {
//                Log.d("输出","点击了添加按钮");
//            }
//        });
//    }
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        llChat = (LinearLayout) findViewById(R.id.llChat);
        llFriends = (LinearLayout) findViewById(R.id.llFriends);
        llContacts = (LinearLayout) findViewById(R.id.llContacts);
        llSettings = (LinearLayout) findViewById(R.id.llSettings);

        llChat.setOnClickListener(this);
        llFriends.setOnClickListener(this);
        llContacts.setOnClickListener(this);
        llSettings.setOnClickListener(this);

        ivChat = (ImageView) findViewById(R.id.ivChat);
        ivFriends = (ImageView) findViewById(R.id.ivFriends);
        ivContacts = (ImageView) findViewById(R.id.ivContacts);
        ivSettings = (ImageView) findViewById(R.id.ivSettings);

        tvChat = (TextView) findViewById(R.id.tvChat);
        tvFriends = (TextView) findViewById(R.id.tvFriends);
        tvContacts = (TextView) findViewById(R.id.tvContacts);
        tvSettings = (TextView) findViewById(R.id.tvSettings);

        ivChat.setSelected(true);
        tvChat.setSelected(true);
        ivCurrent = ivChat;
        tvCurrent = tvChat;

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        viewPager.setOffscreenPageLimit(2); //设置向左和向右都缓存limit个页面
    }

    private void initData() {
        Fragment chatFragment = new WeixinFragment();
        Fragment friendsFragment = new DiscoverFragment();
        Fragment contactsFragment = new ContactsFragment();
        Fragment settingsFragment = new ProfileFragment();

        fragments.add(chatFragment);
        fragments.add(contactsFragment);
        fragments.add(friendsFragment);
        fragments.add(settingsFragment);

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        changeTab(v.getId());
    }

    private void changeTab(int id) {
        ivCurrent.setSelected(false);
        tvCurrent.setSelected(false);
        switch (id) {
            case R.id.llChat:
                viewPager.setCurrentItem(0);
            case 0:
                ivChat.setSelected(true);
                ivCurrent = ivChat;
                tvChat.setSelected(true);
                tvCurrent = tvChat;
                break;
            case R.id.llFriends:
                viewPager.setCurrentItem(1);
            case 1:
                ivFriends.setSelected(true);
                ivCurrent = ivFriends;
                tvFriends.setSelected(true);
                tvCurrent = tvFriends;
                break;
            case R.id.llContacts:
                viewPager.setCurrentItem(2);
            case 2:
                ivContacts.setSelected(true);
                ivCurrent = ivContacts;
                tvContacts.setSelected(true);
                tvCurrent = tvContacts;
                break;
            case R.id.llSettings:
                viewPager.setCurrentItem(3);
            case 3:
                ivSettings.setSelected(true);
                ivCurrent = ivSettings;
                tvSettings.setSelected(true);
                tvCurrent = tvSettings;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onPause();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}