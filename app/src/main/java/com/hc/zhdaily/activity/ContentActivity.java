package com.hc.zhdaily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hc.zhdaily.data.Content;
import com.hc.zhdaily.util.MyHttpClient;
import com.hc.zhdaily.R;
import com.hc.zhdaily.view.MyScrollView;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016-07-14.
 */
public class ContentActivity extends AppCompatActivity  {
    private WebView mWebView;
    private Content content;
    protected MyScrollView myScrollView;
    private Toolbar toolbar;
    private ImageView imageView;
    private FrameLayout frameLayout,frameimgLayout;
    private LinearLayout linearLayout;
    private TextView textTitle;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String image = intent.getStringExtra("image");
        Log.d("ContentActivityTest","image ="+image);


        //toolbar 的设置
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);//折叠后toolbar最左会有返回箭头
        getSupportActionBar().setTitle("");

        //图片上的文字
        textTitle = (TextView) findViewById(R.id.title);
        textTitle.setText(title);

        frameimgLayout = (FrameLayout) findViewById(R.id.frame_img);
        if(image == null){   //有些文章内容没有头部大图，会空出一块，所以判断，当没图片时，height = 0
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) frameimgLayout.getLayoutParams();
            layoutParams.height = 0;
            frameimgLayout.setLayoutParams(layoutParams);
        };

        //头部大图设置
        imageView = (ImageView) findViewById(R.id.image);
        final ImageLoader imageloader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageloader.displayImage(image, imageView, options);


        //webView 初始化
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);

        //http://news-at.zhihu.com/api/4/news/3892357

        MyHttpClient.get("news/" + id, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                s = s.replaceAll("'", "''");
                parseJson(s);
            }
        });



        linearLayout = (LinearLayout) findViewById(R.id.linear);  // 初始，toolbar 在 linear 中
        frameLayout = (FrameLayout) findViewById(R.id.frame);  // 滚动时把 toolbar 换到 frame 中
        myScrollView = (MyScrollView) findViewById(R.id.scrollView);
        myScrollView.setOnScrollChangeListener(new MyScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int l, int t, int oldl, int oldt) {
                frameimgLayout.scrollTo(l, -t/2);
               // imageView.scrollTo(l, -t/2); //scrollView 上滑 t 距离的时候，imageView 下滑 t/2.也就是说图片上滑距离为 t - t/2 距离，造成滚动差。


                // 当滚动时，toolbar 被移除，加到 根的 frameLayout 中，这样就能一直固定在顶部
                if(t > 0) {
                    if (toolbar.getParent() != frameLayout) {
                        linearLayout.removeView(toolbar);
                        frameLayout.addView(toolbar);
                    }
                }


                int height = imageView.getMeasuredHeight();//imageView的高
                float f = (float) t / (float)height;  //为什么要设置这个比例？ 因为我们目的是上滑过正好一个 imageView 的距离，toolbar正好完全渐隐消失
                if( f >= 1){
                    f = 1;   //表明 f 再大也只能是 1
                }


                if(t < height) {   //这个判断表示滚动的距离还没有没过 image 的高度时才执行里面的代码。
                                    // 不然当你滚动到内容部分时（此时 t > height），点击屏幕，依旧会触发这个 onScrollChange()，然后就会执行下面的代码
                    float num = (float) 1 - (float)(f*1);
                    toolbar.setAlpha(num);//根据滑动距离，慢慢改变透明度，造成渐隐效果

                    //查看 log 发现，这里 num 的数值，最低是0.0999之类的，所以取小数点后一位，好判断
                    //float mnum = (float) (Math.round(num * 10))/10; //方法1 ，保留小数点后 1 位。round 四舍五入
                    String tmp = (num+"").substring(0,3);//方法2，直接转化为String，取前三位。前提是知道小数点前固定是一位。
                    float mnum = Float.parseFloat(tmp);

                    if (mnum == 0.0) toolbar.setVisibility(View.GONE);
                    if (mnum > 0.0) toolbar.setVisibility(View.VISIBLE);
                }


                // 在上滑过 toolbar 和 imageView 的时候，此时若下滑，此时马上让 toolbar 显示
                int yVelocity = myScrollView.getyVelocity(); //获取垂直滑动速度
                // Log.d("MyScrollTest","yVelocity ="+yVelocity);
                int height2 = height + toolbar.getMeasuredHeight(); // 图片 + toolbar 的高
                if(t > height2){ // 这里判断是滑过了 toolbar 和 ImageView，到达内容的部分
                    if( (t - oldt) <= 0){  // 判断是下滑
                        if(yVelocity >= 200 ) {
                            toolbar.setAlpha(1f);  //设速度大于 100 时，就显示 toolbar
                            toolbar.setVisibility(View.VISIBLE);
                        }
                    }else {
                        toolbar.setVisibility(View.GONE);//若是上滑,直接 GONE
                        // toolbar.setAlpha(0); //若是上滑，就把 toolbar 设置透明
                    }
                }

                // 通知标题栏刷新显示
                toolbar.invalidate();
            }
        });




    }//----onCreate END--


    private void parseJson(String responseString) {
        Gson gson = new Gson();
        content = gson.fromJson(responseString, Content.class);


        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        //news.css的路径：projec 模式下 app/src/main/ 下新建一个名为 assets 的文件夹（注意有个 s ，但引用的时候没有 s），assets/css/news.css
        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");

        mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_toolbar_menu, menu);
        return true;
    }



    //左上角 toolbar 的返回箭头的监听事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                if(item.getItemId() == android.R.id.home)
                {
                    finish();
                    return true;
                }
                return super.onOptionsItemSelected(item);
            case R.id.content_toolbar_comment:
                CommentActivity.actionStart(ContentActivity.this, ContentActivity.this.id);
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
