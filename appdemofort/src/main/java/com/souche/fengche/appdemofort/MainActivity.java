package com.souche.fengche.appdemofort;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.souche.fengche.appdemofort.matrix.MatrixDemoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewState();
    }

    private void initViewState() {
        final TextView tvInfo = findViewById(R.id.tv_title);
        final BankEditText etInfo= findViewById(R.id.et_content);
        etInfo.setChangeListener(new BankEditText.AfterTextChangeListener() {
            @Override
            public void afterTextChange(int infoLength) {
                tvInfo.setText(String.valueOf(infoLength));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title:{
                final BankEditText etInfo= findViewById(R.id.et_content);
                etInfo.setText("11111111");
            }break;
            case R.id.rtv_resize_text_info:{
                ResizeTextView tv = findViewById(R.id.rtv_resize_text_info);
                tv.setText(tv.getText().toString() + "哈");
            }break;
            case R.id.button_matrix:{
                Intent intent = new Intent(this, MatrixDemoActivity.class);
                startActivity(intent);
            }
            default:
                break;
        }
    }
}
