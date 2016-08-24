package com.hc.zhdaily.util;

import android.util.Log;

import java.io.File;
import java.util.Calendar;

/**
 * Created by hc on 2016/8/20.
 *
 * 获得今天的系统时间
 *
 */
public class GetTodayDate {
    public static String getDate(){
        Calendar c = Calendar.getInstance();
        String month = (c.get(Calendar.MONTH)+1)+"";
        if(month.length() == 1) month = "0"+month;
        String day = c.get(Calendar.DAY_OF_MONTH)+"";
        if(day.length() == 1) month = "0"+day;
        String year = c.get(Calendar.YEAR)+"";
        String date = year + month + day;
        return date;
    }
}
