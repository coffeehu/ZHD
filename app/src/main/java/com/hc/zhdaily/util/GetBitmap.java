package com.hc.zhdaily.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hc on 2016/8/18.
 *
 * 通过图片 url 来获得 bitmap
 */
public class GetBitmap {

    public static Bitmap getBitmap(String url){
        URL imgUrl = null;
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        try {
           imgUrl  = new URL(url);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        try {
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true);
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.connect();
            InputStream is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(connection != null) connection.disconnect();
            return bitmap;
        }
    }

}
