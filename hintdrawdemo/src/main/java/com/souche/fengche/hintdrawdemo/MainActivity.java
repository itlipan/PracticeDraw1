package com.souche.fengche.hintdrawdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private View mParentDecorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mParentDecorView = getWindow().getDecorView();
        getLocationOfView(findViewById(R.id.button));
    }

    private void getLocationOfView(final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                final int[] location = new int[2];
                view.getLocationOnScreen(location);
                Log.e(TAG, "==============> 0:" + location[0] + " 1:" + location[1]);
                int w = view.getWidth();
                int h = view.getHeight();
                Log.e(TAG, "w=>>>" + w + "  h=>>" + h);
                initHintView();
            }
        });
    }

    /**
     * 加载提示 View
     */
    private void initHintView() {


    }
}
