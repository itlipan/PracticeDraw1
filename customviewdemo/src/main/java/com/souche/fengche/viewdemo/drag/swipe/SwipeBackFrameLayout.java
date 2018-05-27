package com.souche.fengche.viewdemo.drag.swipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.souche.fengche.viewdemo.drag.DisplayUtil;


/**
 * Created by Lee on 2017/12/17.
 */

public class SwipeBackFrameLayout extends FrameLayout {

    private SwipeCallBack mSwipeCallBack;

    private ViewDragHelper mDragHelper;

    private float mCriticalWidth;

    private View mContentView;

    public SwipeBackFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeBackFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setSwipeCallBack(SwipeCallBack callBack) {
        mSwipeCallBack = callBack;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ViewGroup) {
                mContentView = getChildAt(i);
                break;
            }
        }
        if (mContentView == null) {
            Log.e("SwipeError", "onFinishInflate", new IllegalAccessException("must have one ContainerView"));
        }
    }

    private void init(Context context) {
        mCriticalWidth = DisplayUtil.dip2px(context, 40);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            private float mLastDx;

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                mDragHelper.captureChildView(mContentView, pointerId);// 边缘触摸操作 Action 时捕获 View
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                // > 0 时  shouldInterceptTouchEvent Action Move 时才可能触发 tryCaptureViewForDrag,拖拽操作才可能被执行
                return 1;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                mLastDx = dx;// 水平移动距离,注意 如果拖拽时不放手,先向左拖拽后向右拖拽, dx 先为正数后为负数(< 0)
                // 设置水平滑动的距离范围
                return (int) Math.min(mCriticalWidth, Math.max(left, 0));
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                // left = 左侧滑动的距离 通常用于根据滑动距离做 View 变化控制
                float alpha = (left * 1.0f) / mCriticalWidth;
                // 设置其 alpha 值变化 以及等由于侧滑显示 View 的动画变化

            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                // 释放时判定条件,是否拖动到临界值
                super.onViewReleased(releasedChild, xvel, yvel);
                Log.i("onViewReleased", "dx: " + mLastDx);
                if (mLastDx > 0) {
                    if (mCriticalWidth == releasedChild.getLeft()) {
                        if (mSwipeCallBack != null) {
                            mSwipeCallBack.onSwipeFinish();
                            return;
                        }
                    }
                }
                // back 复位
                mDragHelper.settleCapturedViewAt(0, releasedChild.getTop());// computedScroll,只能在 onViewReleased 触发
                postInvalidate();

            }

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
                if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE
                    && mSwipeCallBack != null && mLastDx > 0) {
                    if (mContentView.getLeft() == mCriticalWidth) { // 拖拽停止,并且达到了拖拽触发 Callback 的条件
                        mSwipeCallBack.onSwipeFinish();
                    }
                }
            }
        });
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);// 从左向右滑动
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            postInvalidate();
        }
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return mDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    interface SwipeCallBack {
        void onSwipeFinish();
    }
}
