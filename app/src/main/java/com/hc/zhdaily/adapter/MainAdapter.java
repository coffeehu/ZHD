package com.hc.zhdaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hc.zhdaily.R;
import com.hc.zhdaily.activity.ContentActivity;
import com.hc.zhdaily.data.Latest;
import com.hc.zhdaily.data.Story;
import com.hc.zhdaily.view.Header;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016-07-13.
 */
public class MainAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ImageLoader imageloader;
    private DisplayImageOptions options;
    private Latest latest;
    private List<Story> stories;
    private Story story;
    private Context context;
    private static final int IMG_ITEM = 001;
    private static final int TOPIC_ITEM = 002;
    private static final int HEAD_ITEM = 003;
    private static final int FOOT_ITEM = 004;
    private static int tag = 0;

    public MainAdapter (Latest latest , Context context){
        this.latest = latest;
        this.context = context;
        this.stories = latest.getStories();

        imageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        /**
         * tag 注意在构造函数中归0
         */
        this.tag = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        switch (viewType) {
            case IMG_ITEM:
                ImageHolder imageHolder = new ImageHolder(LayoutInflater.from(context).inflate(R.layout.menu_datalist_item, parent, false));
                return imageHolder;
            case TOPIC_ITEM:
                TopicHolder topicHolder = new TopicHolder(LayoutInflater.from(context).inflate(R.layout.main_topic_item, parent, false));
                return topicHolder;
            case HEAD_ITEM:
                Log.d("testx"," onCreateViewHolder HEAD_ITEM be called");
                HeadHolder headHolder =  new HeadHolder(LayoutInflater.from(context).inflate(R.layout.main_head_item, parent, false));
                return headHolder;
            case FOOT_ITEM:
                return new FootHolder(LayoutInflater.from(context).inflate(R.layout.main_foot_item,parent,false));
            default:
                return null;
        }
    }

    @Override
    public  void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
        if(holder instanceof HeadHolder){
            Log.d("testx"," onBindViewHolder  HeadHolder be called");
            /**
             * 当head滑出屏幕，再滑回来时，adapter会重新调用setData()
             * 所以startPlay也会被调用，导致hanlder开启多个task
             * 另外图片上滑动的点也会重新设置，出现显示问题
             * 所以设置一个tag，第二次调用时，setData()不会执行。
             * tag 注意要在构造函数中归0，不然会出现 重新打开app，setData()未执行的情况
             */
            if(tag == 0){
            ((HeadHolder)holder).header.setData(latest.getTop_stories());
            }
            tag += 1;
        }else if(holder instanceof TopicHolder){
            ((TopicHolder)holder).textView.setText(stories.get(position-1).getDate());
        }else if (holder instanceof FootHolder){

        }else {
            story = stories.get(position-1);

            ((ImageHolder)holder).textView.setText(story.getTitle());
            imageloader.displayImage(story.getImages().get(0), ((ImageHolder)holder).imageView, options);
            holder.itemView.setTag(story);//将每个itemView的数据存起来
            ((ImageHolder)holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = ((Story)view.getTag()).getTitle();//view.getTag()取出之前存的数据
                    String id = ((Story)view.getTag()).getId()+"";
                    String image = ((Story)view.getTag()).getImages().get(0);
                   // Toast.makeText(context ,id+","+title+","+image ,Toast.LENGTH_LONG).show(); //cardview Listen
                    Intent intent = new Intent(context , ContentActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("title",title);
                    intent.putExtra("image",image);
                    context.startActivity(intent);
                    }
                });
                // Log.d("test2","position:"+position+",title="+story.getTitle()+",img="+story.getImages().get(0));
        }
    }

    @Override
    public int getItemCount() {
        Log.d("testx"," getItemCount be called");
        return stories.size()+2;
    }

    @Override
    public int getItemViewType(int position){
        Log.d("testx"," getItemViewType be called");
        if(position == 0){
            return HEAD_ITEM;
        }else if (position+1 == getItemCount()) {
            return FOOT_ITEM;
        } else if(stories.get(position-1).getDate() != null){
            return TOPIC_ITEM;
        }else {
            return IMG_ITEM;
        }
    }



    class ImageHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        CardView cardView;
        public ImageHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.item_text);
            imageView = (ImageView) view.findViewById(R.id.item_image);
            cardView=(CardView) view.findViewById(R.id.item_cardview);
        }
    }

    class TopicHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public TopicHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.topic);
        }
    }

    class HeadHolder extends RecyclerView.ViewHolder {
        Header header;
        public HeadHolder(View view){
            super(view);
            Log.d("testx","HeadHolder's constuct be called");
            header = (Header) view.findViewById(R.id.header);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {
        public FootHolder(View view){
            super(view);
        }
    }


}
