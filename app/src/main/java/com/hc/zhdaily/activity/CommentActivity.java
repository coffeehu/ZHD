package com.hc.zhdaily.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hc.zhdaily.R;
import com.hc.zhdaily.adapter.CommentRecycleAdapter;
import com.hc.zhdaily.data.Comment;
import com.hc.zhdaily.data.CommentCount;
import com.hc.zhdaily.data.Comments;
import com.hc.zhdaily.util.CommentDatabaseHelper;
import com.hc.zhdaily.util.GetBitmap;
import com.hc.zhdaily.util.GetJson;
import com.hc.zhdaily.util.MyHttpClient;
import com.hc.zhdaily.view.CircleImageDrawable;
import com.hc.zhdaily.view.MyItemDecoration;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by hc on 2016/8/18.
 *
 * 评论页面。如url：http://news-at.zhihu.com/api/4/story/8693045/short-comments
 * 由于 MyHttpClient 的  get 方法用了 BASE_URL,所以我们只用输入：story/8693045/short-comments
 *采用 recycleView
 *
 * http://news-at.zhihu.com/api/4/story-extra/8693045  获得评论数，喜欢等
 */
public class CommentActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CommentCount commentCount;
    private Comments comments;
    private List<Comment> commentList;
    private RecyclerView recyclerView;
    private CommentRecycleAdapter commentRecycleAdapter;
    private SQLiteDatabase db = null;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.comment);
        db = new CommentDatabaseHelper(this, "CommentJsonDb.db", null, 1).getWritableDatabase();

        id = getIntent().getStringExtra("id");
        String murl = "story/"+ id +"/short-comments";
        String murl2 = "http://news-at.zhihu.com/api/4/story-extra/"+id;   //  http://news-at.zhihu.com/api/4/story-extra/8693045
        initView(murl2);
        getData(murl);
    }

    public void initView(final String url){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        // 获取点评数 http://news-at.zhihu.com/api/4/story-extra/8693045,显示在顶部的 toolbar 上
        final Handler handler = new Handler() {
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        String json = (String) msg.obj;
                        //Log.d("xiancheng","json = "+json);
                        Gson gson = new Gson();
                        commentCount = gson.fromJson(json, CommentCount.class);
                        if(commentCount!=null)getSupportActionBar().setTitle(commentCount.getShort_comments()+"条短评");
                        break;
                    default:
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                String json = GetJson.getJson(url);  // 通过 url 获取 json 数据
                message.what = 0;
                message.obj = json;
                handler.sendMessage(message); // 之后会执行 handleMessage
            }
        }).start();


        recyclerView = (RecyclerView) findViewById(R.id.comment_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new MyItemDecoration(this,
                MyItemDecoration.VERTICAL_LIST));
    }



    public void getData(String url){
        if(MyHttpClient.isConnected(CommentActivity.this)) {  // 联网的时候
            MyHttpClient.get(url, null, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                    Toast.makeText(CommentActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(int i, Header[] headers, String s) {
                    Gson gson = new Gson();
                    comments = gson.fromJson(s, Comments.class);
                    commentList = comments.getComments();
                    commentRecycleAdapter = new CommentRecycleAdapter(commentList, CommentActivity.this);
                    recyclerView.setAdapter(commentRecycleAdapter);
                    //当 table 中没有相应记录就插入
                    // 注意有的用户评论里带有 ' 单引号，这时插入就会报语法错误，所以需要把 ' 转化为 ''(两个单引号),这样在 sql 语法中就相当于一个 '
                    db.execSQL("insert into json(myid,json) select " + id + ",'" + s.replaceAll("'","''") + "' where not exists (select * from json where myid = " + id + ")");
                    db.execSQL(" delete from json  where (select count(1) from json)>8 "); // 行数 > 8 就清空表
                    db.close();
                }
            });
        }else {  // 没网时
            Cursor cursor = db.rawQuery("select * from json where myid = " + id, null);
            if(cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndex("json"));
                Gson gson = new Gson();
                comments = gson.fromJson(s, Comments.class);
                commentList = comments.getComments();
                commentRecycleAdapter = new CommentRecycleAdapter(commentList, CommentActivity.this);
                recyclerView.setAdapter(commentRecycleAdapter);
                db.close();
            }

        }
    }

    //被其他 Activity 调用，跳转并传值
    public static void actionStart(Context context, String id){
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {  //顶部菜单栏监听
        int id = item.getItemId();
        if( id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
