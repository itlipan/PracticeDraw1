package com.souche.fengche.appdemofort.matrix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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

    ///
    private Matrix mMatrix;
    private ScaleGestureDetector mScaleGestureDetector;

    private boolean isHasAutoLayoutImg = false;

    private float mInitScale = sFloatTagOne;
    private float mMaxScale;
    private float mMidScale;

    /// Touch Move
    private int mTouchSlop;

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
                    mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());

                    checkImgBordAndCenterWhenScale();

                    resetViewMatrix();
                }
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
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
                mScaleGestureDetector.onTouchEvent(event);

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
                    case MotionEvent.ACTION_MOVE: {
                        float dx = x - mLastX;
                        float dy = y - mLastY;
                        if (!mIsCanDrage) {
                            mIsCanDrage = isMoveAction(dx, dy);
                        }
                        if (mIsCanDrage) {
                            RectF rectF = getMatrixRecF();
                            if (getDrawable() != null) {
                                if (rectF.width() < getWidth()) {
                                    dx = 0;
                                }
                                if (rectF.height() < getHeight()) {
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
        mMatrix.postTranslate(deX,deY);
        resetViewMatrix();
    }

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
            mMatrix.postTranslate(0, (h - dh) / 2);
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
