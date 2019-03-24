package com.example.westroad.lanya.widge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by 北行_yangyimin on 2018/3/11.
 */

public class NewSeekBar extends View {
    Paint Circlepaint;
    Paint OutCirclepaint;
    private int Radius=100;
    private int Progress=30;
    private int maxProgress=100;
    public void setProgress(int Progress){
        this.Progress=Progress;
                invalidate();
    }
    public int getProgress(){
        return Progress;
    }
    public NewSeekBar(Context context) {
        super(context);
        initView();
    }

    public NewSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(100, widthMeasureSpec);
        int height = getMySize(100, heightMeasureSpec);

//        if (width < height) {
//            height = width;
//        } else {
//            width = height;
//        }

        setMeasuredDimension(width, height);
    }
    private void initView(){

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(0xFFAEEEEE);
        Circlepaint=p;
        Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p2.setAntiAlias(true);
        p2.setStyle(Paint.Style.FILL);
        p2.setColor(0xFFEE4000);
        OutCirclepaint=p2;
    }
    public void setMaxProgress(int maxProgress){
        this.maxProgress=maxProgress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int r = getMeasuredWidth() / 3;
        //圆心的横坐标为当前的View的左边起始位置+半径
        int centerX = getWidth()/2;
        //圆心的纵坐标为当前的View的顶部起始位置+半径
        int centerY = getHeight()/2;
        Radius=r-r/6;
        Paint pa = new Paint(Paint.ANTI_ALIAS_FLAG);
        pa.setAntiAlias(true);
        pa.setStyle(Paint.Style.FILL);
        pa.setColor(0xFFCFCFCF);
        canvas.drawCircle(centerX, centerY, r, pa);
        int p=(int)(Progress*360/maxProgress);
        //开始绘制
        canvas.drawArc(centerX-r,centerY-r,centerX+r,centerY+r,0,p,true,OutCirclepaint);
        canvas.drawCircle(centerX, centerY, Radius, Circlepaint);

    }
}
