package com.souche.fengche.viewdemo.drag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.souche.fengche.viewdemo.R;


public class DragActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * RecyclerView 中的拖拽辅助类
     * ItemTouchHelper
     */


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
