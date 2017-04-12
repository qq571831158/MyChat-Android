package com.example.apple.mychatqq.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by apple on 2017/3/31.
 */

public class FriendinfoModel implements Serializable{
    private String username;
    private String nickname;
    private String user_picture;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public FriendinfoModel(){}
    public FriendinfoModel(String username, String user_picture, String nickname) {
        this.username = username;
        this.user_picture = user_picture;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return this.getUsername()+this.getNickname()+this.getUser_picture();
    }
}
