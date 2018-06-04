package com.debajyotibasak.udacitypopularmovies.di.module;

import com.debajyotibasak.udacitypopularmovies.view.ui.home.HomeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract HomeActivity contributeHomeActivity();
}