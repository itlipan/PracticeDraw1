package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class Practice8DrawArcView extends View {

    public Practice8DrawArcView(Context context) {
        super(context);
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Paint mPaint = new Paint();

    Paint mAPaint = new Paint();

    private void init(){
        mPaint.setStyle(Paint.Style.FILL);

        mAPaint.setStyle(Paint.Style.STROKE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        练习内容：使用 canvas.drawArc() 方法画弧形和扇形
        canvas.drawArc(getWidth()/2 -200,getHeight()/2 -100,getWidth()/2 + 200,getHeight()/2 + 100, -135,120, true,mPaint);
        canvas.drawArc(getWidth()/2 -200,getHeight()/2 -100,getWidth()/2 + 200,getHeight()/2 + 100,15,150,false,mPaint);
        canvas.drawArc(getWidth()/2 -200,getHeight()/2 -100,getWidth()/2 + 200,getHeight()/2 + 100,-180,30,false,mAPaint);

    }
}
