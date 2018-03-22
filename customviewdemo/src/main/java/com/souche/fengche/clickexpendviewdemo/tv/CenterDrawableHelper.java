package com.souche.fengche.clickexpendviewdemo.tv;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Lee on 2018/3/22.
 */
final class CenterDrawableHelper {
    static final int DRAWABLE_LEFT = 0;
    static final int DRAWABLE_TOP = 1;
    static final int DRAWABLE_RIGHT = 2;
    static final int DRAWABLE_BOTTOM = 3;

    private static void onCenterDraw(TextView view, Canvas canvas, Drawable drawable, int gravity, int width, int height) {
        int drawablePadding = view.getCompoundDrawablePadding();
        int ratio = 1;
        float total;
        switch (gravity) {
            case Gravity.RIGHT:
                ratio = -1;
            case Gravity.LEFT:
                total = view.getPaint().measureText(view.getText().toString()) + drawable.getIntrinsicHeight() + drawablePadding + view.getPaddingLeft() + view.getPaddingRight();
                canvas.translate(ratio * (view.getWidth() - total) / 2, 0);
                break;
            case Gravity.BOTTOM:
                ratio = -1;
            case Gravity.TOP:
                Paint.FontMetrics fontMetrics0 = view.getPaint().getFontMetrics();
                total = fontMetrics0.descent - fontMetrics0.ascent + drawable.getIntrinsicHeight() + drawablePadding + view.getPaddingTop() + view.getPaddingBottom();
                canvas.translate(0, ratio * (view.getHeight() - total) / 2);
                break;
        }
    }

    private static void preDraw(TextView view, Drawable[] drawables, Canvas canvas, int[] sizeArray) {
        if (drawables == null) return;

        if (drawables[DRAWABLE_LEFT] != null) {
            view.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            onCenterDraw(view, canvas, drawables[DRAWABLE_LEFT], Gravity.LEFT, sizeArray[DRAWABLE_LEFT], sizeArray[DRAWABLE_LEFT + 1]);
        } else if (drawables[DRAWABLE_TOP] != null) {
            view.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            onCenterDraw(view, canvas, drawables[DRAWABLE_TOP], Gravity.TOP, sizeArray[DRAWABLE_TOP], sizeArray[DRAWABLE_TOP + 1]);
        } else if (drawables[DRAWABLE_RIGHT] != null) {
            view.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            onCenterDraw(view, canvas, drawables[DRAWABLE_RIGHT], Gravity.RIGHT, sizeArray[DRAWABLE_RIGHT], sizeArray[DRAWABLE_RIGHT + 1]);
        } else if (drawables[DRAWABLE_BOTTOM] != null) {
            view.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            onCenterDraw(view, canvas, drawables[DRAWABLE_BOTTOM], Gravity.BOTTOM, sizeArray[DRAWABLE_BOTTOM], sizeArray[DRAWABLE_BOTTOM + 1]);
        }
    }
}
