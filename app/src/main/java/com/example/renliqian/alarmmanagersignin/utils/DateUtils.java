package com.example.renliqian.alarmmanagersignin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 日期转换工具类
 * @Author: lisa
 * @CreateDate: 2019/8/2 15:42
 */
public class DateUtils {
    /**
     * 将time转成 分:秒(30:00) 格式
     *
     * @param time
     * @return
     */
    public static String toHHMM(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        return sdf.format(new Date(time));
    }

    /**
     * 将time转成 年月日 HH:ss(2015年11月11日 12：05) 格式
     *
     * @param time
     * @return
     */
    public static String toYearOfSecond(long time) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

        return sdf.format(new Date(time));
    }
}
