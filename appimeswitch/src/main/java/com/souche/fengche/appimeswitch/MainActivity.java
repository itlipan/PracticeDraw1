package com.souche.fengche.appimeswitch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * 深刻理解 输入法模式
 * <p>
 * 利用输入法 Mod 的切换,以及固定显隐遮罩 Frame 高度 ==> 表情面板若低于输入法面板会有上下浮动现象
 * <p>
 * adjustResize 模式
 * <p>
 * adjustNothing 模式
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 在adjustResize 模式下键盘弹出,挤压屏幕,利用屏幕整体高度- 显示区域高度 获取键盘高度
     *
     * @return 键盘高度  键盘高度 - 240dp - 270 dp
     */
    private float getIMEDisplayHeight() {
        final Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);// 注意: 这个函数
        final int screenHeight = getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - rect.bottom;
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight -= getSoftBottomBarHeight();
        }
        return softInputHeight;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftBottomBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    // 方案1: 切换时输入法 mod 实现
    // 主要是 两种输入法 mod 对 ui 的影响的学习
    private void setSoftInputModForNothing() {
        updateActivitySoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void setSoftInputModForResize() {
        updateActivitySoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * 更新 Activity 输入法模式
     *
     * @param activity
     * @param softInputMod
     */
    private void updateActivitySoftInputMethod(Activity activity, int softInputMod) {
        if (!activity.isFinishing()) {
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            if (softInputMod != params.softInputMode) {
                params.softInputMode = softInputMod;
                activity.getWindow().setAttributes(params);
            }
        }
    }


    // 方案2 : 利用LinearLayout 中锁定内容高度, 显示输入法 ,最后还原 Content 让其自适应
    // 而后续利用 表情面板的高度与软键盘弹出时高度一致 时的显示隐藏
    // 实现 输入 bar 的位置固定,最终实现跳动的解决
    // bar 的高度为何会固定?
    // 在页面中元素改变时 content 高度不变, 输入法和表情面板的高度是等同互相可替换的,因而 bar 的所处位置也就被固定

    private void lockContentHeight(ViewGroup mContentView) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }

    private void unlockContentHeightDelayed(View view,final ViewGroup mContentView) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }
}
