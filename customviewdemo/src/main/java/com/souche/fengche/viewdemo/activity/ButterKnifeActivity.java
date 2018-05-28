package com.souche.fengche.viewdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.souche.fengche.viewdemo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ButterKnifeActivity extends AppCompatActivity {

    private static int sActLayoutID;
    private static int sButtonID;

    static {
        sActLayoutID = R.layout.activity_butterknife;
        sButtonID = R.id.button6;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(sActLayoutID);
        ButterKnife.bind(this);
    }

    /**
     * Value 必须是常量表达式
     * @param view
     */
    @OnClick(value = R.id.button6)
    void onClick(View view){

    }



}
