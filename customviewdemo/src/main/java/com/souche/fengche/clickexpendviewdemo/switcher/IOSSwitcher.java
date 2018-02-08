package com.souche.fengche.clickexpendviewdemo.switcher;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Lee on 2018/2/9.
 * <p>
 * 1. 图形绘制
 * 2. 动画
 * 3. 手势
 */

public class IOSSwitcher extends View {

    interface OnSwitcherChangeListener {
        void onSwitchStateChange(boolean finalViewState);
    }

    public IOSSwitcher(Context context) {
        this(context, null);
    }

    public IOSSwitcher(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IOSSwitcher(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private OnSwitcherChangeListener mSwitcherChangeListener;

    public void setSwitcherChangeListener(OnSwitcherChangeListener switcherChangeListener) {
        mSwitcherChangeListener = switcherChangeListener;
    }

    private final int BORDER_WIDTH = 1;//外边框
    private int mBasePanColor = Color.parseColor("#E5E5E5");//底盘颜色,布局描边颜色
    private int mOpenSlotColor = Color.parseColor("#4ebb7f");//开启时手柄滑动槽的颜色
    private int mOffSlotColor = Color.parseColor("#D9D9D9");//关闭时手柄滑动槽的颜色

    //绘制参数
    private int mCurrentSlotColor; // 当前的椭圆滑道颜色
    private float mMovePanRadius;//底板的圆形半径
    private float mSpotRadius;//手柄半径
    private float mSpotY;
    private float mSpotStartXPoint;
    private float mSpotStopXPoint;// 停止位置 X
    private Paint mPaint;

    //
    private boolean mCurrentToggleState = false;
    private boolean isTouchEventMoveConsume = false;//当前 View 手势事件是滑动还是点击?
    private boolean isInMoving = false;

    public void setSpotStartXPoint(float spotStartXPoint) {
        this.mSpotStartXPoint = spotStartXPoint;
    }

    public float getSpotStartXPoint() {
        return mSpotStartXPoint;
    }

    private Resources mResources;
    private GestureHelper mGestureHelper;
    private RectF mDrawCacheRect;

    private void initView(Context context, AttributeSet attrs) {
        mResources = context.getResources();
        mGestureHelper = GestureHelper.getInstance();
        mDrawCacheRect = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (height / width < 0.5f || height/ width > 0.8f) { // 防止宽高比过长形成窄长View
            height = (int) (width * 0.58f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec));
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, mResources.getDisplayMetrics());
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, mResources.getDisplayMetrics());
        }
        setMeasuredDimension(width, height);
        initViewDrawParamsAfterMeasure();
    }

    private void initViewDrawParamsAfterMeasure() {
        mMovePanRadius = Math.min(getMeasuredWidth(), getMeasuredHeight()) * 0.5f;
        mSpotRadius = mMovePanRadius - BORDER_WIDTH;
        mSpotY = 0;
        mSpotStartXPoint = 0;
        mSpotStopXPoint = getMeasuredWidth() - mMovePanRadius * 2; //动画移动终点
        mCurrentSlotColor = mOffSlotColor;// 默认初始状态关闭

        if (mCurrentToggleState) {
            mSpotStartXPoint = mSpotStopXPoint;
            mCurrentSlotColor = mOpenSlotColor;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mDrawCacheRect.set(0,0,getMeasuredWidth(),getMeasuredHeight());
        mPaint.setColor(mBasePanColor);
        canvas.drawRoundRect(mDrawCacheRect,mMovePanRadius,mMovePanRadius,mPaint);

        mDrawCacheRect.set(BORDER_WIDTH,BORDER_WIDTH,getMeasuredWidth() - BORDER_WIDTH,getMeasuredHeight() - BORDER_WIDTH);
        mPaint.setColor(mCurrentSlotColor);
        canvas.drawRoundRect(mDrawCacheRect,mSpotRadius,mSpotRadius,mPaint);

        //
        mDrawCacheRect.set(mSpotStartXPoint,mSpotY,mMovePanRadius * 2 + mSpotStartXPoint,mMovePanRadius*2 + mSpotY);
        mPaint.setColor(mBasePanColor);
        canvas.drawRoundRect(mDrawCacheRect,mMovePanRadius,mMovePanRadius,mPaint);

        mDrawCacheRect.set(mSpotStartXPoint + BORDER_WIDTH,mSpotY + BORDER_WIDTH,mSpotRadius * 2 + mSpotStartXPoint + BORDER_WIDTH,mSpotRadius*2 + mSpotY + BORDER_WIDTH);
        mPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(mDrawCacheRect,mSpotRadius,mSpotRadius,mPaint);
    }

    private void toMoveOn() {
        // 针对当前 View spotStartXPoint 设置属性动画 ,spotStartXPoint 属性是整个 View 变动的基准
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "spotStartXPoint", 0, mSpotStopXPoint);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                calculateColor(fraction, mOffSlotColor, mOpenSlotColor);
                invalidate();// 刷新颜色以及位置
            }
        });
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isInMoving = false;
            }
        });
    }

    private void toMoveOff() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "spotStartXPoint", mSpotStopXPoint, 0);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                calculateColor(fraction, mOpenSlotColor, mOffSlotColor);
                invalidate();
            }
        });
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isInMoving = false;
            }
        });
    }

    private void changeState (boolean targetState) {
        if (mCurrentToggleState != targetState) {
            mCurrentToggleState = targetState;
            isInMoving = true;
            if (targetState) {
                toMoveOn();
            } else {
                toMoveOff();
            }
            if (mSwitcherChangeListener != null) {
                mSwitcherChangeListener.onSwitchStateChange(targetState);
            }
        }
    }

    public void calculateColor(float fraction, int startColor, int endColor) {
        final int fb = Color.blue(startColor);
        final int fr = Color.red(startColor);
        final int fg = Color.green(startColor);

        final int tb = Color.blue(endColor);
        final int tr = Color.red(endColor);
        final int tg = Color.green(endColor);

        //RGB三通道线性渐变
        int sr = (int) (fr + fraction * (tr - fr));
        int sg = (int) (fg + fraction * (tg - fg));
        int sb = (int) (fb + fraction * (tb - fb));
        //范围限定
        sb = clamp(sb, 0, 255);
        sr = clamp(sr, 0, 255);
        sg = clamp(sg, 0, 255);

        mCurrentSlotColor = Color.rgb(sr, sg, sb);
    }

    private int clamp(int value, int low, int high) {
        return Math.min(Math.max(value, low), high);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mGestureHelper.actionDown(event);
                isTouchEventMoveConsume = false;
                isInMoving = false;
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                mGestureHelper.actionMove(event);
                if (!isInMoving) {
                    if (mGestureHelper.getTouchGesture(GestureHelper.PULL_LEFT)) {
                        isTouchEventMoveConsume = true;
                        changeState(false);
                    } else if (mGestureHelper.getTouchGesture(GestureHelper.PULL_RIGHT)) {
                        isTouchEventMoveConsume = true;
                        changeState(true);
                    }
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                mGestureHelper.actionUp(event);
                if (!isInMoving && !isTouchEventMoveConsume) { // 当前不在动画过程中,没有手势滑动事件,属于单纯点击
                    changeState(!mCurrentToggleState);
                }
            }
            break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
