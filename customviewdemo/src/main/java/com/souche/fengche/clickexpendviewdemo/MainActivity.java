package com.souche.fengche.clickexpendviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExpandTextView textView = findViewById(R.id.tv);
        textView.setText(R.string.info);
    }
}
