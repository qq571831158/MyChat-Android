package com.example.apple.mychatqq.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by apple on 2017/4/5.
 */

public class SqlliteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mychatqq.db";//数据库名称
    private static final int version = 1; //数据库版本
    public SqlliteHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table userinfo(username varchar(20) not null  primary key ,sessionID varchar(50)not null," +
                " nickname varchar(20) not null, user_picture varchat(200) not null,islogin char(1) not null);";
        db.execSQL(sql);
        db.execSQL("create table friendinfo(username varchar(20) not null  primary key ,nickname varchar(50)not null," +
                "user_picture varchar(60) not null,friendbelongto varchar(20) not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
