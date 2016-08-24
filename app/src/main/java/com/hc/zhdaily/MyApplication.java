package com.hc.zhdaily;

import android.app.Application;

import com.hc.zhdaily.util.ImageCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Administrator on 2016-07-10.
 */
public class MyApplication extends Application {
    private ImageCache imageCache;

    @Override
    public void onCreate() {
        super.onCreate();

        imageCache = new ImageCache(getApplicationContext());

        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
    }

    public ImageCache getImageCache(){return  imageCache;}

}
