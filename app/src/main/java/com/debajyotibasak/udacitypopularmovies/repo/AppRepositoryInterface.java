package com.debajyotibasak.udacitypopularmovies.repo;

import android.arch.lifecycle.LiveData;

import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;

import java.util.List;

public interface AppRepositoryInterface {

    LiveData<List<MovieEntity>> getMoviesData(String sortBy, boolean doForceLoad);

    LiveData<List<MovieEntity>> getMoviesFromDb();

    LiveData<Boolean> getGenres();

    LiveData<List<GenreEntity>> getGenresById(List<Integer> genreIds);

}
