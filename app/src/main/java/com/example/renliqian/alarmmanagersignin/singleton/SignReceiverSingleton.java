package com.example.renliqian.alarmmanagersignin.singleton;

import android.content.IntentFilter;
import android.util.Log;

import com.example.renliqian.alarmmanagersignin.MyApp;
import com.example.renliqian.alarmmanagersignin.service.SIListManagerService;
import com.example.renliqian.alarmmanagersignin.service.SigninService;


/**
 * Created by totowen on 2018/5/30.
 * 类级内部类方式（单例）
 */
public class SignReceiverSingleton {

    private volatile static SIListManagerService.AlarmReceiver mSIListReceiverSingleton = null;
    private volatile static String mSIListReceiverActionSingleton = null;
    private volatile static SigninService.NetworkReceiver mSINetReceiverSingleton = null;
    private volatile static String mSINetReceiverActionSingleton = null;
    private volatile static SigninService.LocateReceiver mSILocateReceiverSingleton = null;
    private volatile static String mSILocateReceiverActionSingleton = null;

    /**
     * 获取SIListManagerService.AlarmReceiver单例并注册
     */
    public static void getSIListReceiverInstance() {
        if (mSIListReceiverSingleton == null) {
            synchronized (SIListManagerService.AlarmReceiver.class) {
                if (mSIListReceiverSingleton == null) {
                    //注册动态广播(必须使用随机动态广播才能取消已设置的定时器)
                    mSIListReceiverSingleton = new SIListManagerService.AlarmReceiver();
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(getSIListReceiverActionInstance());
                    MyApp.getAppContext().registerReceiver(mSIListReceiverSingleton, filter);
                    Log.e("SignReceiverSingleton", "注册了了了SIListManagerService.AlarmReceiver");
                }
            }
        }
    }

    /**
     * 获取SIListManagerService.AlarmReceiver的action单例
     *
     * @return
     */
    public static String getSIListReceiverActionInstance() {
        if (mSIListReceiverActionSingleton == null) {
            synchronized (String.class) {
                if (mSIListReceiverActionSingleton == null) {
                    //注册动态广播(必须使用随机动态广播才能取消已设置的定时器)
                    mSIListReceiverActionSingleton = String.valueOf(Math.random());
                    Log.e("SignReceiverSingleton", "mSIListReceiverActionSingleton==" + mSIListReceiverActionSingleton);
                }
            }
        }
        return mSIListReceiverActionSingleton;
    }

    /**
     * 取消SIListManagerService.AlarmReceiver单例广播的注册
     */
    public static void resetSIListReceiverNull() {
        if (mSIListReceiverSingleton != null) {
            MyApp.getAppContext().unregisterReceiver(mSIListReceiverSingleton);
            mSIListReceiverActionSingleton = null;
            mSIListReceiverSingleton = null;
            Log.e("SignReceiverSingleton", "取消注册SIListManagerService.AlarmReceiver");
        }
    }

    /**
     * 获取SigninService.NetworkReceiver广播单例并注册
     */
    public static void getSINetReceiverInstance() {
        if (mSINetReceiverSingleton == null) {
            synchronized (SigninService.NetworkReceiver.class) {
                if (mSINetReceiverSingleton == null) {
                    //注册动态广播(必须使用随机动态广播才能取消已设置的定时器)
                    mSINetReceiverSingleton = new SigninService.NetworkReceiver();
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(getSINetReceiverActionInstance());
                    MyApp.getAppContext().registerReceiver(mSINetReceiverSingleton, filter);
                    Log.e("SignReceiverSingleton", "注册了了了SigninService.NetworkReceiver");
                }
            }
        }
    }

    /**
     * 获取SigninService.NetworkReceiver的action单例
     *
     * @return
     */
    public static String getSINetReceiverActionInstance() {
        if (mSINetReceiverActionSingleton == null) {
            synchronized (String.class) {
                if (mSINetReceiverActionSingleton == null) {
                    //注册动态广播(必须使用随机动态广播才能取消已设置的定时器)
                    mSINetReceiverActionSingleton = String.valueOf(Math.random());
                    Log.e("SignReceiverSingleton", "mSINetReceiverActionSingleton==" + mSINetReceiverActionSingleton);
                }
            }
        }
        return mSINetReceiverActionSingleton;
    }

    /**
     * 取消SigninService.NetworkReceiver单例广播的注册
     */
    public static void resetSINetReceiverNull() {
        if (mSINetReceiverSingleton != null) {
            MyApp.getAppContext().unregisterReceiver(mSINetReceiverSingleton);
            mSINetReceiverActionSingleton = null;
            mSINetReceiverSingleton = null;
            Log.e("SignReceiverSingleton", "取消注册SigninService.NetworkReceiver");
        }
    }


    /**
     * 获取SigninService.LocateReceiver广播单例并注册
     */
    public static void getSILocateReceiverInstance() {
        if (mSILocateReceiverSingleton == null) {
            synchronized (SigninService.LocateReceiver.class) {
                if (mSILocateReceiverSingleton == null) {
                    //注册动态广播(必须使用随机动态广播才能取消已设置的定时器)
                    mSILocateReceiverSingleton = new SigninService.LocateReceiver();
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(getSILocateReceiverActionInstance());
                    MyApp.getAppContext().registerReceiver(mSILocateReceiverSingleton, filter);
                    Log.e("SignReceiverSingleton", "注册了了了SigninService.LocateReceiver");
                }
            }
        }
    }

    /**
     * 获取SigninService.LocateReceiver的action单例
     *
     * @return
     */
    public static String getSILocateReceiverActionInstance() {
        if (mSILocateReceiverActionSingleton == null) {
            synchronized (String.class) {
                if (mSILocateReceiverActionSingleton == null) {
                    //注册动态广播(必须使用随机动态广播才能取消已设置的定时器)
                    mSILocateReceiverActionSingleton = String.valueOf(Math.random());
                    Log.e("SignReceiverSingleton", "mSILocateReceiverActionSingleton==" + mSILocateReceiverActionSingleton);
                }
            }
        }
        return mSILocateReceiverActionSingleton;
    }

    /**
     * 取消SigninService.LocateReceiver单例广播的注册
     */
    public static void resetSILocateReceiverNull() {
        if (mSILocateReceiverSingleton != null) {
            MyApp.getAppContext().unregisterReceiver(mSILocateReceiverSingleton);
            mSILocateReceiverActionSingleton = null;
            mSILocateReceiverSingleton = null;
            Log.e("SignReceiverSingleton", "取消注册SigninService.LocateReceiver");
        }
    }
}
