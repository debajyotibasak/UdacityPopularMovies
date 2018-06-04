package com.debajyotibasak.udacitypopularmovies.repo;

import android.arch.lifecycle.LiveData;

import com.debajyotibasak.udacitypopularmovies.api.ApiResponse;
import com.debajyotibasak.udacitypopularmovies.api.model.MoviesResponse;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;

import java.util.List;

public interface AppRepositoryInterface {

    LiveData<ApiResponse<MoviesResponse>> getMovies(String sortBy);

    LiveData<List<MovieEntity>> getMoviesFromDb();
}
