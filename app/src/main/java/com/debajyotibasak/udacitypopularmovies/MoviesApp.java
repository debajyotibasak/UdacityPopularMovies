package com.debajyotibasak.udacitypopularmovies;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.debajyotibasak.udacitypopularmovies.di.component.DaggerAppComponent;

import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class MoviesApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    public static Context context;
    public static Date dateInserted;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initDagger();
        context = getApplicationContext();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    private void initDagger(){
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }

    public static Context getContext() {
        return context;
    }

    public static Date getDateInserted() {
        return dateInserted;
    }

    public static void setDateInserted(Date dateInserted) {
        MoviesApp.dateInserted = dateInserted;
    }
}
