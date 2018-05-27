package com.souche.fengche.viewdemo.drag;

import android.content.Context;

/**
 * Created by Lee on 2017/12/15.
 */

public class DisplayUtil {

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dpResid
     *  xml中定义的dp属性
     *  getDimension()                返回float型px值     精确
    getDimensionPixelOffset()     返回int型px值       直接把小数删除
    getDimensionPixelSize()       返回int型px值       进行四舍五入

     * @return
     */
    public static int xmlDip2px(Context context, int dpResid) {
        return context.getResources().getDimensionPixelOffset(dpResid);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spResid
     *  xml中定义的sp属性
     *  getDimension()                返回float型px值     精确
    getDimensionPixelOffset()     返回int型px值       直接把小数删除
    getDimensionPixelSize()       返回int型px值       进行四舍五入

     * @return
     */
    public static int xmlSp2px(Context context, int spResid) {
        return context.getResources().getDimensionPixelOffset(spResid);
    }
}
