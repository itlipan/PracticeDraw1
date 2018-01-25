package com.example.nestedscrolldemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.example.nestedscrolldemo.R;

/**
 * Created by Lee on 2017/8/12.
 *
 * StickyLayout 传统实现
 */

public class StickyLayout extends LinearLayout {
    public StickyLayout(Context context) {
        this(context, null);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private OverScroller mOverScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private ViewPager mViewPager;
    private View mViewTopContent;
    private int mTopContentHeight;

    private void initView(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

        mOverScroller = new OverScroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mViewPager = findViewById(R.id.vp_tab);
        mViewTopContent = findViewById(R.id.ll_content);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        // Exactly 设置 viewPager 高度 - ViewPager 不指定高度则高度为0
        params.height = getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 顶部内容高度
        mTopContentHeight = mViewTopContent.getMeasuredHeight();
    }

    float mLastY;
    boolean mIsInControl = false;
    boolean mIsDragging = false;
    boolean isTopHidden = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                getCurrentScrollView();

                RecyclerView recyclerView = (RecyclerView) mInnerScrollView;
                View view = recyclerView.getChildAt(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
                if (!mIsInControl && view != null && view.getTop() == 0 && isTopHidden && dy > 0) {
                    mIsInControl = true;
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    MotionEvent ev2 = MotionEvent.obtain(ev);
                    dispatchTouchEvent(ev);
                    ev2.setAction(MotionEvent.ACTION_DOWN);
                    return dispatchTouchEvent(ev2);
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        float currentY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 之前的滚动未结束,再次触发 touch 事件则停止之前 Action
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                mLastY = currentY;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = currentY - mLastY;
                if (!mIsDragging && Math.abs(dy) > mTouchSlop) {
                    mIsDragging = true;
                }
                if (mIsDragging) {
                    scrollBy(0, (int) -dy);
                    //topView 隐藏且上滑
                    if (getScrollY() == mTopContentHeight && dy < 0) {
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                        mIsInControl = false;
                    }
                }
                mLastY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                mIsDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                mIsDragging = false;
                recycleVelocityTracker();
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }

                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopContentHeight) {
            y = mTopContentHeight;
        }
        if (y != getScrollY()) ;
        {
            super.scrollTo(x, y);
        }
        isTopHidden = getScrollY() == mTopContentHeight;
    }

    private void fling(int flingY) {
        mOverScroller.fling(0, getScrollY(), 0, flingY, 0, 0, 0, mTopContentHeight);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(0, mOverScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                getCurrentScrollView();
                if (Math.abs(dy) > mTouchSlop) {
                    mIsDragging = true;

                    RecyclerView recyclerView = (RecyclerView) mInnerScrollView;
                    View view = recyclerView.getChildAt(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
                    if (!isTopHidden ||
                            (view != null && view.getTop() == 0 && isTopHidden && dy > 0)) {
                        initVelocityTrackerIfNotExists();
                        mVelocityTracker.addMovement(ev);
                        mLastY = y;
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsDragging = false;
                recycleVelocityTracker();
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    private ViewGroup mInnerScrollView;

    private void getCurrentScrollView() {
        int currentItem = mViewPager.getCurrentItem();
        PagerAdapter adapter = mViewPager.getAdapter();
        Fragment fragment = (Fragment) adapter.instantiateItem(mViewPager, currentItem);
        mInnerScrollView = fragment.getView().findViewById(R.id.rv_list);
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
