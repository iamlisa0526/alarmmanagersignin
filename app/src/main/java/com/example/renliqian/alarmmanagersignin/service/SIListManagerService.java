package com.example.renliqian.alarmmanagersignin.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.renliqian.alarmmanagersignin.db.MyDatabase;
import com.example.renliqian.alarmmanagersignin.modle.SignAlarm;
import com.example.renliqian.alarmmanagersignin.modle.SignInfo;
import com.example.renliqian.alarmmanagersignin.singleton.SignReceiverSingleton;
import com.example.renliqian.alarmmanagersignin.utils.DateUtils;
import com.xdandroid.hellodaemon.AbsWorkService;
import com.xdandroid.hellodaemon.DaemonEnv;

import java.util.Calendar;
import java.util.List;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static com.example.renliqian.alarmmanagersignin.constants.SignConstants.INTERVALMILLIS_2_HOURS;
import static com.example.renliqian.alarmmanagersignin.constants.SignConstants.INTERVALMILLIS_7_DAYS;

/**
 * @Description: 定时签到列表服务
 * @Author: lisa
 * @CreateDate: 2019/6/24 14:58
 */
public class SIListManagerService extends AbsWorkService {
    //是否 任务完成, 不再需要服务运行?
    public static boolean sShouldStopService;

    /**
     * 停止服务
     */
    public static void stopService() {
        //我们现在不再需要服务运行了, 将标志位置为 true
        sShouldStopService = true;
        //取消 Job / Alarm / Subscription
        cancelJobAlarmSub();

        unRegisterReceiver();
    }

    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return sShouldStopService;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("SIListManagerService", "onCreate");
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        //注册广播
        SignReceiverSingleton.getSIListReceiverInstance();

        //遍历签到记录缓存，创建每个签到对应的所有AlarmManager
        List<SignInfo> siList = MyDatabase.getSIInfoQuery().list();
        for (SignInfo signInfo : siList) {
            if (signInfo.getSchoolDayList() != null && !signInfo.getSchoolDayList().isEmpty()) {
                setAlarmManager(signInfo);
            }
        }

        Log.e("SIListManagerService", "startWork");
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {

        Log.e("SIListManagerService", "stopWork");
    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {

        Log.e("SIListManagerService", "onServiceKilled");
    }

    /**
     * 销毁数据
     */
    public static void unRegisterReceiver() {
        SignReceiverSingleton.resetSIListReceiverNull();

        //删除SignAlarm缓存
        MyDatabase.getSIAlarmDao().deleteAll();

    }


    /**
     * 设置定时器
     *
     * @param signInfo
     */
    private void setAlarmManager(SignInfo signInfo) {

        //获取签到时分
        String hhmm = DateUtils.toHHMM(signInfo.getSignInTime());
        String[] split = hhmm.split(":");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);
        long signId = Long.parseLong(signInfo.getId());
        String signName = signInfo.getName();
        for (int it : signInfo.getSchoolDayList()) {

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour - 1);//TODO hour<1的情况
            c.set(Calendar.MINUTE, minute);

            int today = c.get(Calendar.DAY_OF_WEEK);//Calendar周日，周一..周六对应1-7
            int nextDay;
            if (today - 1 > it) {//今天之前第一次定位在下周开始
                nextDay = 7 - (today - 1) + it;
            } else if (today - 1 < it) {//今天之后第一次定位在这周开始
                nextDay = it - (today - 1);
            } else {//今天
                if (System.currentTimeMillis() > c.getTimeInMillis() + INTERVALMILLIS_2_HOURS) {//当前时间>今天签到截止时间，7天以后启动
                    nextDay = 7;
                } else {//时间未过，今天到点启动，如果再开始和截止之间，利用AlarmManager立马启动的原理
                    nextDay = 0;
                }
            }

            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + nextDay);
            long firstTime = c.getTimeInMillis();

            int requestCode = Integer.parseInt((String.valueOf(signInfo.getLocalId()) + String.valueOf(it)));

            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(SignReceiverSingleton.getSIListReceiverActionInstance());
            intent.putExtra("signId", signId);
            intent.putExtra("firstTime", firstTime);
            intent.putExtra("requestCode", requestCode);
            intent.putExtra("signName", signName);

            PendingIntent pi = PendingIntent.getBroadcast(this, requestCode, intent, FLAG_CANCEL_CURRENT);//requestCode用于区分多个AlarmManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//minSdkVersion 19
                if (am != null) {
                    am.setWindow(AlarmManager.RTC_WAKEUP, firstTime, INTERVALMILLIS_7_DAYS, pi);
                }
            } else {
                am.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, INTERVALMILLIS_7_DAYS, pi);
            }

            Log.e("SIListManagerService", "signName==" + signName + "，firstTime==" + DateUtils.toYearOfSecond(firstTime) + "，requestCode==" + requestCode + "，signId==" + signId);
        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SigninService.unRegisterReceiver();

            //缓存数据
            SignAlarm signAlarm = new SignAlarm();
            signAlarm.setSignId(intent.getLongExtra("signId", 0));
            signAlarm.setFirstTime(intent.getLongExtra("firstTime", 0));
            signAlarm.setRequestCode(intent.getIntExtra("requestCode", 0));
            signAlarm.setSignName(intent.getStringExtra("signName"));

            MyDatabase.getSIAlarmDao().insertOrReplace(signAlarm);

            //重启定时签到服务
            SigninService.sShouldStopService = false;
            DaemonEnv.startServiceMayBind(SigninService.class);

            Log.e("AlarmReceiver", "onReceive");

            //api>19时通过广播设置下一次定时
            long firstTime = System.currentTimeMillis() + INTERVALMILLIS_7_DAYS;
            intent.putExtra("firstTime", firstTime);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getBroadcast(context, intent.getIntExtra("requestCode", 0), intent, FLAG_CANCEL_CURRENT);//requestCode用于区分多个AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (am != null) {
                    am.setWindow(AlarmManager.RTC_WAKEUP, firstTime, INTERVALMILLIS_7_DAYS, pi);
                }
            }
        }

    }
}
