package com.souche.fengche.appdemofort;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Lee on 2017/12/2.
 */

@SuppressLint("AppCompatCustomView")
public class ResizeTextView extends TextView {


    public ResizeTextView(Context context) {
        super(context);
    }

    public ResizeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        resizeTextViewInfo(getText().toString(), getWidth());
    }


    /**
     * 实现的局限在于仅仅实现了单行的缩放 - 多行需要考虑 高度,在多行显示完毕之后,再考虑缩小
     * @param text
     * @param width textView 宽度
     */
    private void resizeTextViewInfo(String text, int width) {
        if (!TextUtils.isEmpty(text) || width > 0) {
            int availableWidth = width - getPaddingLeft() - getPaddingRight();
            final Rect rect = getReRect();
            getResizePaint().getTextBounds(text, 0, text.length(), rect);
            // while true
            float textWidth = rect.width();
            float currentTextSize = getTextSize();// px
            while (textWidth > availableWidth) {
                currentTextSize = currentTextSize - 2;// 使用二分法优化
                getResizePaint().setTextSize(currentTextSize);// px
                textWidth = getResizePaint().measureText(text);
            }
            setTextSize(TypedValue.COMPLEX_UNIT_PX,currentTextSize);// 指定 px 单位
        }
    }

    private Paint reTextPaint;
    private Rect reMeasureRect;
    private Paint getResizePaint() {
        if (reTextPaint == null) {
            reTextPaint = new Paint(getPaint());
        }
        return reTextPaint;
    }
    private Rect getReRect(){
        if (reMeasureRect == null){
            reMeasureRect = new Rect();
        }
        return reMeasureRect;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // 思路2  利用 onMeasure 以及 onTextChange 函数实现


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);//从 spec 中提取测量大小值
        int height = getMeasuredHeight();
        Log.e("Resize", "height: 1. " + height + " 2. " + MeasureSpec.getSize(heightMeasureSpec));
        MeasureSpec.makeMeasureSpec(height,MeasureSpec.getMode(heightMeasureSpec));// 构造 mask 测量高度值
        refitText(getText().toString(),width);
        this.setMeasuredDimension(width,height);// 何时用真实测量值 何时用 mask 测量值(格式化)要明确
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        refitText(text.toString(),getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw){
            refitText(getText().toString(),w);
        }
    }

    private void refitText(String info, int width) {

    }
}
