package com.example.renliqian.alarmmanagersignin.modle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @Description: 作用描述
 * @Author: lisa
 * @CreateDate: 2019/6/24 9:04
 */
@Entity
public class SignAlarm {
    private long signId;
    private long firstTime;
    @Unique
    private int requestCode;
    private String signName;

    @Generated(hash = 1620268324)
    public SignAlarm(long signId, long firstTime, int requestCode,
                     String signName) {
        this.signId = signId;
        this.firstTime = firstTime;
        this.requestCode = requestCode;
        this.signName = signName;
    }

    @Generated(hash = 355210282)
    public SignAlarm() {
    }

    public long getSignId() {
        return this.signId;
    }

    public void setSignId(long signId) {
        this.signId = signId;
    }

    public long getFirstTime() {
        return this.firstTime;
    }

    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getSignName() {
        return this.signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }
}
