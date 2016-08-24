package com.hc.zhdaily.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hc.zhdaily.R;
import com.hc.zhdaily.activity.ContentActivity;
import com.hc.zhdaily.data.TopStory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-14.
 *
 * 图片轮播，基于 ViewPager
 */
public class Header extends FrameLayout implements View.OnClickListener {

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Context context;
    private List<TopStory> topStories;

    private ViewPager viewPager;
    private List<View> viewList;

    private Handler handler;

    private static boolean isPlay = true;

    private LinearLayout linearLayout;
    private List<ImageView> imageViewList;

    //private int tag ;




    public Header(Context context , AttributeSet attrs , int defStyle){
        super(context,attrs,defStyle);
        Log.d("testx"," Head's construct be called");
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true).build();

        this.viewList = new ArrayList<>();
        this.imageViewList = new ArrayList<>();

      //  this.tag = 0; //
    }


    public Header(Context context,AttributeSet attrs){
        this(context,attrs,0);
    }
    public Header(Context context){
        this(context,null);
    }


    public void setData(List<TopStory> topStories){
        Log.d("testx"," setData be called");
        this.topStories = topStories;
        viewList.clear();
        Log.d("test8","setData be called,title="+topStories.get(0).getTitle());
        View view_layout = LayoutInflater.from(context).inflate(R.layout.main_head_item_layout,this,true);
        viewPager = (ViewPager) view_layout.findViewById(R.id.head_item_layout_viewpager);

        //设置当手指触摸滑动时候，停止自动播放。滑动完毕，开启自动播放
        //设置跟着滑动的圆点图片
        viewPager.addOnPageChangeListener(new MyPagerChangeListener());


        //--------添加轮播的view内容
        int len = topStories.size();
        for (int i = 0; i < len; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.main_head_item_content, null);
            ImageView image = (ImageView) view.findViewById(R.id.head_item_content_image);
            TextView title = (TextView) view.findViewById(R.id.head_item_content_title);

            imageLoader.displayImage(topStories.get(i).getImage(), image, options);
            title.setText(topStories.get(i).getTitle());
            view.setOnClickListener(this);
            //Log.d("test8","setData be called,title="+topStories.get(i).getTitle());

            viewList.add(view);
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setCurrentItem(0);
        // viewPager.setFocusable(true);


          //  if(tag == 0){
            //------设置图片上滑动的点
            linearLayout = (LinearLayout) view_layout.findViewById(R.id.head_item_layout_linearlayout);
            linearLayout.removeAllViews();
            for (int i = 0; i < len; i++) {
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 5;
                params.rightMargin = 5;
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT;

                if (i == viewPager.getCurrentItem()) {
                    imageView.setImageResource(R.drawable.dot_focus);
                } else {
                    imageView.setImageResource(R.drawable.dot_blank);
                }
                linearLayout.addView(imageView, params);
                imageViewList.add(imageView);
            }


            startPlay();
   //     }//---- tag = 0


    }


    public void startPlay(){
        //tag += 1;
        Log.d("testx","startPlay() be called");
        handler = new Handler();
        handler.postDelayed(task , 4000);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if(isPlay) {
                Log.d("testx","task run() is play");
                int currentItem = viewPager.getCurrentItem();
                if (currentItem < viewList.size() - 1) {
                    Log.d("testx","currentItem normal move,currentItem = "+currentItem);
                    viewPager.setCurrentItem(currentItem + 1, true);//true表示平滑变化？
                    handler.postDelayed(task, 4000);
                } else {
                    Log.d("testx","currentItem JUMP move!currentItem = "+currentItem);
                    viewPager.setCurrentItem(0, true);
                    handler.postDelayed(task, 4000);
                }
            }else {
                Log.d("testx","task run() flase play");
                handler.postDelayed(task, 4000);
            }
        }//---- run() end
    };//---- task end



    public class MyPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
    }


    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrollStateChanged(int arg0) {
            /**
             * arg0
             *  1:表示正在滑动
             *  2:表示滑动完毕
             *  0:表示什么都没做，就是停在那
             */
            switch (arg0){
                case 0:
                    isPlay = true;
                    break;
                case 1:
                    isPlay = false;
                    break;
                case 2:
                    isPlay = true;
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            /**
             * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法会一直得到调用
             * arg0:当前页面，即点击滑动的页面
             * arg1:当前页面偏移的百分比
             * arg2:当前页面偏移的像素位置
             */
        }

        @Override
        public void onPageSelected(int arg0) {
            /**
             *arg0 当前选中的页面，在页面跳转完毕的时候调用的。
             */
            for(int i=0;i<imageViewList.size();i++) {
                if(i == arg0) {
                    imageViewList.get(i).setImageResource(R.drawable.dot_focus);
                }else {
                    imageViewList.get(i).setImageResource(R.drawable.dot_blank);
                }
            }
        }//onPageSelected  end.
    }// MyPagerChangeListener end.



    @Override
    public void onClick(View view){
        int position = viewPager.getCurrentItem();
        TopStory topStory = topStories.get(position);

        Intent intent = new Intent(context , ContentActivity.class);
        intent.putExtra("id",topStory.getId()+"");
        intent.putExtra("title",topStory.getTitle());
        intent.putExtra("image",topStory.getImage());
        context.startActivity(intent);
    }

    public static void setIsPlay(boolean b){ isPlay = b;}

}
