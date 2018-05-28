package com.souche.fengche.viewdemo;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 获取 Application 实例
 */
public class AppInstance {
    private static final String TAG = AppInstance.class.getSimpleName();

    @Nullable
    public static final Application INSTANCE;

    @Nullable
    public static final ApplicationInfo INFO_INSTANCE;

    @Nullable
    public static final Resources RES_INSTANCE;

    static {
        Application app = null;
        ApplicationInfo applicationInfo = null;
        //
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Log.e(TAG, "Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.e(TAG, "Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            INSTANCE = app;
        }
        //
        try {
            applicationInfo = app != null ? app.getPackageManager().getApplicationInfo(app.getPackageName(),
                PackageManager.GET_META_DATA) : null;
        } catch (Exception e) {
            applicationInfo = null;
            Log.e(TAG, "Failed to get applicationInfo" + e.getMessage());
        }finally {
            INFO_INSTANCE = applicationInfo;
        }
        //
        RES_INSTANCE = app != null ? app.getResources() : null;
    }
}