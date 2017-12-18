package com.souche.fengche.appimeswitch.drag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.souche.fengche.appimeswitch.R;

public class DragActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "toast", Toast.LENGTH_SHORT).show();
    }
}
