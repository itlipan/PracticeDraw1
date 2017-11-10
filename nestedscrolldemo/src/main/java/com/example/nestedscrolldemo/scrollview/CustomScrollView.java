package com.example.nestedscrolldemo.scrollview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Lee on 2017/10/12.
 */

public class CustomScrollView extends LinearLayout {
    public CustomScrollView(Context context) {
        super(context);
        init(context);
    }

    public CustomScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Scroller mScroller;
    private void init(Context context) {
        mScroller = new Scroller(context);

        setWillNotDraw(false);// computeScroll 在 View.draw 中执行
        ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {// 滚动未执行完,更新对应时间的当前滚动位置,继续执行滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * @param desX 目标 X 位置
     * @param desY 目标 Y 位置
     */
    public void scrollAction(int desX, int desY) {
        int scrollX = getScrollX();// 当前左边滚动距离  初始0
        int dX = desX - scrollX;
        mScroller.startScroll(scrollX, 0, dX, desY);
        invalidate();
    }

}
