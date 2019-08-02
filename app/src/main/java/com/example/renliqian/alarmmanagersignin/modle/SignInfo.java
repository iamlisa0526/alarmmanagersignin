package com.example.renliqian.alarmmanagersignin.modle;

import com.example.renliqian.alarmmanagersignin.db.IntegerListConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.util.List;


/**
 * @Description: 作用描述
 * @Author: lisa
 * @CreateDate: 2019/6/14 8:33
 */
@Entity
public class SignInfo {
    private int localId;//用于本地区分不同的AlarmManager
    private String address;
    private String id;
    private double latitude;
    private double longitude;
    private String name;
    private int signInScope;
    private long signInTime;

    @Convert(columnType = String.class, converter = IntegerListConverter.class)
    private List<Integer> schoolDayList;

    @Generated(hash = 123281580)
    public SignInfo(int localId, String address, String id, double latitude,
                    double longitude, String name, int signInScope, long signInTime,
                    List<Integer> schoolDayList) {
        this.localId = localId;
        this.address = address;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.signInScope = signInScope;
        this.signInTime = signInTime;
        this.schoolDayList = schoolDayList;
    }

    @Generated(hash = 1879720682)
    public SignInfo() {
    }

    public int getLocalId() {
        return this.localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSignInScope() {
        return this.signInScope;
    }

    public void setSignInScope(int signInScope) {
        this.signInScope = signInScope;
    }

    public long getSignInTime() {
        return this.signInTime;
    }

    public void setSignInTime(long signInTime) {
        this.signInTime = signInTime;
    }

    public List<Integer> getSchoolDayList() {
        return this.schoolDayList;
    }

    public void setSchoolDayList(List<Integer> schoolDayList) {
        this.schoolDayList = schoolDayList;
    }
}
