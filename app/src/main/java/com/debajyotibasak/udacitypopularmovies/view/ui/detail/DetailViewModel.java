package com.debajyotibasak.udacitypopularmovies.view.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
import com.debajyotibasak.udacitypopularmovies.repo.AppRepository;
import com.debajyotibasak.udacitypopularmovies.utils.Resource;

import java.util.List;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    private AppRepository moviesRepo;

    @Inject
    public DetailViewModel(AppRepository moviesRepo) {
        this.moviesRepo = moviesRepo;
    }

    LiveData<Resource<List<GenreEntity>>> getGenresById(List<Integer> genreIds) {
        return moviesRepo.loadGenres(genreIds);
    }

}
