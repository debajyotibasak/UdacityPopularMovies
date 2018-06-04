package com.debajyotibasak.udacitypopularmovies.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IntegerListConvertor {

    @TypeConverter
    public String fromIntegerList(List<Integer> genreIdList) {
        if (genreIdList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return gson.toJson(genreIdList, type);
    }

    @TypeConverter
    public List<Integer> toIntegerList(String integerJson) {
        if (integerJson == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return gson.fromJson(integerJson, type);
    }
}
