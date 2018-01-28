package com.souche.fengche.clickexpendviewdemo.tab.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.souche.fengche.clickexpendviewdemo.R;

/**
 * {@link  android.support.design.widget.TabItem}
 */
public class ReDefinedTabItem extends View {
    final CharSequence mText;
    final Drawable mIcon;
    final int mCustomLayout;

    public ReDefinedTabItem(Context context) {
        this(context, null);
    }

    public ReDefinedTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs,
            R.styleable.ReDefinedTabItem, 0, 0);
        mText = a.getText(R.styleable.ReDefinedTabItem_android_text);
        mIcon = a.getDrawable(R.styleable.ReDefinedTabItem_android_icon);
        mCustomLayout = a.getResourceId(R.styleable.ReDefinedTabItem_android_layout, 0);
        a.recycle();
    }
}