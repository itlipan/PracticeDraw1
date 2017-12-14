package com.souche.fengche.androidhotfixdemo;

import android.app.Application;

import com.souche.fengche.androidhotfixdemo.hack.Hack;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * Created by Lee on 2017/12/12.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        findAndInjectDex();
    }

    private void findAndInjectDex() {
        String dexDownLoadPath = "";
        File dexFile = new File(dexDownLoadPath);
        if (dexFile.exists()) {
            injectDex(dexFile);
        }
    }

    private void injectDex(File dexFile) {
        try {
            Hack.HackedClass loader = Hack.into("dalvik.system.BaseDexClassLoader");
            Hack.HackedField pathListField = loader.field("pathList");
            Object pathListInstance = pathListField.get(getClassLoader());//PathList

            Hack.HackedClass pathListClass = Hack.into(pathListField.getClass());
            Hack.HackedField dexElementsField = pathListClass.field("dexElements");
            Object dexElements = dexElementsField.get(pathListInstance);//Elements

            //构建补丁 dex
            String dexopt = getDir("dexopt",0).getAbsolutePath();
            // 目标类所在 jar 路径 , 从目标 jar 所解压出的dex文件所存放路径, 目标类所使用C++库存放路径 , parentClassLoader
            DexClassLoader dexClassLoader = new DexClassLoader(dexFile.getPath(),dexopt,dexopt,getClassLoader());




        } catch (Hack.HackDeclaration.HackAssertionException e) {
            e.printStackTrace();
        }
    }
}
