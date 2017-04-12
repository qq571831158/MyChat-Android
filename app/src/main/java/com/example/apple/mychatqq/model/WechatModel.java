package com.example.apple.mychatqq.model;

/**
 * Created by apple on 2017/3/30.
 */

public class WechatModel {
    private String friendName;
    private String lastRecord;
    private String timeLabel;
    private String imagePath;

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getLastRecord() {
        return lastRecord;
    }

    public void setLastRecord(String lastRecord) {
        this.lastRecord = lastRecord;
    }

    public String getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(String timeLabel) {
        this.timeLabel = timeLabel;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
