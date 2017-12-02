package com.souche.fengche.appdemofort;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
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
    private Rect  reMesureRect;
    private Paint getResizePaint() {
        if (reTextPaint == null) {
            reTextPaint = new Paint(getPaint());
        }
        return reTextPaint;
    }
    private Rect getReRect(){
        if (reMesureRect == null){
            reMesureRect = new Rect();
        }
        return reMesureRect;
    }
}
