package com.souche.fengche.clickexpendviewdemo.tv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.souche.fengche.clickexpendviewdemo.R;

/**
 * Created by Lee on 2018/3/21.
 *
 * 用于设置点击箭头的 TextView => 可以控制箭头大小
 */

public class TextDrawableView extends android.support.v7.widget.AppCompatTextView {
    private int mLeftWidth;
    private int mLeftHeight;
    private int mTopWidth;
    private int mTopHeight;
    private int mRightWidth;
    private int mRightHeight;
    private int mBottomWidth;
    private int mBottomHeight;

    public TextDrawableView(Context context) {
        this(context,null);
    }

    public TextDrawableView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextDrawableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray tr = context.obtainStyledAttributes(attrs, R.styleable.TextDrawableView);
        mLeftWidth = tr.getDimensionPixelOffset(R.styleable.TextDrawableView_drawableLeftWidth, 0);
        mLeftHeight = tr.getDimensionPixelOffset(R.styleable.TextDrawableView_drawableLeftHeight, 0);
        mTopWidth = tr.getDimensionPixelOffset(R.styleable.TextDrawableView_drawableTopWidth, 0);
        mTopHeight = tr.getDimensionPixelOffset(R.styleable.TextDrawableView_drawableTopHeight, 0);
        mRightWidth = tr.getDimensionPixelOffset(R.styleable.TextDrawableView_drawableRightWidth, 0);
        mRightHeight = tr.getDimensionPixelOffset(R.styleable.TextDrawableView_drawableRightHeight, 0);
        mBottomWidth = tr.getDimensionPixelOffset(R.styleable.TextDrawableView_drawableBottomWidth, 0);
        mBottomHeight = tr.getDimensionPixelOffset(R.styleable.TextDrawableView_drawableBottomHeight, 0);
        tr.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        resetDrawableSize();
    }

    private void resetDrawableSize() {
        final Drawable[] drawablesRes = getCompoundDrawables();
        for (int i = 0; i < drawablesRes.length; i++) {
            switch (i) {
                case 0: //left
                    setDrawableBounds(i,drawablesRes[i],mLeftWidth,mLeftHeight);
                    break;
                case 1:// top
                    setDrawableBounds(i,drawablesRes[i],mTopWidth,mTopHeight);
                    break;
                case 2: //right
                    setDrawableBounds(i,drawablesRes[i],mRightWidth,mRightHeight);
                    break;
                case 3: //bottom
                    setDrawableBounds(i,drawablesRes[i],mBottomWidth,mBottomHeight);
                    break;
                default:
                    break;
            }
        }
    }


    private void setDrawableBounds(int drawableIndex,Drawable drawableRes, int width, int height) {
        if (drawableRes == null) return;

        final double scaleSize = drawableRes.getIntrinsicHeight()/drawableRes.getIntrinsicWidth();
        drawableRes.setBounds(0,0,width,height);
        final Rect rect = drawableRes.getBounds();
        if ( rect.right != 0 || rect.bottom != 0) {// 设置单个值,另一个值按照原有高宽比自适应
            if (rect.right == 0) {
                rect.right = (int) (height * scaleSize);
            }
            if (rect.bottom == 0) {
                rect.bottom = (int) (width * scaleSize);
            }
        }

        if (drawableIndex == 0) {
            final int diff = drawableRes.getIntrinsicWidth() - width;
            if (diff > 0) {
                rect.left += diff;
                rect.right += diff;
            }
        }
        if (drawableIndex == 1) {
            final int diff = drawableRes.getIntrinsicHeight() - height;
            if (diff > 0) {
                rect.top += diff;
                rect.bottom += diff;
            }
        }
        drawableRes.setBounds(rect);
    }

}
