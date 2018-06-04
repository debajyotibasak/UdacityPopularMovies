package com.debajyotibasak.udacitypopularmovies.api;

import com.debajyotibasak.udacitypopularmovies.api.model.MoviesResponse;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("discover/movie")
    Call<MoviesResponse> discoverMovies(@Query("sort_by") String sortBy, @Query("page") int page);
}
