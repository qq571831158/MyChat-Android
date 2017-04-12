package com.example.apple.mychatqq.activity.weixin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.activity.contacts.ChattingActivity;
import com.example.apple.mychatqq.model.ChattingModel;
import com.example.apple.mychatqq.model.FriendinfoModel;
import com.example.apple.mychatqq.model.UserinfoModel;
import com.example.apple.mychatqq.utils.MyUtils;
import com.example.apple.mychatqq.utils.SqlliteHelper;
import com.example.apple.mychatqq.utils.TimeUtils;
import com.google.gson.Gson;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeixinFragment extends Fragment {
    ListView listView = null;
    PullToRefreshView mPullToRefreshView;
    public static final String TAG = "WeixinFragment";
    static UserinfoModel userinfoModel;
    List<FriendinfoModel> friendinfoModels = new ArrayList<>();
    List<ChattingModel> chattingModels = new ArrayList<>();
    RecivieMessageReceiver2 recivieMessageReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recivieMessageReceiver = new WeixinFragment.RecivieMessageReceiver2();
        final IntentFilter filter = new IntentFilter();
        filter.addAction("receive_message");
        getActivity().registerReceiver(recivieMessageReceiver, filter);
    }

    public void initData() {
        if (friendinfoModels.size()!=0){
            friendinfoModels.clear();
            chattingModels.clear();
        }
        SqlliteHelper sqlliteHelper = new SqlliteHelper(getContext());
        SQLiteDatabase db = sqlliteHelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("select username from userinfo where islogin = 1",null);
        String username = null;
        while (cursor1.moveToNext()){
            username = cursor1.getString(0);
        }
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/mychatqq/"+username+"/chattingRecord/");
        if (file.exists()){
            File[] files = file.listFiles();
            Arrays.sort(files, new WeixinFragment.CompratorByLastModified());
            Log.e("files",Arrays.toString(files));
            for (File file1 : files) {
                try {
                    String fileName = file1.getName();
                    String friendName = fileName.substring(0, fileName.length() - 4);
                    Log.e("friendname",friendName);
                    Cursor cursor = db.rawQuery("select * from friendinfo where username = ?", new String[]{friendName});
                    while (cursor.moveToNext()) {
                        FriendinfoModel friendinfoModel = new FriendinfoModel();
                        friendinfoModel.setUsername(cursor.getString(0));
                        friendinfoModel.setNickname(cursor.getString(1));
                        friendinfoModel.setUser_picture(cursor.getString(2));
                        friendinfoModels.add(friendinfoModel);
                    }
                    cursor.close();
                    String content = MyUtils.readLastLine(file1.getAbsoluteFile(), "UTF-8");
                    Log.e(TAG,content);
                    if ((content.substring(content.length()-3,content.length()-2).equals("}"))){
                        content = content.substring(0,content.length()-2);
                    }
                    ChattingModel chattingModel = new Gson().fromJson(content, ChattingModel.class);
                    chattingModels.add(chattingModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            initView(chattingModels, friendinfoModels);
        }
    }

    public void initView(List<ChattingModel> chattingModels,  final List<FriendinfoModel> friendinfoModels) {
        List<Map<String, Object>> list = new ArrayList<>();
        Log.e("friendmodelsize",friendinfoModels.size()+"");
        Log.e("chattingdmodelsize",chattingModels.size()+"");
        if (chattingModels.size() != 0) {
            for (int i = 0; i < chattingModels.size(); i++) {
                ChattingModel chattingModel = chattingModels.get(i);
                FriendinfoModel friendinfoModel = friendinfoModels.get(i);
                Log.e(chattingModel.toString(),friendinfoModel.toString());
                Map<String, Object> map = new HashMap<>();
                map.put("image", BitmapFactory.decodeFile(friendinfoModel.getUser_picture()));
                map.put("friendName", friendinfoModel.getNickname());
                map.put("lastRecord", chattingModel.getMessage());
                map.put("time", TimeUtils.getRelativeTime(chattingModel.getTime()));
                list.add(map);
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.weixincell,
                    new String[]{"image", "friendName", "lastRecord", "time"},
                    new int[]{R.id.layout_Image, R.id.layout_friendName, R.id.layout_friendLastRecord, R.id.layout_time});
            listView = (ListView) getActivity().findViewById(R.id.listView);
            listView.setAdapter(simpleAdapter);
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView iv = (ImageView) view;
                        iv.setImageBitmap((Bitmap) data);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            if (mPullToRefreshView!=null) {
                mPullToRefreshView.setRefreshing(false);
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), ChattingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("friendModel", friendinfoModels.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        refresh();
    }

    public void refresh() {
        mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(R.id.tab01_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab01, container, false);
    }

    public static class UserinfoReceiver extends BroadcastReceiver {
        private static final String TAG = "UserinfoReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            userinfoModel = (UserinfoModel) intent.getSerializableExtra("userinfo");
            Log.e(TAG, userinfoModel.toString());
        }
    }

    public  class RecivieMessageReceiver2 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,intent.toString());
            initData();
        }
    }
    public static class CompratorByLastModified implements Comparator<File> {

        public int compare(File f1, File f2) {
            long diff = f1.lastModified() - f2.lastModified();
            if (diff > 0) {
                return -1;
            } else if (diff == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(recivieMessageReceiver);
    }
}
