package com.debajyotibasak.udacitypopularmovies.repo;

import android.arch.lifecycle.LiveData;

import com.debajyotibasak.udacitypopularmovies.api.model.CastResponse;
import com.debajyotibasak.udacitypopularmovies.api.model.CastResult;
import com.debajyotibasak.udacitypopularmovies.api.model.ReviewResult;
import com.debajyotibasak.udacitypopularmovies.api.model.VideoResults;
import com.debajyotibasak.udacitypopularmovies.database.entity.FavMovieCastEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.FavMovieEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.FavMovieReviewEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.FavMovieVideoEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.utils.Resource;

import java.util.List;

public interface AppRepositoryInterface {
    LiveData<Resource<List<GenreEntity>>> loadGenres(List<Integer> genreIds);

    LiveData<Resource<List<MovieEntity>>> loadMovies(boolean forceLoad, String sortBy);

    LiveData<Resource<List<CastResult>>> loadCast(int movieId);

    LiveData<Resource<List<VideoResults>>> loadVideos(int movieId);

    LiveData<Resource<List<ReviewResult>>> loadReviews(int movieId);

    void saveFavouriteMovie(FavMovieEntity favMovieEntity);

    void saveFavMovieCast(List<FavMovieCastEntity> favMovieCastEntities);

    void saveFavMovieReviews(List<FavMovieReviewEntity> favMovieReviewEntities);

    void saveFavMovieVideos(List<FavMovieVideoEntity> favMovieVideoEntities);

    LiveData<Integer> containsMovie(int movieId);

    LiveData<List<FavMovieEntity>> loadFavMoviesFromDb();

    LiveData<FavMovieEntity> loadFavMovieById(int movieId);

    LiveData<List<FavMovieCastEntity>> getCastsById(List<Integer> castIds);

    LiveData<List<FavMovieReviewEntity>> getReviewsByMovie(int favMovieId);

    LiveData<List<FavMovieVideoEntity>> getVideosByMovie(int favMovieId);

    LiveData<Integer> deleteMovieById(int favMovieId);
}
