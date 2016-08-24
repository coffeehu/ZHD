package com.hc.zhdaily.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Administrator on 2016-07-09.
 */
public class MyHttpClient {
    private static final String BASE_URL = "http://news-at.zhihu.com/api/4/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url , RequestParams params ,AsyncHttpResponseHandler responseHandler){
        client.get(BASE_URL+url,params,responseHandler);
    }

    public static void post(String url , RequestParams params ,AsyncHttpResponseHandler responseHandler){
        client.post(BASE_URL+url,params,responseHandler);
    }

    public static void getImage(String url , RequestParams params ,AsyncHttpResponseHandler responseHandler){
        client.get(url,params,responseHandler);
    }

    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }else {
            return false;
        }
    }

}


















