package com.souche.fengche.clickexpendviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button: {
                Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.button2: {
                Intent intent = new Intent(TestActivity.this,TabActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.button3:{
                Intent intent = new Intent(TestActivity.this,WidgetsActivity.class);
                startActivity(intent);
            }break;
        }
    }
}
