package com.hc.zhdaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hc.zhdaily.R;
import com.hc.zhdaily.activity.ContentActivity;
import com.hc.zhdaily.data.Story;
import com.hc.zhdaily.data.ThemeContent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016-07-12.
 *
 *  每个主题的 recyclerView 的 adapter。----感觉可以和 MainAdapter 复用
 *
 */
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ImageLoader imageloader;
    private DisplayImageOptions options;
    private List<Story> stories;
    private Story story;
    private String imageUrl; //head img
    private String descrption; //head descrption
    private Context context;
    private static final int IMG_ITEM = 001;
    private static final int TEXT_ITEM = 002;
    private static final int HEAD_ITEM = 003;

    public MenuAdapter(ThemeContent themeContent, Context context){
        this.context = context;
        this.stories = themeContent.getStories();
        imageUrl = themeContent.getImage();
        descrption = themeContent.getDescription();
        imageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        switch (viewType) {
            case IMG_ITEM:
                ImageHolder imageHolder = new ImageHolder(LayoutInflater.from(context).inflate(R.layout.menu_datalist_item, parent, false));
                return imageHolder;
            case TEXT_ITEM:
                TextHolder textHolder = new TextHolder(LayoutInflater.from(context).inflate(R.layout.menu_datalist_item2, parent, false));
                return textHolder;
            case HEAD_ITEM:
                HeadHolder headHolder =  new HeadHolder(LayoutInflater.from(context).inflate(R.layout.menu_datalist_headitem, parent, false));
                return headHolder;
            default:
                return null;
        }
    }

    @Override
    public  void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
        if(position == 0 ){
            ((HeadHolder)holder).textView.setText(descrption);
            imageloader.displayImage(imageUrl ,((HeadHolder)holder).imageView ,options );
        }else {
            story = stories.get(position-1);
            if(holder instanceof ImageHolder) {
                ((ImageHolder)holder).textView.setText(story.getTitle());
                imageloader.displayImage(story.getImages().get(0), ((ImageHolder)holder).imageView, options);
                holder.itemView.setTag(story);//将每个itemView的数据存起来
                ((ImageHolder)holder).cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = ((Story)view.getTag()).getTitle();//
                        String id = ((Story)view.getTag()).getId()+"";
                        String image = ((Story)view.getTag()).getImages().get(0);
                        //Toast.makeText(context ,tmp+","+tmp2+","+tmp3 ,Toast.LENGTH_LONG).show(); //cardview Listen
                        Intent intent = new Intent(context , ContentActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("title",title);
                        intent.putExtra("image",image);
                        context.startActivity(intent);
                    }
                });
                // Log.d("test2","position:"+position+",title="+story.getTitle()+",img="+story.getImages().get(0));
            }else {
                ((TextHolder)holder).textView.setText(story.getTitle());
                holder.itemView.setTag(story);
                ((TextHolder)holder).cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = ((Story)view.getTag()).getId()+"";
                        String title = ((Story)view.getTag()).getTitle();
                        Intent intent = new Intent(context , ContentActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("title",title);
                        context.startActivity(intent);

                        //Toast.makeText(context ,((Story)view.getTag()).getTitle()+","+tmp2 ,Toast.LENGTH_LONG).show(); //cardview Listen
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return stories.size()+1;
    }

    @Override
    public int getItemViewType(int position){
            if (position == 0) {
                return HEAD_ITEM;
            } else if (stories.get(position - 1).getImages() == null) {
                return TEXT_ITEM;
            } else {
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

    class TextHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView cardView;
        public TextHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.item2_text);
            cardView=(CardView) view.findViewById(R.id.item2_cardview);
        }
    }

    class HeadHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public HeadHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.item_head_image);
            textView = (TextView) view.findViewById(R.id.item_head_text);
        }
    }

}