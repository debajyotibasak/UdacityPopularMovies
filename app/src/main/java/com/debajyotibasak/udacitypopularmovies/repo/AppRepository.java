package com.debajyotibasak.udacitypopularmovies.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.debajyotibasak.udacitypopularmovies.MoviesApp;
import com.debajyotibasak.udacitypopularmovies.api.ApiInterface;
import com.debajyotibasak.udacitypopularmovies.api.model.GenreResponse;
import com.debajyotibasak.udacitypopularmovies.api.model.MoviesResponse;
import com.debajyotibasak.udacitypopularmovies.database.dao.MoviesDao;
import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;
import com.debajyotibasak.udacitypopularmovies.utils.AppUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public LiveData<Boolean> getGenres() {
        final MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        apiInterface.getGenres(AppConstants.LANGUAGE).enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenreResponse> call, @NonNull Response<GenreResponse> response) {
                if (response.isSuccessful() && response.body().getGenres() != null) {
                    if (!response.body().getGenres().isEmpty()) {
                        liveData.setValue(true);
                        liveData.observeForever(apiResponse -> executor.execute(() -> moviesDao.saveGenresToDb(response.body().getGenres())));
                    } else {
                        liveData.setValue(false);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenreResponse> call, @NonNull Throwable t) {
                liveData.setValue(false);
            }
        });
        return liveData;
    }


    @Override
    public LiveData<List<GenreEntity>> getGenresById(List<Integer> genreIds) {
        return moviesDao.getGenresById(genreIds);
    }

    // Movie Loading Logic

    @Override
    public LiveData<List<MovieEntity>> getMoviesData(String sortBy, boolean doForceLoad) {
        refreshMovies(sortBy, doForceLoad);
        return moviesDao.loadFromDb();
    }

    private void refreshMovies(String sortBy, boolean doForceLoad) {
        executor.execute(() -> {
            boolean moviesExists = false;
            if (MoviesApp.getDateInserted() != null) {
                moviesExists = AppUtils.minutesBetween(new Date(), MoviesApp.getDateInserted()) < AppConstants.FRESH_TIMEOUT_IN_MINUTES;
            }
            if ((!moviesExists && doForceLoad) || !moviesExists || doForceLoad) {
                loadMovie(sortBy);
            }
        });
    }

    private void loadMovie(String sortBy) {
        apiInterface.getMovies(sortBy, AppConstants.LANGUAGE, AppConstants.PAGE).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                MoviesApp.setDateInserted(new Date());
                executor.execute(() -> {
                    moviesDao.deleteMovies();
                    moviesDao.saveToDb(response.body().getResults());
                });
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
            }
        });
    }

    @Override
    public LiveData<List<MovieEntity>> getMoviesFromDb() {
        return moviesDao.loadFromDb();
    }
}
