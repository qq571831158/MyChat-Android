package com.example.apple.mychatqq.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by apple on 2017/4/8.
 */

public class TimeUtils {
    public static String getRelativeTime(long time){
        //今天
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR,today.get(Calendar.YEAR));
        today.set(Calendar.MONTH,today.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH,today.get(Calendar.DAY_OF_MONTH));
        today.set(Calendar.HOUR_OF_DAY,0);
        today.set(Calendar.MINUTE,0);
        today.set(Calendar.SECOND,0);
        //昨天
        Calendar yesterday = Calendar.getInstance();
        yesterday.set(Calendar.YEAR,yesterday.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH,yesterday.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH,yesterday.get(Calendar.DAY_OF_MONTH)-1);
        yesterday.set(Calendar.HOUR_OF_DAY,0);
        yesterday.set(Calendar.MINUTE,0);
        yesterday.set(Calendar.SECOND,0);

        Calendar week = Calendar.getInstance();
        week.set(Calendar.YEAR,week.get(Calendar.YEAR));
        week.set(Calendar.MONTH,week.get(Calendar.MONTH));
        week.set(Calendar.DAY_OF_MONTH,week.get(Calendar.DAY_OF_MONTH)-7);
        week.set(Calendar.HOUR_OF_DAY,0);
        week.set(Calendar.MINUTE,0);
        week.set(Calendar.SECOND,0);

        Calendar current = Calendar.getInstance();
        Date date = new Date(time);
        current.setTimeInMillis(time);
        if (current.after(today)){
            String relativeHour = null;
            String relativeMin = null;
            int hour = date.getHours();
            if (hour<10){
                relativeHour = "0"+hour;
            }
            else {
                relativeHour = hour+"";
            }
            int minute = date.getMinutes();
            if (minute<10){
                relativeMin = "0"+minute;
            }
            else {
                relativeMin = minute + "";
            }
            return relativeHour+":"+relativeMin;
        }
        else if (current.after(yesterday)&&current.before(today)){
            return "昨天";
        }
        else if (current.after(week)&&current.before(yesterday)){
            return "星期"+current.get(Calendar.DAY_OF_WEEK);
        }
        else
            return date.getYear()+"/"+date.getMonth()+"/"+date.getDay();

    }
}
