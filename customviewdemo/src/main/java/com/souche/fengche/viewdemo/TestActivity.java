package com.souche.fengche.viewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.souche.fengche.viewdemo.gson.CustomerBaseInfo;

import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.button2: {
                Intent intent = new Intent(TestActivity.this, TabActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.button3: {
                Intent intent = new Intent(TestActivity.this, WidgetsActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.button4: {
                formatGson();
            }
            case R.id.button5: {

            }
            break;
        }
    }

    private void formatGson() {
        String info = URLDecoder.decode(getString(R.string.gson_info));
        Log.e("TAG",info);
        CustomerBaseInfo  baseInfo = new Gson().fromJson(info, CustomerBaseInfo.class);
        Log.e("TAG",baseInfo.getShopName());
    }
}
