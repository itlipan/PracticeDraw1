package com.example.nestedscrolldemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.example.nestedscrolldemo.R;

/**
 * Created by Lee on 2017/8/13.
 */

public class NestedStickLayout extends LinearLayout implements NestedScrollingParent {
    // 滚动 Action 的封装执行 Helper,用于辅助 View 的滑动
    private OverScroller mScroller;


    public NestedStickLayout(Context context) {
        this(context, null);
    }

    public NestedStickLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedStickLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        mScroller = new OverScroller(context);
    }

    private View mTopContentView;
    private View mViewPager;
    private ValueAnimator mOffsetAnimator;
    private Interpolator mInterpolator;

    private void animateScroll(float velocityY, final int duration, boolean consumed) {
        final int currentOffset = getScrollY();
        final int topHeight = mTopContentView.getHeight();
        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mInterpolator = new AccelerateInterpolator();
            mOffsetAnimator.setInterpolator(mInterpolator);
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (animation.getAnimatedValue() instanceof Integer) {
                        //动画Action 更新 Scroll
                        scrollTo(0, (Integer) animation.getAnimatedValue());
                    }
                }
            });
        } else {
            mOffsetAnimator.cancel();
        }
        mOffsetAnimator.setDuration(Math.min(duration, 600));

        if (velocityY >= 0) {
            mOffsetAnimator.setIntValues(currentOffset, topHeight);
            mOffsetAnimator.start();
        } else {
            //如果子View没有消耗down事件,显示 TopView,滚动自身到 y = 0;
            if (!consumed) {
                mOffsetAnimator.setIntValues(currentOffset, 0);
                mOffsetAnimator.setDuration(300);
                mOffsetAnimator.start();
            }
        }
    }

    private int computeDuration(float velocityY) {
        final int distance;
        if (velocityY > 0) {
            distance = Math.abs(mTopContentView.getHeight() - getScrollY());
        } else {
            distance = Math.abs(mTopContentView.getHeight() - (mTopContentView.getHeight() - getScrollY()));
        }
        final int duration;
        if (velocityY > 0) {
            duration = 3 * Math.round(1000 * (distance / velocityY));
        } else {
            final float distanceRatio = distance / getHeight();
            duration = (int) ((distanceRatio + 1) / 150);
        }
        return duration;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mViewPager = findViewById(R.id.vp_tab);
        mTopContentView = findViewById(R.id.ll_content);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //
        mViewPager.getLayoutParams().height = getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), mTopContentView.getMeasuredHeight() + mViewPager.getMeasuredHeight());
    }

    private float mTopContentHeight;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopContentHeight = mTopContentView.getMeasuredHeight();
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopContentHeight) {
            y = (int) mTopContentHeight;
        }
        // 传入的需要滚动到的位置不等于当前的位置,继续滚动
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    /**
     * Draw 触发,用以计算如何滚动
     */
    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()) {// 检测是否完成滚动
            // 更新 View 当前的 mScrollX  以及 mScrollY 值
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    //true if this ViewParent accepts the nested scroll operation
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //super.onNestedPreScroll(target, dx, dy, consumed);
        boolean hiddenTop = dy > 0 && getScrollY() < mTopContentHeight; //当前 TopView 显示
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);

            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //return super.onNestedPreFling(target, velocityX, velocityY);
        return false;
    }


    //velocityY Vertical velocity in pixels per second
    //true if the child consumed the fling, false otherwise
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        // 根据 recyclerView 首个显示的 View 的 Position 判断fling 事件是否被消耗
        final int TOP_CHILD_FLING_THRESHOLD = 3;
        if (target instanceof RecyclerView && velocityY < 0) {
            final RecyclerView recyclerView = (RecyclerView) target;
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            final int firstAdapterPos = layoutManager.findFirstVisibleItemPosition();
            consumed = firstAdapterPos > TOP_CHILD_FLING_THRESHOLD;
        }
        if (!consumed) {
            animateScroll(velocityY, computeDuration(0), consumed);
        } else {
            animateScroll(velocityY, computeDuration(velocityY), consumed);
        }


        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public int getNestedScrollAxes() {
        //return SCROLL_AXIS_NONE;
        return 0;
    }

}
