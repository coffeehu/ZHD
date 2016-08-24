package com.hc.zhdaily.fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hc.zhdaily.data.Latest;
import com.hc.zhdaily.adapter.MainAdapter;
import com.hc.zhdaily.util.GetTodayDate;
import com.hc.zhdaily.util.MyHttpClient;
import com.hc.zhdaily.R;
import com.hc.zhdaily.data.Story;
import com.hc.zhdaily.activity.MainActivity;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016-07-09.
 * //http://news-at.zhihu.com/api/4/news/latest
 * 首页的内容界面，显示最新内容. 下拉加载过往内容
 * 使用 recyclerView
 */
public class MainFragment extends Fragment {
    private View view;
    private Latest latest; // 最新内容
    private Latest latest2; // 加载更多的内容
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager; // recyclerView 的布局Manager
    private MainAdapter adapter ;
    private String todayDate; //今天的时间，加载更多的时候拼接 url
    private boolean isLoading = false; // 一个 tag，是否在 加载更多中。默认否
    private SwipeRefreshLayout swipeRefreshLayout;
    private SQLiteDatabase db = null;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
        MainActivity.setToolbarTitle("首页");
        view = inflater.inflate(R.layout.menu_datalist , container , false);
        initView();
        getData("news/latest");   //加载数据 http://news-at.zhihu.com/api/4/news/latest
        loadMore(); // 下拉加载更多
        return view;
    }

    private void initView(){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //-------------下拉刷新SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false); //关闭刷新动画
                    }
                });
            }
        });//-----SwipeRefreshLayout end.---------
    }


    private void getData(String url){
       db = ((MainActivity)getContext()).getDbHelper().getWritableDatabase(); //从 MainActivity 传来的 MyDatabaseHelper 对象
        if( MyHttpClient.isConnected(getContext())) {  // 如果联网
            MyHttpClient.get(url, null, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//                Toast.makeText(getContext(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(int i, Header[] headers, String s) {
                    Gson gson = new Gson();
                    latest = gson.fromJson(s, Latest.class);

                    // 新建一个 Story 对象，只有一个 date 字段(默认为 null)，其他都为null，放到数据 List<Story> 的首位
                    //这样就可以判断，当 date!=null 时，为xx类型，显示 “今日热闻”或 日期
                    Story tmpStory = new Story();
                    tmpStory.setDate("今日热闻");
                    latest.getStories().add(0, tmpStory);
                    todayDate = latest.getDate(); // 获取今天的时间
                    adapter = new MainAdapter(latest, getContext());
                    recyclerView.setAdapter(adapter);

                    // 当 todayDate 不存在时，才插入 （todayDate,'s'）
                    db.execSQL("insert into json(date,json) select " + todayDate + ",'" + s + "' where not exists (select * from json where date = " + todayDate + ")");
                    db.execSQL(" delete from json  where (select count(1) from json)>4 "); // 行数 > 4 就清空表
                }
            });
        }else { //没网的时候
            Log.d("MainFragmentTest","network error");
            todayDate = GetTodayDate.getDate();
            Cursor cursor = db.rawQuery("select * from json where date = " + todayDate, null);
            if(cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndex("json"));
                Gson gson = new Gson();
                latest = gson.fromJson(s, Latest.class);
                Story tmpStory = new Story();
                tmpStory.setDate("今日热闻");
                latest.getStories().add(0, tmpStory);
                todayDate = latest.getDate(); // 获取今天的时间
                adapter = new MainAdapter(latest, getContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private void loadMore(){
        //----------设置滑动事件，实现滑到底部加载更多数据  url如：http://news.at.zhihu.com/api/4/news/before/20160707
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("MainFragmentTest", "StateChanged = " + newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("MainFragmentTest", "onScrolled,dx ="+dx+",dy ="+dy);
                if(!isLoading) {
                    final int lastVisibleItemPosion = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItemPosion + 1 == adapter.getItemCount()) {
                        isLoading = true;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addData("news/before/" + todayDate);  //http://news.at.zhihu.com/api/4/news/before/20160707
                            }
                        }, 1000);
                    }
                }// isLoading
               // int firstVisibleItemPosion = linearLayoutManager.findFirstVisibleItemPosition();
            }//onScrolled end
        });//--------------滑动事件 END.--------------
    }

    private void addData(final String url){
        if(MyHttpClient.isConnected(getContext())) {
            MyHttpClient.get(url, null, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                    isLoading = false;
                }

                @Override
                public void onSuccess(int i, Header[] headers, String s) {
                    Log.d("MainFragmentTest","url ="+url);
                    Gson gson = new Gson();
                    latest2 = gson.fromJson(s, Latest.class); //获取到的新的数据

                    /**
                     * Story类中新增了date变量，默认为null
                     * 加载更多数据时，先加载一个自己new的空Story，并赋值date，然后再加载其他正常数据。
                     * 这样在adpter中，可判断：若Story的date为null，则是正常数据。若不为空，则为topic类型
                     */
                    todayDate = latest2.getDate();
                    Story tmpStory = new Story();
                    String formatDate = formatDate(todayDate);
                    tmpStory.setDate(formatDate);
                    latest.getStories().add(tmpStory);

                    latest.getStories().addAll(latest2.getStories());
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemRemoved(adapter.getItemCount() - 1);//移除掉底端的progressbar+text
                    db.execSQL("insert into json(url,json) select '" + url + "','" + s + "' where not exists (select * from json where url = '" + url + "')");
                    isLoading = false;
                }
            });
        }else {
            Cursor cursor = db.rawQuery("select * from json where url = '" + url+"'", null);
            if(cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndex("json"));
                Gson gson = new Gson();
                latest2 = gson.fromJson(s, Latest.class); //获取到的新的数据
                todayDate = latest2.getDate();
                Story tmpStory = new Story();
                String formatDate = formatDate(todayDate);
                tmpStory.setDate(formatDate);
                latest.getStories().add(tmpStory);
                latest.getStories().addAll(latest2.getStories());
                adapter.notifyDataSetChanged();
                adapter.notifyItemRemoved(adapter.getItemCount() - 1);//移除掉底端的progressbar+text
                isLoading = false;
            }
        }
    }//---addData() end

    private String formatDate(String date) {
        //String result = date.substring(0, 4);//截取字符串，第0位开始，到第4位前面
        //result += "年";
        String result = date.substring(4, 6);
        result += "月";
        result += date.substring(6, 8);
        result += "日";
        return result;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d("MainFragmentTest","onDestroyView");
        com.hc.zhdaily.view.Header.setIsPlay(false);  //将是否图片轮播设为false
        db.close();  //关闭数据库
    }
}
