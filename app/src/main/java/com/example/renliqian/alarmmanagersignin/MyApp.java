package com.example.renliqian.alarmmanagersignin;

import android.app.Application;

import com.example.renliqian.alarmmanagersignin.db.MyDatabase;

/**
 * @Description: java类作用描述
 * @Author: lisa
 * @CreateDate: 2019/8/2 16:17
 */
public class MyApp extends Application {
    private static MyApp appContext;
    @Override
    public void onCreate() {
        super.onCreate();

        //数据库
        MyDatabase.init(this);

    }

    public static MyApp getAppContext() {
        return appContext;
    }
}
