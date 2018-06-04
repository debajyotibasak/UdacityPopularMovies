package com.debajyotibasak.udacitypopularmovies.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.debajyotibasak.udacitypopularmovies.di.interfaces.ViewModelKey;
import com.debajyotibasak.udacitypopularmovies.factory.ViewModelFactory;
import com.debajyotibasak.udacitypopularmovies.view.ui.home.HomeViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
