package com.souche.fengche.viewdemo.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

/**
 * Created by Lee on 2018/1/22.
 *
 * 侧滑展开隐藏按钮的自定义 View
 */

public class LeftSwipeView extends RelativeLayout {
    public LeftSwipeView(Context context) {
        this(context, null);
    }

    public LeftSwipeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftSwipeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private int mTouchSlop;
    private int mMaxVelocity;
    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;

    private float mLastX;
    private int mMaxRightWidth;

    private void initView(Context context, AttributeSet attrs) {
        mMaxVelocity = ViewConfiguration.get(context).getMaximumFlingVelocity();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new OverScroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // onMeasure中的参数,实际是上级 ParentView 根据 ParentView 自身的状态计算后进行的指定(建议)
        //ViewGroup 在此需要根据自身的设定,指定自身的测量宽高值
        //同时 根据onMeasure 中的参数,指定 ChildView 的 measure 测量模式

        /**
         *1. 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        /**2. 指定 ChildView 的测量模式,对 child 进行测量,测量后可以获取到 ChildView 的宽高*/
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        /**3. 根据测量后的所有 ChildView 的宽高设置自身的宽高*/
        //setMeasuredDimension();
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // changed 参数表示 该 View 是否被更新了新的位置或尺寸
        //根据自身位置进行 childView 位置指定
        int left = 0, right = 0;
        for (int i = 0, size = getChildCount(); i < size; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;

            if (i == 0) {
                // 指定 ChildView 在 ParentView的相对位置
                // 注意此处 left 是 该自定义 ViewGroup 相对于其 ParentView 的 Layout 位置
                // 此处会将 ViewGroup 中的 margin 带入到 child 中
                // 错误使用:
                //child.layout(left, getPaddingTop(), left + child.getMeasuredWidth(), getPaddingTop() + child.getMeasuredHeight());

                // 正确:
                child.layout(0, getPaddingTop(), child.getMeasuredWidth(), getPaddingTop() + child.getMeasuredHeight());
            } else {// 除开第一个View 作为 content 占据全部位置,其他 View 在侧边隐藏
                child.layout(left, getPaddingTop(), left + child.getMeasuredWidth(), getPaddingTop() + child.getMeasuredHeight());
                mMaxRightWidth += child.getMeasuredWidth();
            }
            left += child.getMeasuredWidth();
        }
    }



    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            //overScrollBy 进一步触发 onOverScrolled
            overScrollBy(mScroller.getCurrX() - getScrollX(), 0, getScrollX(), 0, mMaxRightWidth, 0, 0, 0, false);
            //scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        scrollTo(scrollX, scrollY);
    }

    @Override
    public void scrollTo(int x, int y) {
        //限定最大滚动距离
        if (x < 0) x = 0;

        if (x > mMaxRightWidth) x = mMaxRightWidth;

        super.scrollTo(x, y);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        makeVelocityTracker();
        mVelocityTracker.addMovement(ev);

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                mLastX = ev.getX();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                int dx = (int) (ev.getX() - mLastX);
                mScroller.startScroll(getScrollX(), getScrollY(), -dx, 0);
                postInvalidate();
            }
            break;
            case MotionEvent.ACTION_CANCEL: {
                releaseVelocityTracker();
            }
            break;
            case MotionEvent.ACTION_UP: {
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);//设置units的值为1000，计算一秒时间内运动了多少个像素
                fling(getScrollX(), -mVelocityTracker.getXVelocity());//速度值计算结果为负
                releaseVelocityTracker();
            }
            break;
        }
        return super.onTouchEvent(ev);
    }

    public void fling(float startX, float velocityX) {
        mScroller.fling((int) startX, 0, (int) velocityX, 0, 0, mMaxRightWidth, 0, 0);
    }

    private void makeVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
