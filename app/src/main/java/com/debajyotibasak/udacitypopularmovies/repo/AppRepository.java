package com.debajyotibasak.udacitypopularmovies.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.debajyotibasak.udacitypopularmovies.api.ApiInterface;
import com.debajyotibasak.udacitypopularmovies.api.ApiResponse;
import com.debajyotibasak.udacitypopularmovies.api.model.GenreResponse;
import com.debajyotibasak.udacitypopularmovies.api.model.MoviesResponse;
import com.debajyotibasak.udacitypopularmovies.database.dao.MoviesDao;
import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;
import com.debajyotibasak.udacitypopularmovies.utils.Resource;
import com.debajyotibasak.udacitypopularmovies.utils.SharedPreferenceHelper;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppRepository implements AppRepositoryInterface {

    private final ApiInterface apiInterface;
    private final MoviesDao moviesDao;
    private final Executor executor;

    @Inject
    public AppRepository(ApiInterface apiInterface, MoviesDao moviesDao, Executor executor) {
        this.apiInterface = apiInterface;
        this.moviesDao = moviesDao;
        this.executor = executor;
    }

    @Override
    public LiveData<Resource<List<GenreEntity>>> loadGenres(List<Integer> genreIds) {
        return new NetworkBoundResource<List<GenreEntity>, GenreResponse>(executor) {
            @Override
            protected void saveCallResult(@NonNull GenreResponse item) {
                moviesDao.saveGenresToDb(item.getGenres());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<GenreEntity> data) {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<GenreEntity>> loadFromDb() {
                return moviesDao.getGenresById(genreIds);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<GenreResponse>> createCall() {
                return apiInterface.getGenres(AppConstants.LANGUAGE);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<List<MovieEntity>>> loadMovies(boolean forceLoad, String sortBy) {
        return new NetworkBoundResource<List<MovieEntity>, MoviesResponse>(executor) {
            @Override
            protected void saveCallResult(@NonNull MoviesResponse item) {
                SharedPreferenceHelper.setSharedPreferenceLong(AppConstants.DATA_SAVED_TIME, new Date(System.currentTimeMillis()).getTime());
                moviesDao.deleteMovies();
                moviesDao.saveMoviesToDb(item.getResults());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<MovieEntity> data) {
                return (((data == null || data.isEmpty()) && forceLoad && shouldFetchData(new Date(System.currentTimeMillis()).getTime()))
                        || (data == null || data.isEmpty())
                        || forceLoad
                        || shouldFetchData(new Date(System.currentTimeMillis()).getTime()));
            }

            @NonNull
            @Override
            protected LiveData<List<MovieEntity>> loadFromDb() {
                return moviesDao.loadFromDb();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MoviesResponse>> createCall() {
                return apiInterface.getMovies(sortBy, AppConstants.LANGUAGE, AppConstants.PAGE);
            }
        }.asLiveData();
    }

    private Boolean shouldFetchData(Long time) {
        boolean shouldFetch;
        long savedTime;
        if (SharedPreferenceHelper.contains(AppConstants.DATA_SAVED_TIME)) {
            savedTime = SharedPreferenceHelper.getSharedPreferenceLong(AppConstants.DATA_SAVED_TIME, 0L);
            shouldFetch = (time - savedTime) > TimeUnit.MINUTES.toMillis(AppConstants.FRESH_TIMEOUT_IN_MINUTES);
        } else {
            shouldFetch = false;
        }
        return shouldFetch;
    }
}
