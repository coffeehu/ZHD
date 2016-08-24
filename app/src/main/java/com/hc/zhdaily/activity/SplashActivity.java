package com.hc.zhdaily.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hc.zhdaily.R;
import com.hc.zhdaily.data.SplashData;
import com.hc.zhdaily.util.GetBitmap;
import com.hc.zhdaily.util.GetJson;
import com.hc.zhdaily.util.MyHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;


import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016-07-07.
 *
 * 启动图片数据获取，得到 json 数据，img 字段就是 img url
 * http://news-at.zhihu.com/api/4/start-image/1080*1776
 */
public class SplashActivity extends AppCompatActivity {
    final String TAG = "SplashActivityTest";
    private ImageView imageView;
    private ScaleAnimation scaleAnimation = null;
    private File imgFile;
    private SplashData splashData;
    private File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        //获取当天系统时间，用时间做图片名字
        imgFile = getImgFile();
        dir = getFilesDir(); // 文件目录的路径 /data/data/com.hc.zhdaily/files/

        //设置动画效果
        initAnimation();

        imageView = (ImageView) findViewById(R.id.image);
        if(imgFile.exists()) {
            Log.d(TAG,"imgFile is exists");
            //--------------------位图加载的优化写法--------------------
            InputStream is = null;
            try {
                is = new FileInputStream(imgFile.getAbsolutePath());
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTempStorage = new byte[100 * 1024];
            options.inPurgeable = true;
            options.inSampleSize = 4;
            options.inInputShareable = true;
            Bitmap bitmap =BitmapFactory.decodeStream(is,null, options);
            imageView.setImageBitmap(bitmap);
            //imageView.setImageResource(R.drawable.splash);
            //-----------------------------------------------------------
            // 用下面这种方法，app多打开几次就会引起 OutOfMemoryError
            //imageView.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            imageView.startAnimation(scaleAnimation);
        }else {
            Log.d(TAG,"imgFile not exists");
            final Handler handler = new Handler(){  // 使用 handler message 获取 json 数据，json 里有 imageUrl
                public void handleMessage(final Message message){
                    if (message.what == 0){
                        String response = (String) message.obj;
                        Gson gson = new Gson();
                        splashData = gson.fromJson(response, SplashData.class);
                        final String imgUrl = splashData.getImg().replace("s:", ":"); // 把 https 改为 http

                        //获取图片，也要开启一个新线程
                        final Handler handler1 = new Handler(){
                            public void handleMessage(Message message1){
                                if (message1.what == 1){
                                    Bitmap bitmap = (Bitmap) message1.obj;
                                    if(bitmap != null){
                                        Log.d(TAG,"bitmap != null");
                                        imageView.setImageBitmap(bitmap);
                                        imageView.startAnimation(scaleAnimation);
                                        emptyDir(dir);
                                        saveImage(imgFile, bitmap); // 将图片保存到本地
                                    }else {
                                        Log.d(TAG,"bitmap == null");
                                        imageView.setImageResource(R.drawable.splash);
                                        imageView.startAnimation(scaleAnimation);
                                    }
                                }
                            }
                        };
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = GetBitmap.getBitmap(imgUrl);
                                Message message1 = new Message();
                                message1.what = 1;
                                message1.obj = bitmap;
                                handler1.sendMessage(message1);
                            }
                        }).start();
                        //--获取图片 end--
                        // 当获取不到 json 数据时，使用本地图片
                    }else if (message.what == 2){
                        imageView.setImageResource(R.drawable.splash);
                        imageView.startAnimation(scaleAnimation);
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String response = GetJson.getJson("http://news-at.zhihu.com/api/4/start-image/1080*1776");
                    Message message = new Message();
                    message.obj = response;
                    if( response != null) {
                        message.what = 0;
                        handler.sendMessage(message);
                    }else {  // 当获取不到 json 数据时
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                }
            }).start();
        }
    }

    //获取当天系统时间，用时间做图片名字
    private File getImgFile(){
        Calendar c = Calendar.getInstance();
        String month = "0"+(c.get(Calendar.MONTH)+1)+"";
        String day = "0"+c.get(Calendar.DAY_OF_MONTH)+"";
        String time = month + day;
        File dir = getFilesDir();
        Log.d(TAG, "time ="+time+",dir ="+dir);
        return imgFile = new File(dir, time+".jpg");
    }

    //设置动画效果
    private void initAnimation(){
        scaleAnimation = new ScaleAnimation(1.0f , 1.2f , 1.0f , 1.2f ,
                Animation.RELATIVE_TO_SELF , 0.5f , Animation.RELATIVE_TO_SELF , 0.5f );
        scaleAnimation.setDuration(3000);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG,"animation start");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG,"animation end");
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //??看不出效果
                //两个activity切换时的动画
                //2个参数：进入的动画、出去的动画
                //必须在startActivity()或finish()之后立即使用
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    // 把 bitmap 保存到本地
    public void saveImage(File file, Bitmap bitmap){
        Log.d(TAG,"save img");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // 清空 file 目录下的文件，这里 file = getFilesDir(); /data/data/com.hc.zhdaily/files/
     public  void emptyDir(File file) {
           if(file.isDirectory()){
              File[] childFiles = file.listFiles();
             for (int i = 0; i < childFiles.length; i++) {
                 childFiles[i].delete();
                }
           }
     }

}
