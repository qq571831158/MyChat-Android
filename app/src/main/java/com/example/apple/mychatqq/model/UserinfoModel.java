package com.example.apple.mychatqq.model;

import java.io.Serializable;

/**
 * Created by apple on 2017/4/4.
 */

public class UserinfoModel implements Serializable{
    private String sessionID;
    private String nickname;
    private String user_picture;
    private String ip;
    private int port;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "sessionID"+ getSessionID()+ "nickname"+getNickname()+"userpicture"+getUser_picture() + "ip"+getIp()+getPort();
    }
    //    {
//        "sessionID":"1819ac2e09cb32c2dbde1cdae771fb0e",
//            "username":"cheng",
//            "nickname":"程恒",
//            "user_picture":"http://182.254.152.99:8080/MyChat1/test/test0.jpg",
//            "ip":"59.68.1.242",
//            "port":54966
//    }
}
