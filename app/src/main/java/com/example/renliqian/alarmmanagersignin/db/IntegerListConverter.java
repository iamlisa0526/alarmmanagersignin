package com.example.renliqian.alarmmanagersignin.db;

import com.example.renliqian.alarmmanagersignin.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;


/**
 * Created by renliqian on 2017/10/2.
 */

public class IntegerListConverter implements PropertyConverter<List<Integer>, String> {

    @Override
    public List<Integer> convertToEntityProperty(String json) {

        return GsonUtils.fromJson(json, new TypeToken<List<Integer>>() {
        }.getType());
    }

    @Override
    public String convertToDatabaseValue(List<Integer> list) {
        return GsonUtils.toJson(list);
    }
}
