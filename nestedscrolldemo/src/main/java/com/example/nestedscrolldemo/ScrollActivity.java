package com.example.nestedscrolldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.nestedscrolldemo.scrollview.CustomScrollView;

public class ScrollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        final CustomScrollView scrollView = findViewById(R.id.ll_custom_scroll);
        findViewById(R.id.tv_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollAction(100,100);
            }
        });
    }
}
