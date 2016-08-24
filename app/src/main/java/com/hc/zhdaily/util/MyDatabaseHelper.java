package com.hc.zhdaily.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hc on 2016/8/20.
 *
 *  生成数据据，用来保存首页的 json 数据（最新消息和过往消息），无网的时候用到
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_JSON = "create table json ("
            + "id integer primary key autoincrement,"
            + "date integer,"
            + "url text,"
            + "json text)";

    private Context mContent;

    public MyDatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory cursorFactory, int version){
        super(context, databaseName, cursorFactory, version);
        mContent = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_JSON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

}
