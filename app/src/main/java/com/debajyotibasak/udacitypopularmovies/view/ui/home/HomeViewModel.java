package com.debajyotibasak.udacitypopularmovies.view.ui.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.repo.AppRepository;
import com.debajyotibasak.udacitypopularmovies.utils.Resource;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private AppRepository moviesRepo;

    @Inject
    HomeViewModel(AppRepository moviesRepo) {
        this.moviesRepo = moviesRepo;
    }

    /*LiveData<List<MovieEntity>> getMoviesFromDb() {
        return moviesRepo.getMoviesFromDb();
    }

    LiveData<List<MovieEntity>> getMoviesData(String sortBy, boolean doForceLoad) {
        return moviesRepo.getMoviesData(sortBy, doForceLoad);
    }

    LiveData<Boolean> getGenres() {
        return moviesRepo.getGenres();
    }*/

    LiveData<Resource<List<MovieEntity>>> loadMovies(boolean forceLoad, String sortBy){
        return moviesRepo.loadMovies(forceLoad, sortBy);
    }
}
