package com.souche.fengche.viewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ButterknifeActivity extends AppCompatActivity {

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

    @OnClick(value = sButtonID)
    void onClick(View view){

    }



}
