package com.souche.fengche.clickexpendviewdemo;

import android.app.Application;
import android.util.Log;

/*
 * Created by Lee on 2018/1/26.
 */

public class MainApplication extends Application {


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_COMPLETE){

        }
        if (level == TRIM_MEMORY_UI_HIDDEN){

        }
        Log.e(">>>>>>>>","level:" + level);
    }
}