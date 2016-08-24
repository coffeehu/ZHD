package com.hc.zhdaily.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hc on 2016/8/18.
 *
 * 通过 url 获取 json 数据
 *
 */
public class GetJson {

    public static String getJson(String myUrl){
        HttpURLConnection connection = null;
        StringBuilder response = null;
        try {
            URL url = new URL(myUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(2000);
            connection.setRequestMethod("GET");
            connection.setReadTimeout(2000);
            InputStream in = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(connection != null) connection.disconnect();
            if (response != null) {return response.toString();}
            else {return null;}
        }
    }

}
