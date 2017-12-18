package com.souche.fengche.appimeswitch.drag.swipe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.souche.fengche.appimeswitch.R;

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
