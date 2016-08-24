package com.hc.zhdaily.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hc.zhdaily.data.Comment;

/**
 * Created by hc on 2016/8/21.
 */
public class CommentDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_JSON = "create table json ("
            + "id integer primary key autoincrement,"
            + "myid integer,"
            + "json text)";

    private Context mContent;

    public CommentDatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory cursorFactory, int version){
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
