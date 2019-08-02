package com.example.renliqian.alarmmanagersignin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.renliqian.alarmmanagersignin.modle.DaoMaster;
import com.example.renliqian.alarmmanagersignin.modle.DaoSession;
import com.example.renliqian.alarmmanagersignin.modle.SignAlarm;
import com.example.renliqian.alarmmanagersignin.modle.SignAlarmDao;
import com.example.renliqian.alarmmanagersignin.modle.SignInfo;
import com.example.renliqian.alarmmanagersignin.modle.SignInfoDao;

import org.greenrobot.greendao.query.Query;

/**
 * Created by admin on 2017/9/28.
 */

public class MyDatabase {

    Context context;
    static MyDatabase instance;
    static DaoSession daoSession;
    private static SignInfoDao siinfoDao;
    private static Query<SignInfo> siinfoQuery;
    private static SignAlarmDao sialarmDao;
    private static Query<SignAlarm> siAlarmQuery;

    public MyDatabase(Context context) {
        this.context = context;
        setupDataBase();
    }

    public static void init(Context context) {
        instance = new MyDatabase(context);
    }

    /**
     * 初始化数据库
     */
    private void setupDataBase() {
        //创建数据库bolian.db"

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "alarmsignin.db", null);

        //获取可写数据库

        SQLiteDatabase db = helper.getWritableDatabase();

        //获取数据库对象

        DaoMaster daoMaster = new DaoMaster(db);

        //获取Dao对象管理者

        daoSession = daoMaster.newSession();
    }


    /**
     * 签到
     *
     * @return
     */
    public static SignInfoDao getSIInfoDao() {
        if (siinfoDao == null) {
            siinfoDao = daoSession.getSignInfoDao();
        }
        return siinfoDao;
    }


    /**
     * 签到
     *
     * @return
     */
    public static Query<SignInfo> getSIInfoQuery() {
        if (siinfoQuery == null) {
            siinfoQuery = getSIInfoDao().queryBuilder().build();
        }
        return siinfoQuery;
    }

    /**
     * 签到
     *
     * @return
     */
    public static SignAlarmDao getSIAlarmDao() {
        if (sialarmDao == null) {
            sialarmDao = daoSession.getSignAlarmDao();
        }
        return sialarmDao;
    }

    /**
     * 签到
     *
     * @return
     */
    public static Query<SignAlarm> getSIAlarmQuery() {
        if (siAlarmQuery == null) {
            siAlarmQuery = getSIAlarmDao().queryBuilder().build();
        }
        return siAlarmQuery;
    }

}