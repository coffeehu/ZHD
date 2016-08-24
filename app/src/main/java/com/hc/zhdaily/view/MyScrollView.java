package com.hc.zhdaily.view;

/**
 * Created by hc on 2016/8/17.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by hc on 2016/8/15.
 */
public class MyScrollView extends ScrollView {
    int yVelocity;
    VelocityTracker velocityTracker;
    private OnScrollChangeListener mOnScrollChangeListener;

    public MyScrollView(Context context){
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollChangeListener(OnScrollChangeListener mOnScrollChangeListener){
        this.mOnScrollChangeListener = mOnScrollChangeListener;
    }

    public interface OnScrollChangeListener{
        void onScrollChange(View view, int l, int t, int oldl, int oldt);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt){
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollChangeListener != null){
            mOnScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        super.onTouchEvent(ev);
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker = VelocityTracker.obtain();
                velocityTracker.addMovement(ev);
                velocityTracker.computeCurrentVelocity(1000);
                yVelocity = (int) velocityTracker.getYVelocity();
                Log.d("ScrollTest","yVelocity ="+yVelocity);
                break;
            case MotionEvent.ACTION_UP:
                if (velocityTracker != null) {
                    velocityTracker.clear();
                    velocityTracker.recycle();
                }
                break;
        }

        return true;
    }

    public  int getyVelocity(){
        return yVelocity;
    }


}
