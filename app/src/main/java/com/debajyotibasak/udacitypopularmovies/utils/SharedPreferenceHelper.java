package com.debajyotibasak.udacitypopularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.debajyotibasak.udacitypopularmovies.MoviesApp;

public class SharedPreferenceHelper {

    public static void setSharedPreferenceString(String key, String value) {
        SharedPreferences settings = MoviesApp.getContext().getSharedPreferences(AppConstants.PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getSharedPreferenceString(String key, String defValue) {
        SharedPreferences settings = MoviesApp.getContext().getSharedPreferences(AppConstants.PREF_FILE, 0);
        return settings.getString(key, defValue);
    }

    public static void setSharedPreferenceBoolean(String key, boolean value) {
        SharedPreferences settings = MoviesApp.getContext().getSharedPreferences(AppConstants.PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getSharedPreferenceBoolean(String key, boolean defValue) {
        SharedPreferences settings = MoviesApp.getContext().getSharedPreferences(AppConstants.PREF_FILE, 0);
        return settings.getBoolean(key, defValue);
    }

    public static boolean contains(String key) {
        SharedPreferences settings = MoviesApp.getContext().getSharedPreferences(AppConstants.PREF_FILE, 0);
        return settings.contains(key);
    }

}