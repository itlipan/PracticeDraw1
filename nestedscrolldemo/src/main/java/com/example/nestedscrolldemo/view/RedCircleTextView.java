package com.example.nestedscrolldemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.nestedscrolldemo.R;


/**
 * Created by pan_li on 2015/9/24.
 */
public class RedCircleTextView extends TextView {
    private Paint mPaint;
    private int mCircleX;
    private int mCircleY;
    private float mRadius;
    private boolean isDrawRedCircle = false;//默认不显示红点
    private int dotPaddingRight = 0;
    private int notifyNum = 0;
    private float density;

    public RedCircleTextView(Context context) {
        super(context);
    }

    public RedCircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RedCircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    void init(Context context,AttributeSet attrs,int defStyleAttr){
        density = getContext().getResources().getDisplayMetrics().density;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DotViewStyleable, defStyleAttr, 0);
        dotPaddingRight = a.getDimensionPixelSize(R.styleable.DotViewStyleable_paddingRight,0);
        if(dotPaddingRight < 0){
            dotPaddingRight = 0;
        }
        a.recycle();
    }



    //  根据设置文本后所自适应的宽高，延展半径，宽延展2倍半径，高延展1倍半径
//  宽高应该设置为wrap_content
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCircleX = this.getMeasuredWidth() - dotPaddingRight * (density>1.5?1:2);
        mCircleY = this.getMeasuredHeight();
        mRadius = mCircleY/10.0f;
        this.setHeight(mCircleY+(int)mRadius);
        this.setWidth(mCircleX + 2 * (int) mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDrawRedCircle) {

            if(mPaint ==null) mPaint = new Paint();
            mPaint.setColor(0xFFF44336);
            //抗锯齿标志
            mPaint.setAntiAlias(true);
            float textWidth = 0;
            float textHeight = 0;
            if(notifyNum > 0){
                mPaint.setStrokeWidth(1);
                mPaint.setTextSize(12 * density);
                String num = notifyNum < 100 ? String.valueOf(notifyNum) : "99+";
                textWidth = mPaint.measureText(num);
                Paint.FontMetrics fm = mPaint.getFontMetrics();
                textHeight = fm.bottom - fm.top;
                int padding = (int) (1 * density);
                mRadius = (Math.max(textHeight,textWidth)) /2 + padding;
                float textX = 0;
                float textY = 0;
                if(notifyNum > 99){
                    int radius = (int) (9 * density);
                    RectF rectF = new RectF(mCircleX - textWidth - padding*2 + 6*density,padding,mCircleX +  padding*2 + 6*density,textHeight + padding*3);
                    canvas.drawRoundRect(rectF,radius,radius,mPaint);
                    textX = mCircleX - textWidth/2 + 6*density;
                    textY = textHeight/2 - fm.ascent/2 - fm.descent/2 + padding*2;
                } else {
                    canvas.drawCircle(mCircleX - mRadius,  mRadius + 2*padding, mRadius, mPaint);
                    textX = mCircleX - mRadius;
                    textY = mRadius - fm.ascent/2 - fm.descent/2 + 2*padding;
                }
                mPaint.setColor(Color.WHITE);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(num, textX , textY , mPaint);
            }else{
                mRadius = mCircleY/10.0f;
                float cy = mRadius + density * 2;
                canvas.drawCircle(mCircleX - mRadius * 1.2f, 0 + mRadius * 1.2f, mRadius, mPaint);
            }
        }
    }

    /**
     *设置红点绘制标志位，绘制右上角带有红点的TextView，红点半径为自适应高的1/10
     */
    public  void drawRedCircle(int notifyNum){
        this.isDrawRedCircle = true;
        this.notifyNum = notifyNum;
        this.invalidate();
    }

    /**
     * 取消绘制红点标志位，重新绘制无红点TextView
     */
    public  void cancleRedCircle(){
        this.isDrawRedCircle = false;
        this.notifyNum = 0;
        this.invalidate();
    }

}

