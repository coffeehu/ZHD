package com.hc.zhdaily.activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hc.zhdaily.fragment.MainFragment;
import com.hc.zhdaily.data.MenuData;
import com.hc.zhdaily.fragment.MenuDataList;
import com.hc.zhdaily.util.MyDatabaseHelper;
import com.hc.zhdaily.util.MyHttpClient;
import com.hc.zhdaily.R;
import com.hc.zhdaily.data.Themes;
import com.hc.zhdaily.view.CircleImageText;
import com.hc.zhdaily.view.IconTextButton;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * http://news-at.zhihu.com/api/4/themes
 * 主界面：顶部toolbar，左侧侧滑栏，内容部分是 Fragment，根据不同主题内容不同，复用fragment
 *
 */

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private static Toolbar toolbar;
    private NavigationView navigationView;
    private Themes themes;
    private List<MenuData> others;
    private MyDatabaseHelper myDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        myDatabaseHelper = new MyDatabaseHelper(this, "JsonDb.db", null, 1); // SQLiteOpenHelper对象,后面framgent要缓存json数据时调用
        initView();
        menuGetData("themes");  // URL: http://news-at.zhihu.com/api/4/themes
        //设置 navigation 的菜单栏 item 的监听事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(item.getTitle().equals("首页")){  //用 == "首页" 会返回false
                    getSupportFragmentManager().beginTransaction().replace(R.id.tmp , new MainFragment() ).commit();
                }else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.tmp, MenuDataList.newInstance((item.getTitle() + ""), getId(item.getTitle() + ""))).commit();
                    //用来自定义的newInstance方法来传入参数，这里传入了menuItem的【名字】和其对应的【id】
                    //由自定义的getId()方法，通过【名字】获得【id】
                }
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.tmp , new MainFragment() ).commit();

    }


    //为 navigation head 的三个按钮设置监听事件
    private void initView(){
        //设置 toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("首页");
        setSupportActionBar(toolbar);

        // 设置 drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this , drawerLayout , toolbar ,
                R.string.drawer_open , R.string.drawer_close );
        toggle.syncState();
        drawerLayout.setDrawerListener(toggle);

        //设置 navigation
        navigationView = (NavigationView) findViewById(R.id.navigation);
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenuView.setVerticalScrollBarEnabled(false); //隐藏滚动条
        //设置 navigation 的菜单栏 item 被选中时的 icon 和 text 的颜色。
        ColorStateList csl=(ColorStateList)getResources().getColorStateList(R.color.navigation_menu_item_color);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);

        // navigation 头部的 view 的设置
        View navigationHead = navigationView.getHeaderView(0);  //获取 headVIew，参数用 R.id.xx 会报空指针
        CircleImageText circleImageText = (CircleImageText) navigationHead.findViewById(R.id.circleimagetext);
        Button collection = (Button) navigationHead.findViewById(R.id.button2);
        IconTextButton download = (IconTextButton) navigationHead.findViewById(R.id.button3);
        circleImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
            }
        });
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "请登录", Toast.LENGTH_SHORT).show();
            }
        });
        download.setIcon(R.drawable.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "请登录", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void menuGetData(String url){
        MyHttpClient.get(url, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                Gson gson = new Gson();
                themes = gson.fromJson(s , Themes.class);
                others = themes.getOthers();

                for(int a=0;a<others.size();a++) {
                    String name = others.get(a).getName();
                    navigationView.getMenu().getItem(a+1).setTitle(name); //将第二个item标题设为name中的值
                    //navigationView.getMenu().getItem(1).getSubMenu().getItem(a).setTitle(name);
                }
            }
        });

    }

    //通过item的title获取id值
    private String getId(String title){
        String myid = null;

        for(int i = 0;i<others.size();i++) {
            if(title.equals(others.get(i).getName())){
                myid = others.get(i).getId()+"";
                break;
            }
        }
        return myid;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public static void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }

    public MyDatabaseHelper getDbHelper(){ return myDatabaseHelper; }

}
