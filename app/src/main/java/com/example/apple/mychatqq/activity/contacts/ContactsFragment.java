package com.example.apple.mychatqq.activity.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.activity.main.LogoActivity;
import com.example.apple.mychatqq.activity.main.MainActivity;
import com.example.apple.mychatqq.model.FriendinfoModel;
import com.example.apple.mychatqq.model.ListBaseModel;
import com.example.apple.mychatqq.model.UserinfoModel;
import com.example.apple.mychatqq.utils.Download;
import com.example.apple.mychatqq.utils.HttpRequest;
import com.example.apple.mychatqq.utils.SqlliteHelper;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsFragment extends Fragment {
    ListView listView = null;
    Bitmap bitmap;
    private Download download = new Download();
    private UserinfoModel userinfoModel;
    private SQLiteDatabase db ;//这段代码放到Activity类中才用this
    ArrayList<FriendinfoModel> friendList = null;
    ArrayList<FriendinfoModel> intent_friendList = new ArrayList<>();
    private int[] images = {R.drawable.a_1,R.drawable.a_9,R.drawable.a_7,R.drawable.a_8};
    private String[] names = {"新的朋友","群聊","标签","公众号"};
    private List<Map<String, Object>> mylist = new ArrayList<>();
    private List<Map<String, Object>> splitList = new ArrayList<>();
    Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                friendList = (ArrayList<FriendinfoModel>) msg.obj;
                new myDownLoadThread(friendList).start();
            }
            if (msg.what == 10) {
                ArrayList<FriendinfoModel> list = (ArrayList<FriendinfoModel>)msg.obj;

                for (FriendinfoModel friend: list){
                    db.execSQL("insert into friendinfo values(?,?,?,?)",new String[]{friend.getUsername(),friend.getNickname(),friend.getUser_picture(),userinfoModel.getUsername()});
                }
                initView();
            }

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqlliteHelper database = new SqlliteHelper(getActivity());
        db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from userinfo where islogin = 1",null);
        if (cursor.moveToNext()){
            userinfoModel= new UserinfoModel();
            userinfoModel.setUsername(cursor.getString(0));
            userinfoModel.setSessionID(cursor.getString(1));
            userinfoModel.setNickname(cursor.getString(2));
            userinfoModel.setUser_picture(cursor.getString(3));
            Log.e("userinfoModelCon",userinfoModel.toString());
        }
        cursor.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab02, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
		initView();
//        initHeader();
    }

//    public void initHeader(){
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (int i=0;i<4;i++){
//            Map<String, Object> map = new HashMap<>();
//            map.put("username","");
//            map.put("nickname", names[i]);
//            map.put("user_picture", images[i]);
//            list.add(map);
//        }
//        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.contacts,
//                new String[]{"user_picture", "nickname"},
//                new int[]{R.id.layout_contacts_Image, R.id.layout_contacts_friendName});
////        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
////            @Override
////            public boolean setViewValue(View view, Object data, String textRepresentation) {
////                if (view instanceof ImageView && data instanceof Bitmap) {
////                    ImageView iv = (ImageView) view;
////                    iv.setImageBitmap((Bitmap) data);
////                    return true;
////                }
////                return false;
////            }
////        });
//        listView = (ListView) getActivity().findViewById(R.id.tab02_list);
//        listView.setAdapter(simpleAdapter);
////        listView.setOnItemClickListener(new ItemClickEvent());
//    }
    public void initView(){
        Cursor cursor = db.rawQuery("select * from friendinfo where friendbelongto = ?",new String[]{userinfoModel.getUsername()});
        if (cursor.getCount()==0){
            new myThread().start();
        }
        else {

            List<Map<String, Object>> list = new ArrayList<>();
            while (cursor.moveToNext()){
                Map<String, Object> map = new HashMap<>();
                map.put("username", cursor.getString(0));
                map.put("nickname", cursor.getString(1));
                map.put("user_picture", BitmapFactory.decodeFile(cursor.getString(2)));
                FriendinfoModel friendinfoModel = new FriendinfoModel();
                friendinfoModel.setUsername(cursor.getString(0));
                friendinfoModel.setNickname(cursor.getString(1));
                friendinfoModel.setUser_picture(cursor.getString(2));
                intent_friendList.add(friendinfoModel);
                list.add(map);

            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.contacts,
                    new String[]{"user_picture", "nickname"},
                    new int[]{R.id.layout_contacts_Image, R.id.layout_contacts_friendName});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView iv = (ImageView) view;
                        iv.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            listView = (ListView) getActivity().findViewById(R.id.layout_friendsList);
            listView.setAdapter(simpleAdapter);
            listView.setOnItemClickListener(new ItemClickEvent());
        }
        cursor.close();

    }


    @SuppressWarnings("unchecked")
    private final class ItemClickEvent implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FriendinfoModel friendinfoModel = intent_friendList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("friendModel", friendinfoModel);
            Intent intent = new Intent(getActivity(), DetailinfoActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.in_from_left);
        }
    }

    class myDownLoadThread extends Thread {
        private ArrayList<FriendinfoModel> friendinfoModels;
        private myDownLoadThread(ArrayList<FriendinfoModel> friendinfoModels) {
            this.friendinfoModels = friendinfoModels;
        }

        @Override
        public void run() {
            for (FriendinfoModel model : friendinfoModels) {
                bitmap = download.GetImageInputStream(model.getUser_picture());
                model.setUser_picture(download.SavaImage(bitmap, Environment.getExternalStorageDirectory().getPath() + "/mychatqq/"+userinfoModel.getUsername()+"/friendinfo",
                        model.getUsername()));
                Log.e("图片", model.getUser_picture());

            }
            Message message = Message.obtain();
            message.what = 10;
            message.obj = friendinfoModels;
            mHandler.sendMessage(message);
        }
    }
    @SuppressWarnings("unchecked")
    class myThread extends Thread {
        public void run() {
            try {
                String response = HttpRequest.doGet("http://182.254.152.99:8080/MyChat1/user/friend?username="+userinfoModel.getUsername());
                ListBaseModel<FriendinfoModel> friendinfoModel = ListBaseModel.fromJson(response, FriendinfoModel.class);
                Message message = new Message();
                if (friendinfoModel.getCode().equals("S01")) {
                    message.what = 1;
                    message.obj = friendinfoModel.getContents();
                    ContactsFragment.this.mHandler.sendMessage(message);
                    Log.e("e", friendinfoModel.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(), friendinfoModel.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), friendinfoModel.getMessage(), Toast.LENGTH_SHORT).show();
                    message.what = 2;
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
