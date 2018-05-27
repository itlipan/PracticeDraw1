package com.souche.fengche.googlecomponents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.souche.fengche.googlecomponents.database.User;
import com.souche.fengche.googlecomponents.database.UserDataBase;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUserDataBase();
    }

    private void getUserDataBase() {
        UserDataBase.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hello:{
                List<User> userList = UserDataBase.getInstance(this).getUserDao().getAllUser();
                Log.e(TAG,String.valueOf(userList == null));
            }
            break;
            default:break;
        }
    }
}
