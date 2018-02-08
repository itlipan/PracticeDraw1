package com.souche.fengche.clickexpendviewdemo.tab;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Lee on 2018/1/29.
 */

public class PagerIndicator extends LinearLayout {
    public PagerIndicator(Context context) {
        super(context);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void initView(Context context) {
        setWillNotDraw(false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawRectLine(canvas);
        super.onDraw(canvas);
    }

    private void drawRectLine(Canvas canvas) {
        // 绘制 line
        canvas.save();

        canvas.restore();
    }

    /**
     * @param position       ViewPager 位置
     * @param offset         ViewPager 滚动偏移量 (0 ~ 1)
     * @param positionOffset
     */
    private void scroll(int position, float offset, int positionOffset) {
        // calculate line left and right
        int targetX = 0;
        scrollTo(targetX, 0);
    }


    public void setRelationViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
