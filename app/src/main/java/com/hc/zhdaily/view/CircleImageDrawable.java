package com.hc.zhdaily.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.hc.zhdaily.R;
import com.hc.zhdaily.util.OptimizeImage;

/**
 * Created by hc on 2016/8/17.
 *
 * 创建一个圆形图片，构造参数为 Context 、 Bitmap
 */
public class CircleImageDrawable extends BitmapDrawable {

    /**
     * 流程：
     *  1、先调用 构造函数
     *  2、getIntrinsicWidth() 和 getIntrinsicHeight()
     *  3、 setBounds()  2 次
     *  4、 draw()
     *
     */
    private Paint paint;//画笔
    private Bitmap bitmap;//我们要操作的Bitmap
    private RectF rectF;//矩形f
    private int mWidth;
    private int mHeight;
    private int mX;
    private int mY;
    private int radius;

    public CircleImageDrawable(Context context, Bitmap bitmap) {
        Log.d("ImageDrawableTest","constructor");
        this.bitmap = bitmap;
        paint = new Paint();//初始化画笔
        paint.setAntiAlias(true);//抗锯齿
        //位图渲染器(参数1:我们要操作的Bitmap,参数2.3:X轴,Y轴的填充类型,
        // 类型一共有三种,REPEAT:重复类型,CLAMP:拉伸类型(注意这里的拉伸是指拉伸图片的而最后一个像素),MIRROM:镜像类型)
        BitmapShader shader = null;
        if(bitmap != null) {
            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP); //这里 TileMode 设置哪个效果都一样why？？？？
        }else { // 有些人没传头像，所以加一句，不然空指针
            this.bitmap = OptimizeImage.decodeBitmapFromResource(context, context.getResources(),  R.drawable.loli, 50, 50);// 优化下默认图片的大小
            //this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.loli);
            shader = new BitmapShader(this.bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        }
        paint.setShader(shader);
    }

    /**
     * 这个方法是指drawbale将被绘制在画布上的区域
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     *
     *
     * RectF : RectF holds four float coordinates for a rectangle.
     * The rectangle is represented by the coordinates of its 4 edges (left, top, right bottom).
     * These fields can be accessed directly. Use width() and height() to retrieve the rectangle's width and height.
     * Note: most methods do not check to see that the coordinates are sorted correctly (i.e. left <= right and top <= bottom).
     * 简单来说就是存了 上下左右四个角的位置的东西。
     * 对画圆来说这个方法没必要重写。对写圆角矩形图片来说，就是为了获得 RectF 的实例
     */
    //左上右下
   /* @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        //绘制区域
        rectF = new RectF(left, top, right, bottom);
        Log.d("ImageDrawableTest","setBounds()");
    }*/

    //获取bipmap的高度
    @Override
    public int getIntrinsicHeight() {
        Log.d("ImageDrawableTest","getIntrinsicHeight()");
        return mHeight = bitmap.getHeight();
    }

    //获取bitmap的宽
    @Override
    public int getIntrinsicWidth() {
        Log.d("ImageDrawableTest","getIntrinsicWidth");
        return mWidth = bitmap.getWidth();
    }


    /**
     * 这是我们的核心方法,绘制我们想要的图片
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        //参数1:绘制的区域,参数2:X轴圆角半径,参数3:Y轴圆角半径,参数4:画笔
        //canvas.drawRoundRect(rectF, 50, 50, paint);
        //画圆(参数1.2:确定圆心坐标,参数3:半径,参数4:画笔)
        mX = mWidth / 2;
        mY = mHeight / 2;
        radius = Math.min(mX, mY);
        Log.d("ImageDrawableTest","draw()");
        canvas.drawCircle(mX, mY, radius, paint);
    }


    //设置透明度
    @Override
    public void setAlpha(int alpha) {
        Log.d("ImageDrawableTest","setAlpha()");
    }

    //设置滤镜渲染颜色
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        Log.d("ImageDrawableTest","setColorFilter()");
    }

    //获取透明图
    @Override
    public int getOpacity() {
        Log.d("ImageDrawableTest","getOpacity()");
        return 0;
    }

}
