package com.hc.zhdaily.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hc.zhdaily.R;
import com.hc.zhdaily.util.OptimizeImage;

/**
 * Created by hc on 2016/8/17.
 * 一个 左边是 图标 右边是 文字 的 ViewGroup，
 * 与IconTExtButton 不同的是，这个左边的图片是圆形图片。并且实际是一个 LinearLayout 布局
 * 其 图片、text 都不能在 xml 文件中设置，需要调用这个类的 setImage()、setText()来进行设置
 */
public class CircleImageText extends LinearLayout {
    private Bitmap bitmap = null;
    private TextView textView;

    public CircleImageText(Context context){
        super(context);
    }

    public CircleImageText(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        View view = LayoutInflater.from(context).inflate(R.layout.view_circleimagetext_ll, this, true);
        textView = (TextView) view.findViewById(R.id.circleimagetext_ll_text);

        // 圆形图片使用：
        //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loli);// 获得默认图片
        bitmap = OptimizeImage.decodeBitmapFromResource(context, getResources(), R.drawable.loli, 70, 70); // 获得默认图片 并设置分辨率
        ImageView imageView = (ImageView) view.findViewById(R.id.circleimagetext_ll_img); // 获得 ImageView（一个普通的 ImageView就行，因为这里是使用 drawable 做的圆形图片）
        imageView.setImageDrawable(new CircleImageDrawable(context, bitmap));  // 设置 drawable ---- 一个圆形图片
    }

    public void setImage(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void setImage(int resourceId){
        bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
    }

    public void setText(String text){
        textView.setText(text);
    }
}
