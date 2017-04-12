package com.example.apple.mychatqq.activity.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.activity.contacts.ChattingActivity;
import com.example.apple.mychatqq.model.ChattingModel;
import com.example.apple.mychatqq.model.UserinfoModel;
import com.example.apple.mychatqq.utils.SqlliteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private List<Map<String, Object>> mylist = new ArrayList<>();
    private List<Map<String, Object>> splitList = new ArrayList<>();
    private static UserinfoModel userinfoModel;
    ImageView imageView = null;
    TextView textView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab04, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        imageView = (ImageView) getActivity().findViewById(R.id.layout_profile_iamge);
        textView = (TextView) getActivity().findViewById(R.id.layout_profile_text);
        imageView.setImageBitmap(BitmapFactory.decodeFile(userinfoModel.getUser_picture()));
        textView.setText(userinfoModel.getNickname());
        GroupListAdapter groupListAdapter = new GroupListAdapter(getContext(), mylist, splitList);
        ListView listView = (ListView) getActivity().findViewById(R.id.layout_profile_listview);
        listView.setAdapter(groupListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position",position+"");
                if (position==9){
                    Intent intent = new Intent(getActivity(),SettingActivity.class);
                    intent.putExtra("username",userinfoModel.getUsername());
                    startActivity(intent);
                }
            }
        });
    }

    public void setData() {
        Map<String, Object> mp = new HashMap<>();
        mp.put("title", "A");
        mylist.add(mp);
        splitList.add(mp);
        for (int i = 0; i < 2; i++) {
            Map<String, Object> map = new HashMap<>();
            if (i == 0) {
                map.put("title", "相册");
                map.put("image", R.drawable.akb);
                mylist.add(map);
            } else {
                map.put("title", "收藏");
                map.put("image", R.drawable.ake);
                mylist.add(map);
            }
        }
        mp = new HashMap<>();
        mp.put("title", "B");
        mylist.add(mp);
        splitList.add(mp);
        for (int i = 0; i < 2; i++) {
            Map<String, Object> map = new HashMap<>();
            if (i == 0) {
                map.put("title", "钱包");
                map.put("image", R.drawable.akc);
                mylist.add(map);
            } else {
                map.put("title", "卡包");
                map.put("image", R.drawable.akd);
                mylist.add(map);
            }
        }
        mp = new HashMap<>();
        mp.put("title", "C");
        mylist.add(mp);
        splitList.add(mp);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("title", "表情");
        map3.put("image", R.drawable.akg);
        mylist.add(map3);

        mp = new HashMap<>();
        mp.put("title", "D");
        mylist.add(mp);
        splitList.add(mp);
        Map<String, Object> map4 = new HashMap<>();
        map4.put("title", "设置");
        map4.put("image", R.drawable.akf);
        mylist.add(map4);
    }

    public static class UserinfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            userinfoModel = (UserinfoModel) intent.getSerializableExtra("userinfo");
            Log.e(TAG, userinfoModel.toString());
        }
    }
}
