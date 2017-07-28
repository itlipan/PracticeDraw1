package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Practice11PieChartView extends View {

    public Practice11PieChartView(Context context) {
        super(context);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    float[] mPercentArray = new float[]{0.35F, 0.25F, 0.15F, 0.206F, 0.033F, 0.011F};
    int[] mColorArray = new int[]{Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.GRAY, Color.MAGENTA};

    Paint mPaint = new Paint();
    Paint mTextPaint = new Paint();

    private void init() {
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStyle(Paint.Style.STROKE);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画饼图
        drawAngle(canvas);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawAngle(Canvas canvas) {
        final int gap = 2;
        final int raduis = 450 / 2;
        int currentEndAng = -180;// 绘制当前区域的起始angle
        for (int i = 0; i < mPercentArray.length; i++) {
            mPaint.setColor(mColorArray[i]);
            float currentAngle = mPercentArray[i] * (360 - gap * 4);// 当前扇形的 sweepAngle
            if (i == 0) {
                canvas.drawArc(getWidth() / 2 - 300, getHeight() / 2 - 300, getWidth() / 2 + 150, getHeight() / 2 + 150, currentEndAng, currentAngle, true, mPaint);
                drawLineAndText(canvas, currentEndAng, currentAngle, raduis, getWidth() / 2 - 300 + raduis, getHeight() / 2 - 300 + raduis);
            } else {
                canvas.drawArc(getWidth() / 2 - 275, getHeight() / 2 - 275, getWidth() / 2 + 175, getHeight() / 2 + 175, currentEndAng, currentAngle, true, mPaint);
                drawLineAndText(canvas, currentEndAng, currentAngle, raduis, getWidth() / 2 - 275 + raduis, getHeight() / 2 - 275 + raduis);
            }
            if (i == 0) {
                currentEndAng += currentAngle;
            } else {
                currentEndAng += currentAngle + gap;
            }
        }
    }

    /**
     * @param canvas
     * @param startAng 起始角度
     * @param sweepAng
     * @param radius   半径
     * @param centerX  圆心坐标X
     * @param centerY  圆心坐标Y
     */
    private void drawLineAndText(Canvas canvas, int startAng, float sweepAng, int radius, float centerX, float centerY) {
        final int rM = radius + 20;
        float pointAng = startAng + sweepAng / 2;
        Log.e("LINE", "start: " + startAng + " sweepAng:" + sweepAng + " radius:" + radius + "  !!! pointAng: " + pointAng);
        if (pointAng > -180 && pointAng <= -90) {
            final double y = radius * Math.sin(Math.toRadians(pointAng + 180));
            final double yM = rM * Math.sin(Math.toRadians(pointAng + 180));
            final double x = radius * Math.cos(Math.toRadians(pointAng + 180));
            final double xM = rM * Math.cos(Math.toRadians(pointAng + 180));
            canvas.drawLine((float) (centerX - x), (float) (centerY - y), (float) (centerX - xM), (float) (centerY - yM), mTextPaint);
            canvas.drawLine((float) (centerX - xM), (float) (centerY - yM), (float) (centerX - xM) - 100, (float) (centerY - yM), mTextPaint);
        } else if (pointAng > -90 && pointAng <= 0) {
            final double y = radius * Math.sin(Math.toRadians(pointAng * -1));
            final double yM = rM * Math.sin(Math.toRadians(pointAng * -1));
            final double x = radius * Math.cos(Math.toRadians(pointAng * -1));
            final double xM = rM * Math.cos(Math.toRadians(pointAng * -1));
            canvas.drawLine((float) (centerX + x), (float) (centerY - y), (float) (centerX + xM), (float) (centerY - yM), mTextPaint);
            canvas.drawLine((float) (centerX + xM), (float) (centerY - yM), (float) (centerX + xM) + 100, (float) (centerY - yM), mTextPaint);
        } else if (pointAng > 0 && pointAng <= 90) {
            final double y = radius * Math.sin(Math.toRadians(pointAng));
            final double yM = rM * Math.sin(Math.toRadians(pointAng));
            final double x = radius * Math.cos(Math.toRadians(pointAng));
            final double xM = rM * Math.cos(Math.toRadians(pointAng));
            canvas.drawLine((float) (centerX + x), (float) (centerY + y), (float) (centerX + xM), (float) (centerY + yM), mTextPaint);
            canvas.drawLine((float) (centerX + xM), (float) (centerY + yM), (float) (centerX + xM) + 100, (float) (centerY + yM), mTextPaint);
        } else {
            final double y = radius * Math.sin(Math.toRadians(180 - pointAng));
            final double yM = rM * Math.sin(Math.toRadians(180 - pointAng));
            final double x = radius * Math.cos(Math.toRadians(180 - pointAng));
            final double xM = rM * Math.cos(Math.toRadians(180 - pointAng));
            canvas.drawLine((float) (centerX - x), (float) (centerY + y), (float) (centerX - xM), (float) (centerY + yM), mTextPaint);
            canvas.drawLine((float) (centerX - xM), (float) (centerY + yM), (float) (centerX - xM) - 100, (float) (centerY + yM), mTextPaint);
        }
    }
}
