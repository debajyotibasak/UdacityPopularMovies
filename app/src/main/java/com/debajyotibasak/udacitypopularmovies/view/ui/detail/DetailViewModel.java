package com.debajyotibasak.udacitypopularmovies.view.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.debajyotibasak.udacitypopularmovies.api.model.CastResult;
import com.debajyotibasak.udacitypopularmovies.api.model.ReviewResult;
import com.debajyotibasak.udacitypopularmovies.api.model.VideoResults;
import com.debajyotibasak.udacitypopularmovies.database.entity.FavMovieCastEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.FavMovieEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.FavMovieReviewEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.FavMovieVideoEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.repo.AppRepository;
import com.debajyotibasak.udacitypopularmovies.utils.Resource;
import com.debajyotibasak.udacitypopularmovies.utils.SharedPreferenceHelper;

import java.util.List;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    private AppRepository moviesRepo;
    private MutableLiveData<Resource<List<CastResult>>> castResults = new MutableLiveData<>();
    private MutableLiveData<Resource<List<VideoResults>>> videoResults = new MutableLiveData<>();
    private MutableLiveData<Resource<List<ReviewResult>>> reviewResult = new MutableLiveData<>();
    private MutableLiveData<MovieEntity> movieResult = new MutableLiveData<>();
    private MutableLiveData<Integer> isFavorite = new MutableLiveData<>();

    @Inject
    public DetailViewModel(AppRepository moviesRepo) {
        this.moviesRepo = moviesRepo;

        moviesRepo.loadCast(SharedPreferenceHelper.getSharedPreferenceInt("mId"))
                .observeForever(listResource -> castResults.setValue(listResource));

        moviesRepo.loadReviews(SharedPreferenceHelper.getSharedPreferenceInt("mId"))
                .observeForever(listResource -> reviewResult.setValue(listResource));

        moviesRepo.loadVideos(SharedPreferenceHelper.getSharedPreferenceInt("mId"))
                .observeForever(listResource -> videoResults.setValue(listResource));

        moviesRepo.loadMoviesById(SharedPreferenceHelper.getSharedPreferenceInt("mId"))
                .observeForever(movieEntity -> movieResult.setValue(movieEntity));
    }

    LiveData<Resource<List<GenreEntity>>> getGenresById(List<Integer> genreIds) {
        return moviesRepo.loadGenres(genreIds);
    }

    public MutableLiveData<MovieEntity> getMovieResult() {
        return movieResult;
    }

    public MutableLiveData<Resource<List<CastResult>>> getCastResults() {
        return castResults;
    }

    public MutableLiveData<Resource<List<VideoResults>>> getVideoResults() {
        return videoResults;
    }

    public MutableLiveData<Resource<List<ReviewResult>>> getReviewResult() {
        return reviewResult;
    }

    void saveFavMovies(FavMovieEntity favMovieEntity) {
        moviesRepo.saveFavouriteMovie(favMovieEntity);
    }

    void saveFavCast(List<FavMovieCastEntity> favCast) {
        moviesRepo.saveFavMovieCast(favCast);
    }

    void saveFavReviews(List<FavMovieReviewEntity> favReviews) {
        moviesRepo.saveFavMovieReviews(favReviews);
    }

    void saveFavTrailers(List<FavMovieVideoEntity> favTrailers) {
        moviesRepo.saveFavMovieVideos(favTrailers);
    }

    LiveData<List<FavMovieEntity>> loadFavMoviesFromDb() {
        return moviesRepo.loadFavMoviesFromDb();
    }

    LiveData<FavMovieEntity> loadFavMoviesById(int favMovieId) {
        return moviesRepo.loadFavMovieById(favMovieId);
    }

    LiveData<List<FavMovieCastEntity>> getFavCasts(List<Integer> castIds) {
        return moviesRepo.getCastsById(castIds);
    }

    LiveData<List<FavMovieReviewEntity>> getFavReviews(int favMovieId) {
        return moviesRepo.getReviewsByMovie(favMovieId);
    }

    LiveData<List<FavMovieVideoEntity>> getFavVideos(int favMovieId) {
        return moviesRepo.getVideosByMovie(favMovieId);
    }

    LiveData<Integer> deleteMovieById(int favMovieId) {
        return moviesRepo.deleteMovieById(favMovieId);
    }
}
