package com.souche.fengche.appimeswitch.drag;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Lee on 2017/12/16.
 */

public class DragContainer extends LinearLayout {

    /**
     * 1. 拖拽 -> 手势跟随
     * 2. 边界触摸检测
     * 3. Drag 释放回调
     * 4. 移动到某个位置
     */
    ViewDragHelper mDragHelper;

    public DragContainer(Context context) {
        this(context, null);
    }

    public DragContainer(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                // 是否捕获相关View
                // 可用于指定哪些 View 可被拖拽
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 控制水平边界
                final int maxLeftPosition = (getWidth() - child.getMeasuredWidth());
                if (left > maxLeftPosition) {// 最大右边的值,不允许 View 拖动时有部分被拖动到屏幕外
                    left = maxLeftPosition;
                } else if (left < 0) { // 左边边界控制
                    left = 0;
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int maxHeightPosition = getHeight() - child.getMeasuredHeight();
                if (top > maxHeightPosition){
                    top = maxHeightPosition;
                }else if(top < 0){
                    top = 0;
                }
                return top;
            }

            /**
             * 捕捉的 View 释放回调,如重新布局位置等
             * @param releasedChild
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild instanceof TextView){
                    mDragHelper.settleCapturedViewAt(0,0);// 设置释放 View 的位置 ,拖拽释放时可以重新设置其初始位置,方法内部利用 scroll 实现
                    postInvalidate();
                }
            }

            // 子 View 水平方向可被拖拽范围
            // 子 View 可点击
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getWidth() - child.getMeasuredWidth();
            }

            // 子 View 垂直方向可被拖拽范围
            @Override
            public int getViewVerticalDragRange(View child) {
                return super.getViewVerticalDragRange(child);
            }
        });
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)){
            postInvalidate();
        }
        super.computeScroll();
    }

}
