package com.hc.zhdaily.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

import com.hc.zhdaily.R;

/**
 * Created by hc on 2016/8/17.
 *
 * 一个 左边是 图标 右边是 文字 的 自定义view
 * 如 navigtion 的 head 的 “我的收藏” “离线下载”
 */
public class IconTextButton extends Button {

    private int resourceId = 0;
    private Bitmap bitmap;
    private int textWidth;

    public IconTextButton(Context context) {
        super(context,null);
    }

    public IconTextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        //this.setClickable(true);
        resourceId = R.drawable.star;
        bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
    }

    public void setIcon(int resourceId)
    {
        this.bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        invalidate();
    }


    @Override
    protected  void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        // int mHeigth = Math.max(bitmap.getHeight(), (int)this.getTextSize());
        String textString =  this.getText().toString();
        Paint paint = new Paint();
        paint.setTextSize(this.getTextSize());
        textWidth = (int) paint.measureText(textString);
        int mWidth = textWidth + bitmap.getWidth();

        Paint.FontMetrics fm = paint.getFontMetrics();
        int mHeigth = (int)Math.max((fm.bottom - fm.top), bitmap.getHeight());


        if(heightSpecMode == MeasureSpec.AT_MOST && widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, mHeigth);
        }else if(heightSpecMode == MeasureSpec.AT_MOST ){
            setMeasuredDimension(widthSpecSize, mHeigth);
        }else if(widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, heightSpecSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub

        // 图片水平方向靠最左，竖直方向居中显示
        int y = this.getMeasuredHeight()/2 - bitmap.getHeight()/2;
        int x = 0;
        canvas.drawBitmap(bitmap, x, y, null);
        // 坐标需要转换，因为默认情况下Button中的文字居中显示
        // 这里需要让文字靠右显示
        canvas.translate(  ((this.getMeasuredWidth()-textWidth)/2), 0);
        super.onDraw(canvas);
    }

}
