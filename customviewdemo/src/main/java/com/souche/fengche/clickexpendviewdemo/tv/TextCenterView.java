package com.souche.fengche.clickexpendviewdemo.tv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Lee on 2018/3/23.
 */

@SuppressLint("AppCompatCustomView")
public class TextCenterView extends TextView {

    public TextCenterView(Context context) {
        super(context);
    }

    public TextCenterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextCenterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Drawable Caches
     */
    private Drawable[] drawablesResCaches;

    @Override
    protected void onDraw(Canvas canvas) {
        CenterDrawableHelper.preDraw(this, drawablesResCaches, canvas);
        super.onDraw(canvas);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        drawablesResCaches = getCompoundDrawables();
    }

    @Override
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        drawablesResCaches = getCompoundDrawables();
    }
}
