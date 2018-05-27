package com.souche.fengche.viewdemo.switcher;

import android.support.annotation.IntDef;
import android.view.MotionEvent;

/**
 * Created by Lee on 2018/2/9.
 *
 * 手势测量辅助类
 */

public class GestureHelper {
    public static final int PULL_UP = 0;
    public static final int PULL_DOWN = 1;
    public static final int PULL_LEFT = 11;
    public static final int PULL_RIGHT = 111;

    @IntDef(flag = true,value = {PULL_UP,PULL_DOWN,PULL_LEFT,PULL_RIGHT})
    public @interface TouchGesture {}

    private float startX = 0f;
    private float endX = 0f;
    private float startY = 0f;
    private float endY = 0f;
    private float xDistance = 0f;
    private float yDistance = 0f;


    public static GestureHelper getInstance() {
        return new GestureHelper();
    }

    private GestureHelper() {}

    /**
     * 当event.getAction() == MotionEvent.ACTION_DOWN 的时候调用
     * 设置初始X,Y坐标
     *
     * @param event
     */
    public void actionDown(MotionEvent event) {
        xDistance = yDistance = 0f;
        setStartX(event);
        setStartY(event);
    }

    /**
     * 当event.getAction() == MotionEvent.ACTION_MOVE 的时候调用
     * 设置移动的X,Y坐标
     *
     * @param event
     */
    public void actionMove(MotionEvent event) {
        setEndX(event);
        setEndY(event);
    }

    /**
     * 当event.getAction() == MotionEvent.ACTION_UP 的时候调用
     * 设置截止的X,Y坐标
     *
     * @param event
     */
    public void actionUp(MotionEvent event) {
        setEndX(event);
        setEndY(event);
    }

    public boolean getTouchGesture(@TouchGesture int gesture) {
        switch (gesture) {
            case PULL_UP:
                return isRealPullUp();
            case PULL_DOWN:
                return isRealPullDown();
            case PULL_LEFT:
                return isRealPullLeft();
            case PULL_RIGHT:
                return isRealPullRight();
            default:
                return false;
        }
    }

    /**
     * 获取X轴偏移量,取绝对值
     *
     * @param startX
     * @param endX
     * @return
     */
    private float gestureDistanceX(float startX, float endX) {
        setxDistance(Math.abs(endX - startX));
        return xDistance;
    }

    /**
     * 获取Y轴偏移量,取绝对值
     *
     * @param startY
     * @param endY
     * @return
     */
    private float gestureDistanceY(float startY, float endY) {
        setyDistance(Math.abs(endY - startY));
        return yDistance;
    }

    /**
     * endY坐标比startY小,相减负数表示手势上滑
     *
     * @param startY
     * @param endY
     * @return
     */
    private boolean isPullUp(float startY, float endY) {
        return (endY - startY) < 0;
    }

    private boolean isPullDown(float startY, float endY) {
        return (endY - startY) > 0;
    }

    private boolean isPullRight(float startX, float endX) {
        return (endX - startX) > 0;
    }

    private boolean isPullLeft(float startX, float endX) {
        return (endX - startX) < 0;
    }

    private boolean isRealPullUp() {
        if (gestureDistanceX(startX, endX) < gestureDistanceY(startY, endY)) {
            //Y轴偏移量大于X轴,表示用户真实目的是上下滑动
            return isPullUp(startY, endY);
        }
        return false;
    }

    private boolean isRealPullDown() {
        if (gestureDistanceX(startX, endX) < gestureDistanceY(startY, endY)) {
            //Y轴偏移量大于X轴,表示用户真实目的是上下滑动
            return isPullDown(startY, endY);
        }
        return false;
    }


    private boolean isRealPullLeft() {
        if (gestureDistanceX(startX, endX) > gestureDistanceY(startY, endY)) {
            //Y轴偏移量大于X轴,表示用户真实目的是上下滑动
            return isPullLeft(startX, endX);
        }
        return false;
    }

    private boolean isRealPullRight() {
        if (gestureDistanceX(startX, endX) > gestureDistanceY(startY, endY)) {
            //Y轴偏移量大于X轴,表示用户真实目的是上下滑动
            return isPullRight(startX, endX);
        }
        return false;
    }


    private void setStartX(MotionEvent event) {
        this.startX = event.getRawX();
    }

    private void setEndX(MotionEvent event) {
        this.endX = event.getRawX();
    }

    private void setStartY(MotionEvent event) {
        this.startY = event.getRawY();
    }

    private void setEndY(MotionEvent event) {
        this.endY = event.getRawY();
    }

    private void setxDistance(float xDistance) {
        this.xDistance = xDistance;
    }

    private void setyDistance(float yDistance) {
        this.yDistance = yDistance;
    }

    public float getStartX() {
        return startX;
    }

    public float getEndX() {
        return endX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndY() {
        return endY;
    }

    public float getxDistance() {
        return xDistance;
    }

    public float getyDistance() {
        return yDistance;
    }
}