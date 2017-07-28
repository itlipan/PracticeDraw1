package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice2DrawCircleView extends View {

    public Practice2DrawCircleView(Context context) {
        super(context);
        init();
    }

    public Practice2DrawCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Practice2DrawCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Paint mPaintFill = new Paint();
    Paint mPaintStroke = new Paint();
    Paint mPaintBlue = new Paint();
    Paint mPaintStroke20 = new Paint();

    private void init() {
        mPaintFill.setAntiAlias(true);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(Color.BLACK);
        //
        mPaintStroke.setColor(Color.BLACK);
        mPaintStroke.setStyle(Paint.Style.STROKE);
        //
        mPaintBlue.setStyle(Paint.Style.FILL);
        mPaintBlue.setColor(Color.BLUE);
        //
        mPaintStroke20.setColor(Color.BLACK);
        mPaintStroke20.setStyle(Paint.Style.STROKE);
        mPaintStroke20.setStrokeWidth(20);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        练习内容：使用 canvas.drawCircle() 方法画圆
//        一共四个圆：1.实心圆 2.空心圆 3.蓝色实心圆 4.线宽为 20 的空心圆
        canvas.drawCircle(getWidth() / 4, getHeight() / 4, 200, mPaintFill);
        canvas.drawCircle(getWidth() * 3 / 4, getHeight() / 4, 200, mPaintStroke);
        canvas.drawCircle(getWidth() / 4, getHeight() * 3 / 4, 200, mPaintBlue);
        canvas.drawCircle(getWidth() * 3 / 4, getHeight() * 3 / 4, 200, mPaintStroke20);
    }
}
