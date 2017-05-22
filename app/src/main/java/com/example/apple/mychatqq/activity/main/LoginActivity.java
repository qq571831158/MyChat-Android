package com.example.apple.mychatqq.activity.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.apple.mychatqq.R;
import com.example.apple.mychatqq.model.BaseModel;
import com.example.apple.mychatqq.model.UserinfoModel;
import com.example.apple.mychatqq.utils.Download;
import com.example.apple.mychatqq.utils.MyUtils;
import com.example.apple.mychatqq.utils.SqlliteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by apple on 2017/4/4.
 */
@SuppressWarnings("unchecked")
public class LoginActivity extends AppCompatActivity {
    EditText usernameEdit = null;
    EditText passwordEdit = null;
    Button loginBtn = null;
    ImageView imageView = null;
    private static final String loginurl = "http://182.254.152.99:8080/MyChat1/user/login";
    private static final String TAG = "LoginActivity";
    public static final int DOWNLOAD_COMPLITE = 1;
    public static final int DOWNLOAD_IMAGE_COMPLETE = 2;
    private Download download = new Download();
    private Bitmap bitmap;
    private BaseModel<UserinfoModel> baseModel;
    private String username;
    private String password;
    ProgressDialog waitingDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            if (msg.what == DOWNLOAD_COMPLITE) {
                SqlliteHelper database = new SqlliteHelper(LoginActivity.this);
                SQLiteDatabase db = database.getReadableDatabase();
                BaseModel<UserinfoModel> baseModel = (BaseModel<UserinfoModel>) msg.obj;
                Cursor cursor = db.rawQuery("select * from userinfo where username = ?", new String[]{username});
                if (cursor.moveToNext()) {
//                    if (!baseModel.getContents().getSessionID().equals(cursor.getString(cursor.getColumnIndex("sessionID")))){
                    db.execSQL("update userinfo set  islogin = 1 where username = ?",
                            new String[]{username});
//                    }
                } else {
                    Log.e("login", baseModel.getContents().getUsername());
                    db.execSQL("insert into userinfo values(?,?,?,?,?)", new String[]{username, baseModel.getContents().getSessionID(),
                            baseModel.getContents().getNickname(), baseModel.getContents().getUser_picture(), "1"});
                }
                cursor.close();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                waitingDialog.dismiss();
            }
            if (msg.what==DOWNLOAD_IMAGE_COMPLETE){
                final String url   = msg.obj.toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Bitmap bitmap = MyUtils.getBitmap(url);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap bitmap1 = MyUtils.zoomImg(bitmap,100,100);
                                    imageView.setImageBitmap(bitmap1);

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEdit = (EditText) findViewById(R.id.layout_login_username);
        passwordEdit = (EditText) findViewById(R.id.layout_login_password);
        loginBtn = (Button) findViewById(R.id.layout_login_loginBtn);
        imageView = (ImageView) findViewById(R.id.layout_login_image);
        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String url = "http://182.254.152.99:8080/MyChat1/user/loginPic?username="+usernameEdit.getText().toString();
                final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("login",response.toString());
                        try {
                            if (response.get("code").equals("S01")){
                                JSONObject contents = response.getJSONObject("contents");
                                String pic = contents.getString("user_picture");
                                System.out.println(pic);
                                Message message = new Message();
                                message.what = DOWNLOAD_IMAGE_COMPLETE;
                                message.obj = pic;
                                handler.sendMessage(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage(), error);
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(jsonRequest);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWaitingDialog();
                username = usernameEdit.getText().toString();
                password = passwordEdit.getText().toString();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, loginurl, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "response -> " + response.toString());
                        baseModel = BaseModel.fromJson(response.toString(), UserinfoModel.class);
                        if (baseModel.getCode().equals("S01")) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    bitmap = download.GetImageInputStream(baseModel.getContents().getUser_picture());
                                    baseModel.getContents().setUser_picture(download.SavaImage(bitmap, Environment.getExternalStorageDirectory()
                                            + "/mychatqq/" + username, username));
                                    Message message = Message.obtain();
                                    message.what = DOWNLOAD_COMPLITE;
                                    message.obj = baseModel;
                                    handler.sendMessage(message);
                                }
                            }).start();
                        } else {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                            waitingDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage(), error);
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        waitingDialog.dismiss();
                    }
                });
//                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonRequest);
            }
        });
    }

    public void showWaitingDialog() {
    /* 等待Dialog具有屏蔽其他控件的交互能力
     * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
     * 下载等事件完成后，主动调用函数关闭该Dialog
     */
        waitingDialog = new ProgressDialog(LoginActivity.this);
//        waitingDialog.setTitle("我是一个等待Dialog");
        waitingDialog.setMessage("正在登陆...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onPause();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


//    AsyncHttpClient client = new AsyncHttpClient();
//    JSONObject jsonObject = new JSONObject();
//try {
//        jsonObject.put("username", username);
//        jsonObject.put("password", password);
//        } catch (JSONException e) {
//        e.printStackTrace();
//        }
//        ByteArrayEntity entity = null;
//        try {
//        entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
//        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//        } catch (UnsupportedEncodingException e) {
//        e.printStackTrace();
//        }
//
//        client.post(LoginActivity.this, loginurl, entity, "application/json", new JsonHttpResponseHandler() {
//@Override
//public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//        super.onSuccess(statusCode, headers, response);
//        baseModel = BaseModel.fromJson(response.toString(), UserinfoModel.class);
//        if (baseModel.getCode().equals("S01")) {
//        Log.e(TAG, baseModel.getContents().toString());
//        new Thread(new Runnable() {
//@Override
//public void run() {
//        bitmap = download.GetImageInputStream(baseModel.getContents().getUser_picture());
//        baseModel.getContents().setUser_picture(download.SavaImage(bitmap, Environment.getExternalStorageDirectory()
//        + "/mychatqq", username));
//        Message message = Message.obtain();
//        message.what = DOWNLOAD_COMPLITE;
//        message.obj = baseModel;
//        handler.sendMessage(message);
//        }
//        }).start();
//
//        }
//        else {
//        Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
//        }
//        }
//
//@Override
//public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//        super.onFailure(statusCode, headers, throwable, errorResponse);
//        Log.e(TAG, errorResponse.toString());
//        }
//        });
