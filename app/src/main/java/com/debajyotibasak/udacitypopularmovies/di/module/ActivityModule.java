package com.debajyotibasak.udacitypopularmovies.di.module;

import com.debajyotibasak.udacitypopularmovies.view.ui.detail.DetailActivity;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.reviews.ReviewsActivity;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.trailers.TrailerActivity;
import com.debajyotibasak.udacitypopularmovies.view.ui.home.HomeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract HomeActivity contributeHomeActivity();

    @ContributesAndroidInjector
    abstract DetailActivity contributeDetailActivity();

    @ContributesAndroidInjector
    abstract ReviewsActivity contributeReviewsActivity();

    @ContributesAndroidInjector
    abstract TrailerActivity contributeTrailerActivity();
}