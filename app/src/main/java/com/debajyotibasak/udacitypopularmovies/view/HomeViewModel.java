package com.debajyotibasak.udacitypopularmovies.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.debajyotibasak.udacitypopularmovies.api.ApiResponse;
import com.debajyotibasak.udacitypopularmovies.api.model.MoviesResponse;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.repo.AppRepository;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private AppRepository moviesRepo;

    @Inject
    public HomeViewModel(AppRepository moviesRepo) {
        this.moviesRepo = moviesRepo;
    }

    LiveData<ApiResponse<MoviesResponse>> getMovies(String sortBy, int page) {
        return moviesRepo.getMovies(sortBy, page);
    }

    LiveData<List<MovieEntity>> getMoviesFromDb(){
        return moviesRepo.getMoviesFromDb();
    }
}
