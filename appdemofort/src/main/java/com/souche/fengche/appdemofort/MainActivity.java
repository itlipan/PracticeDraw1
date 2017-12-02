package com.souche.fengche.appdemofort;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tvInfo = findViewById(R.id.tv_title);
        final BankEditText etInfo= findViewById(R.id.et_content);
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInfo.setText("11111111");
            }
        });

        etInfo.setChangeListener(new BankEditText.AfterTextChangeListener() {
            @Override
            public void afterTextChange(int infoLength) {
                tvInfo.setText(String.valueOf(infoLength));
            }
        });
        findViewById(R.id.rtv_resize_text_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResizeTextView tv = findViewById(R.id.rtv_resize_text_info);
                tv.setText(tv.getText().toString() + "哈");
            }
        });
    }
}
