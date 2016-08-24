package com.hc.zhdaily.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.hc.zhdaily.MyApplication;
import com.hc.zhdaily.R;
import com.hc.zhdaily.view.CircleImageDrawable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hc on 2016/8/20.
 *
 * 图片内存缓存。LruCache 内部的数据结构是 LinkedHashMap 存储的。这样，LruCache 就达到了缓存最近 put 的 n 个数据。
 * 本 app 用作评论页面的用户头像缓存。具体作用：
 * 当 recyclerview 的 item 滑出屏幕再滑回来时，recyclerview 的 adapter 的 onBindViewHolder() 会重新调用。所以有了这个缓存，重新调用时就不用重新请求网络了。
 * 情景1：针对一个活动
 * 若没有设置这个缓存，有网的情况下打开评论页面，此时再断网。上下滑动 item，让所有 item 滑出屏幕再滑回。会发现所有用户的头像都统一变为默认头像。
 *
 * 情景2：活动跳转
 * 要在 Application （这里是 MyApplication 继承了 Application）中创建这个对象，这样对象才会存在整个生命周期，直到所有活动销毁才被回收；
 * 在活动中直接引用： getApplication.getImageCache()
 * 如果只在一个活动中创建对象，那么这个活动页面关闭，这个缓存也回收了，起不到缓存效果。
 */
public class ImageCache {
    private LruCache<String,Bitmap> mMemoryCache;
    private Context context;
    private ExecutorService executorService = null;

    public ImageCache(Context context){
        this.context = context;
        executorService = Executors.newFixedThreadPool(2); //线程池，2 个线程
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int mCacheSize = maxMemory / 8;
        Log.d("CommentAdapter","2 . maxMemory ="+maxMemory+",cachesize ="+mCacheSize);
        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value){
                return value.getRowBytes() * value.getHeight();
                /**
                 * 1、getRowBytes：Since API Level 1，用于计算位图每一行所占用的内存字节数。
                 2、getByteCount：Since API Level 12，用于计算位图所占用的内存字节数。
                 经实测发现：getByteCount() = getRowBytes() * getHeight()，也就是说位图所占用的内存空间数等于位图的每一行所占用的空间数乘以位图的行数。
                 因为getByteCount要求的API版本较高，因此对于使用较低版本的开发者，在计算位图所占空间时上面的方法或许有帮助。
                 */
            }
        };
    }


    private void addBitmapToMem(String key, Bitmap bitmap){
        if(getBitmapFromMem(key) == null && bitmap != null){
            mMemoryCache.put(key,bitmap);
            Log.d("CommentAdapter","add caache!,size ="+mMemoryCache.size());
        }
    }


    private Bitmap getBitmapFromMem(String key){
        return mMemoryCache.get(key);
    }


    public void disPlayImage(final String url, final ImageView view){
        //加载网络图片(用户头像)，不能写在主线程，所以用 handler message
        final Handler handler = new Handler() {
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        Bitmap bitmap = (Bitmap) msg.obj;
                        view.setImageDrawable(new CircleImageDrawable(context, bitmap));
                        break;
                    default:
                        break;
                }
            }
        };
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("ImageCacheTest",Thread.currentThread().getName()+" running....");
                Log.d("CommentAdapter","4 . display run() called");
                Message message = new Message();
                Bitmap bitmap = null;
                String subUrl = url.replaceAll("[^\\w]", ""); // 去掉 // : . 这些，将 subUrl 作为 key 保存
                Log.d("CommentAdapter","----imgUrl ="+url+", suburl ="+subUrl);
                if(getBitmapFromMem(subUrl) != null){  //有缓存时
                    Log.d("CommentAdapter","imgcache work!");
                    bitmap = getBitmapFromMem(subUrl);
                    message.what = 0;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                }else { // 找不到内存缓存时,就用文件缓存
                    Log.d("CommentAdapter","imgcache not work! start file caache");

                    //---------- 文件缓存 伪代码--------
                    File tmpDir = context.getCacheDir();
                    File imgFile = new File(tmpDir, "MyImgCache/"+subUrl+".jpg"); // 以 imgurl 为文件名， 获得文件名绝对路径
                    File dir = new File(tmpDir, "MyImgCache/");
                    Log.d("CommentAdapter","dir ="+dir);
                    if(!dir.exists()) {
                        dir.mkdirs();  // 目录不存在则创建目录  mkdir() 只能创建一个目录，mkdirs() 可创建多级目录
                        Log.d("CommentAdapter","mkdir!");
                    }
                    Log.d("CommentAdapter","imgFile ="+imgFile.toString()+", imgfile path ="+imgFile.getAbsolutePath());
                    if(imgFile.exists()){
                        bitmap = getBitmapFromSD(imgFile);
                        updateFileTime(imgFile); // 每调用一次文件，就将该文件的最后修改时间设为当前
                        Log.d("CommentAdapter","file cache work");
                    }else {  // 如果文件缓存也没有，那就只有网络请求了
                        if (url != null){
                            bitmap = GetBitmap.getBitmap(url); // 这是一个网络请求，通过 url 获得 bitmap
                            Log.d("CommentAdapter","file cache not work and start network request,url ="+url);
                            if(bitmap != null) {
                                addBitmapToMem(subUrl, bitmap);
                                addBitmapToSD(imgFile, bitmap);
                               deletBitmapFromSD(dir); // 根据策略，删除一些 image file
                            }
                        }// 当 url 为 null 也没关系，圆形图片的 view 会判断，当 null 时设置默认图片
                    }

                    //------------------
                    message.what = 0;
                    message.obj = bitmap;
                    handler.sendMessage(message); // 之后会执行 handleMessage
                }
            }
        });
    }


    private Bitmap getBitmapFromSD(File imgFile){
        InputStream is = null;
        try {
            is = new FileInputStream(imgFile.getAbsolutePath()); // 先把文件转化为 Stream
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTempStorage = new byte[100 * 1024];
        options.inPurgeable = true;
        options.inSampleSize = 2;
        options.inInputShareable = true;

        return BitmapFactory.decodeStream(is, null, options); // is 再转为 bitmap
    }


    private void addBitmapToSD(File imgFile, Bitmap bitmap){
        try {
            FileOutputStream fos = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 修改文件的最后修改时间
     */
    private void updateFileTime(File imgFile) {
        long newModifiedTime =System.currentTimeMillis();
        imgFile.setLastModified(newModifiedTime);
    }

    private void deletBitmapFromSD(File dir){
        /**
         * File f=new File("c:\\");
         String[] f1=f.list();
         File[] f2=f.listFiles();
         ① list() 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录。
         以C盘为例，返回的是c盘下文件夹名字的字符串数组,如[TEMP, Windows]
         ②listFiles() 返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件。
         以C盘为例返回的是C盘下文件夹目录地址，如[c:\TEMP, c:\Windows]
         */
        File[] files = dir.listFiles();
        /**
         * 不同的设备上，调用getExternalStorageDirectory()返回值却不一样。
         * 查询了Android的文档，才找到原因，原来这个方法返回的是当前设备厂商所认为的“外部存储”，
         * 有可能返回外置的SD卡目录（Micro SD Card），也可能返回内置的存储目（eMMC）。
         */
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        Log.d("CommentAdapter","--------- storagedir ="+Environment.getExternalStorageDirectory());
        Log.d("CommentAdapter","--------- storagedir getpath ="+Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize());
        Log.d("CommentAdapter","sdfree ="+sdFreeMB);
        if(files == null) return;
        Log.d("CommentAdapter","files[0] ="+files[0]);

        if(files.length > 16){
            Arrays.sort(files, new FileModifSort()); // // 根据文件的修改时间，将 files 排序
            int deleteCount = (int)(files.length / 2);  // 删掉 一半 的文件
            for (int i = 0; i < deleteCount; i++){
                files[i].delete();
            }
        }



    }

    // 根据文件的修改时间排序
    private class FileModifSort implements Comparator<File>{
        public int compare(File file1, File file2){  // Comparator 必须重写 compare()
            if(file1.lastModified() > file2.lastModified()){
                return 1;
            } else if(file1.lastModified() == file2.lastModified()){
                return 2;
            }else {
                return -1;
            }
        }
    }

}
