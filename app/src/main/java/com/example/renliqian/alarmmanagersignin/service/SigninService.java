package com.example.renliqian.alarmmanagersignin.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.renliqian.alarmmanagersignin.R;
import com.example.renliqian.alarmmanagersignin.db.MyDatabase;
import com.example.renliqian.alarmmanagersignin.modle.SignAlarm;
import com.example.renliqian.alarmmanagersignin.modle.SignInRecordQo;
import com.example.renliqian.alarmmanagersignin.singleton.SignReceiverSingleton;
import com.example.renliqian.alarmmanagersignin.utils.DateUtils;
import com.example.renliqian.alarmmanagersignin.utils.GsonUtils;
import com.example.renliqian.alarmmanagersignin.utils.NetworkUtils;
import com.xdandroid.hellodaemon.AbsWorkService;

import java.util.List;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static com.example.renliqian.alarmmanagersignin.constants.SignConstants.INTERVALMILLIS_10_MINUTES;
import static com.example.renliqian.alarmmanagersignin.constants.SignConstants.INTERVALMILLIS_1_MINUTES;
import static com.example.renliqian.alarmmanagersignin.constants.SignConstants.INTERVALMILLIS_2_HOURS;

/**
 * @Description: 签到服务
 * @Author: lisa
 * @CreateDate: 2019/7/9 15:34
 */
public class SigninService extends AbsWorkService {

    //是否 任务完成, 不再需要服务运行?
    public static boolean sShouldStopService;

    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return sShouldStopService;
    }

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
    public void onCreate() {
        super.onCreate();
        Log.e("SigninService", "onCreate");
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        SignReceiverSingleton.getSINetReceiverInstance();
        SignReceiverSingleton.getSILocateReceiverInstance();

        //遍历缓存，创建每个签到对应的网络提醒和定位定时器
        List<SignAlarm> siList = MyDatabase.getSIAlarmQuery().list();

        Log.e("SigninService", "当前要发送位置信息的定时器个数==" + siList.size());
        for (SignAlarm signAlarm : siList) {
            setAlarmManager(signAlarm);
        }
        Log.e("SigninService", "startWork");
    }

    private void setAlarmManager(SignAlarm signAlarm) {
        //网络提醒通知
        AlarmManager am1 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(SignReceiverSingleton.getSINetReceiverActionInstance()).putExtra("requestCode", signAlarm.getRequestCode()).putExtra("firstTime", signAlarm.getFirstTime());
        PendingIntent pi1 = PendingIntent.getBroadcast(this, signAlarm.getRequestCode(), intent1, FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am1.setWindow(AlarmManager.RTC_WAKEUP, signAlarm.getFirstTime(), INTERVALMILLIS_10_MINUTES, pi1);//10分钟提醒一次
        } else {
            am1.setRepeating(AlarmManager.RTC_WAKEUP, signAlarm.getFirstTime(), INTERVALMILLIS_10_MINUTES, pi1);//10分钟提醒一次
        }

        //开启定位
        AlarmManager am2 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(SignReceiverSingleton.getSILocateReceiverActionInstance()).putExtra("requestCode", signAlarm.getRequestCode()).putExtra("signId", signAlarm.getSignId()).putExtra("firstTime", signAlarm.getFirstTime());
        PendingIntent pi2 = PendingIntent.getBroadcast(this, signAlarm.getRequestCode(), intent2, FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am2.setWindow(AlarmManager.RTC_WAKEUP, signAlarm.getFirstTime(), INTERVALMILLIS_1_MINUTES, pi2);//1分钟重新唤醒一次定位
        } else {
            am2.setRepeating(AlarmManager.RTC_WAKEUP, signAlarm.getFirstTime(), INTERVALMILLIS_1_MINUTES, pi2);//1分钟重新唤醒一次定位
        }

        Log.e("SigninService", "startWork：signName==" + signAlarm.getSignName() + "，firstTime==" + DateUtils.toYearOfSecond(signAlarm.getFirstTime()) + "，requestCode==" + signAlarm.getRequestCode() + "，signId==" + signAlarm.getSignId());
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        Log.e("SigninService", "stopWork");
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
        Log.e("SigninService", "onServiceKilled");
    }

    /**
     * 销毁数据
     */
    public static void unRegisterReceiver() {
        SignReceiverSingleton.resetSINetReceiverNull();
        SignReceiverSingleton.resetSILocateReceiverNull();
    }

    /**
     * 网络通知广播
     */
    public static class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (System.currentTimeMillis() - intent.getLongExtra("firstTime", 0) < INTERVALMILLIS_2_HOURS) {//2小时后停止定时

                if (!NetworkUtils.isNetWorkAvailable(context)) {
                    NotificationManager nfManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//此处必须兼容android O(sdk26)设备，否则系统版本在O以上可能不展示通知栏
                        builder = new Notification.Builder(context, "bolian");
                    } else {
                        builder = new Notification.Builder(context);
                    }

                    builder.setDefaults(Notification.DEFAULT_SOUND)//通知铃声
                            .setPriority(Notification.PRIORITY_HIGH)//优先级（这样才能发出浮动通知）
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("网络未开启")
                            .setContentText("您有一个签到设置需要上传位置信息，请开启网络连接");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
                    }

                    nfManager.notify(getResultCode(), builder.build());

                    Log.e("网络提醒通知", "您有签到需要上传位置信息，请开启网络连接");
                }

                //api>19时通过广播设置下一次定时
                AlarmManager am1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pi1 = PendingIntent.getBroadcast(context, intent.getIntExtra("requestCode", 0), intent, PendingIntent.FLAG_CANCEL_CURRENT);//requestCode用于区分多个AlarmManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    am1.setWindow(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + INTERVALMILLIS_10_MINUTES, INTERVALMILLIS_10_MINUTES, pi1);
                }

            }

        }

    }

    /**
     * 定位广播
     */
   public static class LocateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            if (System.currentTimeMillis() - intent.getLongExtra("firstTime", 0) < INTERVALMILLIS_2_HOURS) {//2小时后停止定时

                if (NetworkUtils.isNetWorkAvailable(context)) {
                    final AMapLocationClient mLocationClient = new AMapLocationClient(context);
                    AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
                    // 是否使用连续定位
                    mLocationOption.setOnceLocation(true);
                    // 每1分钟定位一次
//                    mLocationOption.interval = INTERVALMILLIS_1_MINUTES
                    mLocationClient.setLocationOption(mLocationOption);

                    SignInRecordQo.ClassQo classQo = new SignInRecordQo.ClassQo();

                    final SignInRecordQo signInRecordQo = new SignInRecordQo();
                    signInRecordQo.setClassQo(classQo);
                    signInRecordQo.setSetId(intent.getLongExtra("signId", 0));

                    mLocationClient.setLocationListener(new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation aMapLocation) {
                            if (System.currentTimeMillis() - intent.getLongExtra("firstTime", 0) < INTERVALMILLIS_2_HOURS) {//2小时后停止定时
                                //发送位置信息
                                if (NetworkUtils.isNetWorkAvailable(context)) {

                                    signInRecordQo.setLongitude(aMapLocation.getLongitude());
                                    signInRecordQo.setLatitude(aMapLocation.getLatitude());
                                    Log.e("LocateReceiver", "发送消息>>>message:" + "，message:" + GsonUtils.toJson(signInRecordQo));
                                }
                            } else {
                                mLocationClient.stopLocation();
                            }
                        }
                    });
                    mLocationClient.startLocation();
                }

                //api>19时通过广播设置下一次定时
                AlarmManager am2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pi2 = PendingIntent.getBroadcast(context, intent.getIntExtra("requestCode", 0), intent, PendingIntent.FLAG_CANCEL_CURRENT);//requestCode用于区分多个AlarmManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    am2.setWindow(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + INTERVALMILLIS_1_MINUTES, INTERVALMILLIS_1_MINUTES, pi2);
                }

            }
        }

    }
}
