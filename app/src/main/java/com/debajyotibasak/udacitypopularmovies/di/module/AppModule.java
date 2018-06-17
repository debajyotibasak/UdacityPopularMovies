package com.debajyotibasak.udacitypopularmovies.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.debajyotibasak.udacitypopularmovies.api.ApiInterface;
import com.debajyotibasak.udacitypopularmovies.api.interceptors.AuthenticationInterceptor;
import com.debajyotibasak.udacitypopularmovies.database.MoviesDb;
import com.debajyotibasak.udacitypopularmovies.database.dao.MoviesDao;
import com.debajyotibasak.udacitypopularmovies.repo.AppRepository;
import com.debajyotibasak.udacitypopularmovies.utils.AppExecutor;
import com.debajyotibasak.udacitypopularmovies.utils.LiveDataCallAdapterFactory;
import com.debajyotibasak.udacitypopularmovies.utils.MainThreadExecutor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.BASE_URL;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.DB_NAME;

@Module(includes = ViewModelModule.class)
public class AppModule {

    // --- REPOSITORY INJECTION ---

    @Provides
    @Singleton
    AppRepository provideAppRepository(ApiInterface apiInterface, MoviesDao moviesDao, AppExecutor executor) {
        return new AppRepository(apiInterface, moviesDao, executor);
    }

    // --- EXECUTOR INJECTION ---

    @Provides
    AppExecutor provideAppExecutor() {
        return new AppExecutor(Executors.newSingleThreadExecutor(), new MainThreadExecutor());
    }

    // --- DATABASE INJECTION ---

    @Provides
    @Singleton
    MoviesDb provideDatabase(Application application) {
        return Room.databaseBuilder(application, MoviesDb.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    MoviesDao provideUserDao(MoviesDb database) {
        return database.moviesDao();
    }

    // --- NETWORK INJECTION ---

    @Provides
    @Singleton
    ApiInterface provideApiInterface(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson, LiveDataCallAdapterFactory liveDataCallAdapterFactory) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(liveDataCallAdapterFactory)
                .build();
    }

    @Provides
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    public OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor,
                                     AuthenticationInterceptor authenticationInterceptor) {
        return new OkHttpClient()
                .newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(authenticationInterceptor)
                .build();
    }

    @Provides
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Provides
    public LiveDataCallAdapterFactory liveDataCallAdapterFactory() {
        return new LiveDataCallAdapterFactory();
    }

}
