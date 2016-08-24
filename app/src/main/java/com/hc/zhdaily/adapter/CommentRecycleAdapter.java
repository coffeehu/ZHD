package com.hc.zhdaily.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hc.zhdaily.MyApplication;
import com.hc.zhdaily.R;
import com.hc.zhdaily.data.Comment;
import com.hc.zhdaily.util.GetBitmap;
import com.hc.zhdaily.util.GetTime;
import com.hc.zhdaily.util.ImageCache;
import com.hc.zhdaily.view.CircleImageDrawable;
import com.hc.zhdaily.view.IconTextButton;

import java.security.acl.Group;
import java.util.List;

import java.util.logging.LogRecord;

/**
 * Created by hc on 2016/8/18.
 */
public class CommentRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SHORT_COMMENT = 001;
    private List<Comment> commentList;
    private Context context;
    private ImageCache imageCache;

    public CommentRecycleAdapter(List<Comment> commentList, Context context){
        Log.d("CommentAdapter","1 . ComentAdapter construct called");
        this.commentList = commentList;
        this.context = context;
        imageCache = ((MyApplication)((Activity)context).getApplication()).getImageCache();  // 通过 Application 获取 ImageCache 的实例
    }

    @Override
    public int getItemViewType(int position){
        return SHORT_COMMENT;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        switch (viewType){
            case SHORT_COMMENT:
                ShortHolder shortHolder = new ShortHolder(LayoutInflater.from(context).inflate(R.layout.comment_short, parent, false));
                return shortHolder;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position){
        if(viewHolder instanceof ShortHolder){
           // Log.d("CommentTest","content = "+commentList.get(1).getContent());
            final Comment comment =  commentList.get(position);
            ((ShortHolder) viewHolder).author.setText(comment.getAuthor());
            ((ShortHolder) viewHolder).author.setTypeface(Typeface.DEFAULT_BOLD);//设为粗体，然而只有英文是粗体
            ((ShortHolder) viewHolder).content.setText(comment.getContent());
            String time = GetTime.getTime(comment.getTime());
            ((ShortHolder) viewHolder).time.setText(time);
            ((ShortHolder) viewHolder).like.setIcon(R.drawable.like);
            ((ShortHolder) viewHolder).like.setText(comment.getLikes());
            Log.d("CommentAdapter","3 . onBindViewHolder called");
            imageCache.disPlayImage(comment.getAvatar(), ((ShortHolder) viewHolder).avatar);  // 内存缓存图片并显示
        }
    }


    @Override
    public int getItemCount(){
        return commentList.size();
    }

    class ShortHolder extends RecyclerView.ViewHolder{
        TextView author, content, time;
        ImageView avatar;
        IconTextButton like;
        public ShortHolder(View view){
            super(view);
            author = (TextView) view.findViewById(R.id.author);
            content = (TextView) view.findViewById(R.id.content);
            time = (TextView) view.findViewById(R.id.time);
            avatar = (ImageView) view.findViewById(R.id.avatar);
            like = (IconTextButton) view.findViewById(R.id.like);
        }
    }

}
