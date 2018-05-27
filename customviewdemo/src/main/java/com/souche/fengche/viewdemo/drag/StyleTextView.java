package com.souche.fengche.viewdemo.drag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by Lee on 2017/12/15.
 *
 * 这里主要为了学习,了解TextView 文字绘制时 top / ascent  / base / decent / bottom 几条线的坐标计算规则
 * https://github.com/siyehua/StyleTextView/blob/master/app/src/main/java/com/siyehua/styletextview/StyleTextView.java
 */
public class StyleTextView extends android.support.v7.widget.AppCompatTextView {
    public StyleTextView(Context context) {
        super(context);
    }

    public StyleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StyleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawText();  文字绘制时的 drawText 中的 startX 受居中等布局因素影响需要动态控制
        // 文字绘制的 坐标系  以  baseline 为 x 轴, 也就是 y 值为0 的点
        // baseLine = topTextY + -top(默认状态下,百分号的baseline) - 百分号的topTextY;
    }


    //TextView 的文字上部总的空白高度
    private float  getTopSpace(Paint paint){
        return  paint.getFontMetrics().ascent - paint.getFontMetrics().top  // textView 顶部到 ascent 线之间的空白距离
         +  secondTopSpace(paint); // 文字上方到 ascent 线之间的距离
    }

    // 文字的高度 + second 的空白 =  ascent 线与 descent 线之间的距离
    private float secondTopSpace(Paint paint) {
        return  paint.getFontMetrics().descent - paint.getFontMetrics().ascent - paint.getTextSize();
    }
}
