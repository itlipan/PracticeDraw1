package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class Practice9DrawPathView extends View {

    public Practice9DrawPathView(Context context) {
        super(context);
    }

    public Practice9DrawPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Practice9DrawPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Path mPath = new Path();
    Path mTPath = new Path();
    Paint mPaint = new Paint();

    private void init() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        练习内容：使用 canvas.drawPath() 方法画心形
        // 定位椭圆的 左  上  右 下 四个顶点
        // 也可以通过定义矩形绘制椭圆，矩形同样是根据 左 上 右 下 四个点，通过四点确定矩形，左 右结合确定宽，上下确定高
        mPath.addArc(getWidth() / 2 - 100, getHeight() / 2 - 100, getWidth() / 2, getHeight() / 2, -180, 180);
        mPath.addArc(getWidth() / 2, getHeight() / 2 - 100, getWidth() / 2 + 100, getHeight() / 2, -180, 180);
        canvas.drawPath(mPath, mPaint);
        mTPath.moveTo(getWidth() / 2 - 100, getHeight() / 2 - 50);
        mTPath.lineTo(getWidth() / 2 + 100, getHeight() / 2 - 50);
        mTPath.lineTo(getWidth() / 2, getHeight() / 2 + 50);
        canvas.drawPath(mTPath, mPaint);
    }
}
