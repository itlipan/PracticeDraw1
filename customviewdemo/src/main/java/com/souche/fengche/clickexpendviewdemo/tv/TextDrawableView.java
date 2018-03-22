package com.souche.fengche.clickexpendviewdemo.tv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.souche.fengche.clickexpendviewdemo.R;

/**
 * Created by Lee on 2018/3/21.
 * <p>
 * 用于设置点击箭头的 TextView => 可以控制箭头大小
 */

public class TextDrawableView extends android.support.v7.widget.AppCompatTextView {
    private static final int DRAWABLE_LEFT = 0;
    private static final int DRAWABLE_TOP = 1;
    private static final int DRAWABLE_RIGHT = 2;
    private static final int DRAWABLE_BOTTOM = 3;


    private int mLeftWidth;
    private int mLeftHeight;
    private int mTopWidth;
    private int mTopHeight;
    private int mRightWidth;
    private int mRightHeight;
    private int mBottomWidth;
    private int mBottomHeight;
    /**
     * Drawable Caches
     */
    private Drawable[] drawablesResCaches;
    private Rect mRectCache = new Rect(0, 0, 0, 0);

    public TextDrawableView(Context context) {
        this(context, null);
    }

    public TextDrawableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextDrawableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
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
        drawablesResCaches = getCompoundDrawables();
    }

    @Override
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        drawablesResCaches = getCompoundDrawables();
    }

    private void resetDrawableSize(Drawable[] drawablesRes) {
        if (drawablesRes == null) return;

        for (int i = 0; i < drawablesRes.length; i++) {
            switch (i) {
                case DRAWABLE_LEFT: //left
                    setDrawableBounds(i, drawablesRes[i], mLeftWidth, mLeftHeight);
                    break;
                case DRAWABLE_TOP:// top
                    setDrawableBounds(i, drawablesRes[i], mTopWidth, mTopHeight);
                    break;
                case DRAWABLE_RIGHT: //right
                    setDrawableBounds(i, drawablesRes[i], mRightWidth, mRightHeight);
                    break;
                case DRAWABLE_BOTTOM: //bottom
                    setDrawableBounds(i, drawablesRes[i], mBottomWidth, mBottomHeight);
                    break;
                default:
                    break;
            }
        }
    }


    private void setDrawableBounds(final int drawableIndex, final Drawable drawableRes, final int width, final int height) {
        if (drawableRes == null) return;
        final double scaleSize = drawableRes.getIntrinsicHeight() / drawableRes.getIntrinsicWidth();
        final Rect rect = resetRect(height, width);

        // 设置单个值,另一个值按照原有高宽比自适应
        if (rect.right != 0 || rect.bottom != 0) {
            if (rect.right == 0) {
                rect.right = (int) (height * scaleSize);
            }
            if (rect.bottom == 0) {
                rect.bottom = (int) (width * scaleSize);
            }
        }

        switch (drawableIndex) {
            case DRAWABLE_RIGHT: {
                // 高的 bounds边界在原有显示范围内进行调节
                final int heightDiff = drawableRes.getIntrinsicHeight() / 2 - height / 2;
                if (heightDiff < 0) {// 图标大小小于设置大小,将图片往View中心移动
                    rect.left = -(int) ((getMeasuredWidth() - getPaint().measureText(getText().toString())) / 2);
                    rect.right = rect.left + width;
                }
                rect.top = heightDiff;
                rect.bottom = rect.top + height;
            }
            break;
            case DRAWABLE_TOP: {
                final int heightDiff = drawableRes.getIntrinsicHeight() / 2 - height / 2;
                rect.top = Math.abs(heightDiff);
                rect.bottom = rect.top + height;

                final int diff = (drawableRes.getIntrinsicWidth() - width) / 2;
                rect.left = diff;
                rect.right = rect.left + width;
            }
            break;
            default:
                break;
        }

        drawableRes.setBounds(rect);
    }

    private Rect resetRect(int height, int width) {
        mRectCache.left = 0;
        mRectCache.top = 0;
        mRectCache.right = width;
        mRectCache.bottom = height;
        return mRectCache;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        resetDrawableSize(drawablesResCaches);
    }

}
