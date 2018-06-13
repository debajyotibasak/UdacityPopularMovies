package com.debajyotibasak.udacitypopularmovies.api;

import android.arch.lifecycle.LiveData;

import com.debajyotibasak.udacitypopularmovies.api.model.GenreResponse;
import com.debajyotibasak.udacitypopularmovies.api.model.MoviesResponse;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/{type}")
    LiveData<ApiResponse<MoviesResponse>> getMovies(@Path(value="type", encoded=true) String type,
                                    @Query("language") String language,
                                    @Query("page") int page);

    @GET("genre/movie/list")
    LiveData<ApiResponse<GenreResponse>> getGenres(@Query("language") String language);
}
