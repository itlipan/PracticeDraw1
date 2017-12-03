package com.souche.fengche.appdemofort.matrix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Lee on 2017/12/3.
 */

@SuppressLint("AppCompatCustomView")
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener {
    private final float sFloatTagOne = 1.0f;// 用于 int 变换 float
    private final float sFloatTagCompare = 0.01f;// float 比较

    public ZoomImageView(Context context) {
        super(context);
        initView(context);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /// init
    private Matrix mMatrix;
    private ScaleGestureDetector mScaleGestureDetector;

    private boolean isHasAutoLayoutImg = false;

    private float mInitScale = sFloatTagOne;
    private float mMaxScale;
    private float mMidScale;

    /// Touch Move
    private int mTouchSlop;

    /// DoubleCheck
    private GestureDetector mGestureDetector;
    private boolean mIsInAutoScale = false;//当前是否处于双击缩放过程中

    private void initView(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();//move 阈值

        mMatrix = new Matrix();
        // 多点触控
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();

                if (getDrawable() == null) return true;

                float currentScale = getCurrentScaleValue();
                if ((currentScale < mMaxScale && scaleFactor > sFloatTagOne) // 允许范围内手势放大
                    ||
                    (currentScale > mInitScale && scaleFactor < sFloatTagOne)// 允许范围内手势缩小
                    ) {
                    if (currentScale * scaleFactor > mMaxScale) {
                        scaleFactor = mMaxScale / currentScale;
                    }
                    if (currentScale * scaleFactor < mInitScale) {
                        scaleFactor = mInitScale / currentScale;
                    }
                    mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());//以手势聚焦点为中心缩放

                    checkImgBordAndCenterWhenScale();

                    resetViewMatrix();
                }
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;//是否检测此次手势事件
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }
        });
        setOnTouchListener(new OnTouchListener() {
            float mLastY;
            float mLastX;
            boolean mIsCanDrage;// 是否能够被移动
            int mLastPointerCount;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleGestureDetector.onTouchEvent(event);//手势接口监听 Touch 事件
                mGestureDetector.onTouchEvent(event);

                float x = 0, y = 0;
                int pointCount = event.getPointerCount();//拖动时的多点触控优化,如用户先点击一个手指,接着另外的手指按下
                //多点取各点的中心值
                for (int i = 0; i < pointCount; i++) {
                    x += event.getX(i);
                    y += event.getY(i);
                }
                x /= pointCount;
                y /= pointCount;// 取多点的中心值   一个点在左上一个点在右下,相当于一个手指在屏幕中心点击

                if (mLastPointerCount != pointCount) {
                    mIsCanDrage = false;
                    mLastX = x;
                    mLastY = y;
                }
                mLastPointerCount = pointCount;

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        // More Fix
                        // 如果没有左右移动到图片的边界 移动事件由 View 本身处理请求不被父 View 拦截
                        //getParent().requestDisallowInterceptTouchEvent(true);
                        if (Math.abs(getMatrixRecF().width() - getWidth()) > sFloatTagCompare) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {
                        float dx = x - mLastX;
                        float dy = y - mLastY;
                        if (!mIsCanDrage) {
                            mIsCanDrage = isMoveAction(dx, dy);
                        }
                        if (mIsCanDrage) {
                            RectF rectF = getMatrixRecF();
                            if (getDrawable() != null) {
                                if (rectF.width() <= getWidth()) {// 相等时也需要校验,差值消除
                                    dx = 0;
                                }
                                if (rectF.height() <= getHeight()) {
                                    dy = 0;
                                }
                                mMatrix.postTranslate(dx, dy);
                                checkBorderWhenTranslate();
                                resetViewMatrix();
                            }
                        }
                        mLastX = x;
                        mLastY = y;
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        mLastPointerCount = 0;
                    }
                    break;
                }
                return true;
            }

            private boolean isMoveAction(float dx, float dy) {
                //根据 x  y 的偏移量 做勾股定理 取斜方向便宜
                return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
            }
        });

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mIsInAutoScale) return true;

                float x = e.getX();
                float y = e.getY();
                if (getCurrentScaleValue() < mMidScale) {
                    post(new AutoRunnable(mMidScale, x, y));
                } else {
                    post(new AutoRunnable(mInitScale, x, y));
                }
                return true;
            }
        });
    }

    private final class AutoRunnable implements Runnable {
        private final float SMALL = 0.92f;// 放大缩小的步进
        private final float BIG = 1.08f;
        private float mTempScale;
        private float mTargetScale;
        private float mPointX;
        private float mPointY;

        private AutoRunnable(float targetScale, float pointX, float pointY) {
            mTargetScale = targetScale;
            mPointX = pointX;
            mPointY = pointY;
            if (getCurrentScaleValue() < mTargetScale) {// 当前操作是要放大还是缩小
                mTempScale = BIG;
            } else if (getCurrentScaleValue() > mTargetScale) {
                mTempScale = SMALL;
            }
        }

        @Override
        public void run() {
            mIsInAutoScale = true;

            mMatrix.postScale(mTempScale, mTempScale, mPointX, mPointY);
            checkImgBordAndCenterWhenScale();
            resetViewMatrix();

            if (mTempScale > sFloatTagOne && getCurrentScaleValue() < mTargetScale) {//需要继续放大
                postDelayed(this, 20);
            } else if (mTempScale < sFloatTagOne && getCurrentScaleValue() > mTargetScale) {//需要继续放大
                postDelayed(this, 20);
            } else {
                mMatrix.postScale(mTargetScale / getCurrentScaleValue(), mTargetScale / getCurrentScaleValue(), mPointX, mPointY);
                checkImgBordAndCenterWhenScale();
                resetViewMatrix();

                mIsInAutoScale = false;
            }
        }
    }

    private void checkBorderWhenTranslate() {
        final RectF rectF = getMatrixRecF();
        float deX = 0, deY = 0;
        int w = getWidth();
        int h = getHeight();

        if (rectF.height() > h) {
            if (rectF.top > 0) {
                deY = -rectF.top;
            }
            if (rectF.bottom < h) {
                deY = h - rectF.bottom;
            }
        }
        if (rectF.width() > w) {
            if (rectF.left > 0) {
                deX = -rectF.left;
            }
            if (rectF.right < w) {
                deX = w - rectF.right;
            }
        }
        mMatrix.postTranslate(deX, deY);
        resetViewMatrix();
    }

    /**
     * 获取当前图片被 Matrix 应用变换后的图片矩形边界
     * @return
     */
    private RectF getMatrixRecF() {
        RectF rectF = new RectF();

        Drawable d = getDrawable();
        if (d == null) return rectF;


        rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        mMatrix.mapRect(rectF);//Apply this matrix to the rectangle  获取图片应用 Matrix 之后的 img border
        return rectF;
    }

    /**
     * 以手势操作点为中心对图片进行缩放可能出现的 图片边缘白边问题
     */
    private void checkImgBordAndCenterWhenScale() {
        RectF rectF = getMatrixRecF();
        float deltaX = 0;
        float deltaY = 0;

        int w = getWidth();
        int h = getHeight();

        // 针对放大缩小后的 matrix 边界与 View 的边界进行比较
        // 对图片进行边界的细节的弥补
        // 当图片的宽度或者高度大于 View 的宽度高度时产生的边缘空隙才需要平移弥补
        if (rectF.width() >= w) {
            if (rectF.left > 0) {
                deltaX = -rectF.left;
            }
            if (rectF.right < w) {
                deltaX = w - rectF.right;
            }
        }
        if (rectF.height() >= h) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < h) {
                deltaY = h - rectF.bottom;
            }
        }
        // 如果 img 宽度高度小于控件宽度高度-> img 居中
        if (rectF.width() < w) {
            deltaX = w / 2 - rectF.right + rectF.width() / 2;
        }
        if (rectF.height() < h) {
            deltaY = h / 2 - rectF.bottom + rectF.height() / 2;
        }
        mMatrix.postTranslate(deltaX, deltaY);
        setImageMatrix(mMatrix);
    }

    private float getCurrentScaleValue() {
        final float[] values = new float[9];
        mMatrix.getValues(values);
        return values[Matrix.MSCALE_X];// 一维数组 9个数字表示 3维矩阵
    }

    /**
     * 1 . 图片的自缩放,防止图片过大导致的内存浪费 OOM
     */
    @Override
    public void onGlobalLayout() {
        //图片大小自适应
        //视图数变化时会触发 CallBack ,防止多次缩放操作
        if (!isHasAutoLayoutImg) {
            isHasAutoLayoutImg = true;

            final int w = getWidth();
            final int h = getHeight();

            Drawable drawable = getDrawable();
            if (drawable == null) return;

            final int dw = drawable.getIntrinsicWidth();
            final int dh = drawable.getIntrinsicHeight();

            float scale = sFloatTagOne;
            if (dw > w && dh < h) {// 图片宽/高单边大于可显示宽高,计算图片缩小比例
                scale = w * sFloatTagOne / dw;
            }
            if (dw < w && dh > h) {
                scale = h * sFloatTagOne / dh;
            }

            if (dw > w && dh > h) {//缩小
                scale = Math.min(w * sFloatTagOne / dw, h * sFloatTagOne / dh);
            }
            if (dw < w && dh < h) {//放大
                scale = Math.min(w * sFloatTagOne / dw, h * sFloatTagOne / dh);
            }
            mInitScale = scale;
            mMaxScale = mInitScale * 5;
            mMidScale = mInitScale * 2;

            //移动图片至 View 中心  平移
            mMatrix.postScale(mInitScale, mInitScale);
            mMatrix.postTranslate((w - getMatrixRecF().width()) / 2, (h - getMatrixRecF().height()) / 2);
            resetViewMatrix();
        }
    }

    private void resetViewMatrix() {
        setScaleType(ScaleType.MATRIX);
        setImageMatrix(mMatrix);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

}
