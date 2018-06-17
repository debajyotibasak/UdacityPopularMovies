package com.debajyotibasak.udacitypopularmovies.di.component;

import android.app.Application;

import com.debajyotibasak.udacitypopularmovies.MoviesApp;
import com.debajyotibasak.udacitypopularmovies.di.module.ActivityModule;
import com.debajyotibasak.udacitypopularmovies.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules={ActivityModule.class, AppModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(MoviesApp app);
}
