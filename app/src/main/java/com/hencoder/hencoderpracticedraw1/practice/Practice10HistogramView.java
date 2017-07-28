package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice10HistogramView extends View {

    public Practice10HistogramView(Context context) {
        super(context);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint mWPaint = new Paint();
    Paint mGPaint = new Paint();
    Paint mTextPaint = new Paint();

    private void init() {
        mWPaint.setAntiAlias(true);
        mWPaint.setColor(Color.WHITE);
        mWPaint.setStyle(Paint.Style.STROKE);

        mGPaint.setAntiAlias(true);
        mGPaint.setColor(Color.GREEN);
        mGPaint.setStyle(Paint.Style.FILL);

        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画直方图
        canvas.drawLine(200, 100, 200, getHeight() - 200, mWPaint);
        canvas.drawLine(200, getHeight() - 200, getWidth() - 100, getHeight() - 200, mWPaint);
        canvas.drawText("直方图", getWidth() / 2, getHeight() - 50, mTextPaint);
        //
        drawRecMap(canvas);

    }

    private Float[] mHeightArray = new Float[]{1F, 30F, 170F, 50F, 220F, 160F, 280F};

    private String[] mInfoArray = new String[]{"Froy", "GB", "ICS", "JB", "Kitkat", "L", "M"};

    private void drawRecMap(Canvas canvas) {
        final int gap = 25;
        final int gWidth = (getWidth() - 300 - 8 * gap) / 7; // 每个直方的宽度
        canvas.drawRect(200 + 20, getHeight() - 200 - 1, 200 + 20 + gWidth, getHeight() - 200, mWPaint);
        for (int i = 0; i < mHeightArray.length; i++) {
            final int left = 200 + 20 * (i + 1) + gWidth * i;
            canvas.drawRect(left, getHeight() - 200 - mHeightArray[i], left + gWidth, getHeight() - 200, mGPaint);
            canvas.drawText(mInfoArray[i], left + gWidth / 2, getHeight() - 165, mTextPaint);
        }
    }
}
