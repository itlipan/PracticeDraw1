package com.souche.fengche.viewdemo.drag.swipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.souche.fengche.viewdemo.R;

public class SwipActivity extends AppCompatActivity {

    private SwipeBackFrameLayout mSwipeBackFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swip);
        mSwipeBackFrameLayout = findViewById(R.id.swipe_container);
        mSwipeBackFrameLayout.setSwipeCallBack(new SwipeBackFrameLayout.SwipeCallBack() {
            @Override
            public void onSwipeFinish() {
                finish();
            }
        });
    }
}
