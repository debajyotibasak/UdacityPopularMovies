package com.debajyotibasak.udacitypopularmovies.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveToDb(List<MovieEntity> movieList);

    @Query("SELECT * FROM movies ORDER BY _id ASC")
    LiveData<List<MovieEntity>> loadFromDb();

    @Query("DELETE FROM movies")
    void deleteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveGenresToDb(List<GenreEntity> genreList);

    @Query("SELECT * FROM genres WHERE genreId IN (:genreIds)")
    LiveData<List<GenreEntity>> getGenresById(List<Integer> genreIds);
}
