package com.souche.fengche.hintdrawdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Lee on 2017/9/23.
 */

public class AllScreenScanHintView extends AppCompatTextView {

    public AllScreenScanHintView(Context context) {
        super(context);
    }

    public AllScreenScanHintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AllScreenScanHintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
