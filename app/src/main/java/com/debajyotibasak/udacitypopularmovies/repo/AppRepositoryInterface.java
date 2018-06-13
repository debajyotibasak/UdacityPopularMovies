package com.debajyotibasak.udacitypopularmovies.repo;

import android.arch.lifecycle.LiveData;

import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.utils.Resource;

import java.util.List;

public interface AppRepositoryInterface {

    /*LiveData<List<MovieEntity>> getMoviesData(String sortBy, boolean doForceLoad);

    LiveData<List<MovieEntity>> getMoviesFromDb();

    LiveData<Boolean> getGenres();

    LiveData<List<GenreEntity>> getGenresById(List<Integer> genreIds);*/

    LiveData<Resource<List<GenreEntity>>> loadGenres(List<Integer> genreIds);

    LiveData<Resource<List<MovieEntity>>> loadMovies(boolean forceLoad, String sortBy);

}
