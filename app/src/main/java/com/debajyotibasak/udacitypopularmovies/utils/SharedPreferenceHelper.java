package com.debajyotibasak.udacitypopularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {

    public static void setSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(AppConstants.PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getSharedPreferenceString(Context context, String key, String defValue) {
        SharedPreferences settings = context.getSharedPreferences(AppConstants.PREF_FILE, 0);
        return settings.getString(key, defValue);
    }

    public static boolean contains(Context context, String key){
        SharedPreferences settings = context.getSharedPreferences(AppConstants.PREF_FILE, 0);
        return settings.contains(key);
    }

}