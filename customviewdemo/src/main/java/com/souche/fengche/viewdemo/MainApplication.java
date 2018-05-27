package com.souche.fengche.viewdemo;

import android.app.Application;
import android.util.Log;

import com.elvishew.xlog.XLog;

/*
 * Created by Lee on 2018/1/26.
 */

public class MainApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        XLog.init();
    }

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
