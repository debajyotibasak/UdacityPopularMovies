package com.debajyotibasak.udacitypopularmovies.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.debajyotibasak.udacitypopularmovies.api.ApiInterface;
import com.debajyotibasak.udacitypopularmovies.api.ApiResponse;
import com.debajyotibasak.udacitypopularmovies.api.model.GenreResponse;
import com.debajyotibasak.udacitypopularmovies.api.model.MoviesResponse;
import com.debajyotibasak.udacitypopularmovies.database.dao.MoviesDao;
import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;

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
    public LiveData<ApiResponse<MoviesResponse>> getMovies(String sortBy) {
        final MutableLiveData<ApiResponse<MoviesResponse>> liveData = new MutableLiveData<>();
        Call<MoviesResponse> call = apiInterface.getMovies(sortBy, AppConstants.LANGUAGE, AppConstants.PAGE);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body().getResults() != null) {
                    liveData.setValue(new ApiResponse<>(response.body()));
                    if (!response.body().getResults().isEmpty()) {
                        liveData.observeForever(moviesResponseApiResponse ->
                                executor.execute(() -> {
                                    moviesDao.deleteMovies();
                                    moviesDao.saveToDb(response.body().getResults());
                                }));
                    } else {
                        liveData.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                liveData.setValue(new ApiResponse<>(t));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<List<MovieEntity>> getMoviesFromDb() {
        return moviesDao.loadFromDb();
    }

    @Override
    public LiveData<ApiResponse<GenreResponse>> getGenres() {
        final MutableLiveData<ApiResponse<GenreResponse>> liveData = new MutableLiveData<>();
        Call<GenreResponse> call = apiInterface.getGenres(AppConstants.LANGUAGE);
        call.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenreResponse> call, @NonNull Response<GenreResponse> response) {
                if (response.isSuccessful() && response.body().getGenres() != null) {
                    liveData.setValue(new ApiResponse<>(response.body()));
                    if (!response.body().getGenres().isEmpty()) {
                        liveData.observeForever(apiResponse ->
                                executor.execute(() -> {
                                    moviesDao.saveGenresToDb(response.body().getGenres());
                                }));
                    } else {
                        liveData.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenreResponse> call, @NonNull Throwable t) {
                liveData.setValue(new ApiResponse<>(t));
            }
        });

        return liveData;
    }

    @Override
    public LiveData<List<GenreEntity>> getGenresById(List<Integer> genreIds) {
        return moviesDao.getGenresById(genreIds);
    }
}
