package com.hc.zhdaily.fragment;


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

import com.google.gson.Gson;
import com.hc.zhdaily.util.MyHttpClient;
import com.hc.zhdaily.R;
import com.hc.zhdaily.activity.MainActivity;
import com.hc.zhdaily.adapter.MenuAdapter;
import com.hc.zhdaily.data.ThemeContent;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016-07-10.
 *
 * 不同主题的显示内容
 */
public class MenuDataList extends Fragment {

    private String id;
    private ThemeContent themeContent;
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
         View view = inflater.inflate(R.layout.menu_datalist , container , false);

        String title = getArguments().getString("title"); //用来获取传入的参数
        id = getArguments().getString("id"); //用来获取传入的参数
        Log.d("test2","title="+title+",id="+id);
        MainActivity.setToolbarTitle(title);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);



        //http://news-at.zhihu.com/api/4/theme/11
        getData("theme/"+id);

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

        return view;
    }


    private void getData(String url){
        MyHttpClient.get(url, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                Gson gson = new Gson();
                themeContent = gson.fromJson(s , ThemeContent.class);
                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new MenuAdapter(themeContent,getContext());
                recyclerView.setAdapter(adapter);

                //--------------滑动事件---------------

            }//onSuccess end
        });
    }


    //在MainActivity页面跳转到该Fragment时，activity需要传参数title和id，则调用该方法传参。
    public static Fragment newInstance(String title,String id){
        MenuDataList menuDataList = new MenuDataList();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("id",id);
        menuDataList.setArguments(bundle);
        return menuDataList;
    }


}
