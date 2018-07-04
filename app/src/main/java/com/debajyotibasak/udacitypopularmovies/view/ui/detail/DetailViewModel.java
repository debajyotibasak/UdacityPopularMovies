package com.debajyotibasak.udacitypopularmovies.view.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.debajyotibasak.udacitypopularmovies.api.model.CastResult;
import com.debajyotibasak.udacitypopularmovies.api.model.ReviewResult;
import com.debajyotibasak.udacitypopularmovies.api.model.VideoResults;
import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
import com.debajyotibasak.udacitypopularmovies.repo.AppRepository;
import com.debajyotibasak.udacitypopularmovies.utils.Resource;
import com.debajyotibasak.udacitypopularmovies.utils.SharedPreferenceHelper;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    private AppRepository moviesRepo;
    private MutableLiveData<Resource<List<CastResult>>> castResults = new MutableLiveData<>();
    private MutableLiveData<Resource<List<VideoResults>>> videoResults = new MutableLiveData<>();
    private MutableLiveData<Resource<List<ReviewResult>>> reviewResult = new MutableLiveData<>();

    @Inject
    public DetailViewModel(AppRepository moviesRepo) {
        this.moviesRepo = moviesRepo;

        moviesRepo.loadCast(SharedPreferenceHelper.getSharedPreferenceInt("mId"))
                .observeForever(listResource -> castResults.setValue(listResource));

        moviesRepo.loadReviews(SharedPreferenceHelper.getSharedPreferenceInt("mId"))
                .observeForever(listResource -> reviewResult.setValue(listResource));

        moviesRepo.loadVideos(SharedPreferenceHelper.getSharedPreferenceInt("mId"))
                .observeForever(listResource -> videoResults.setValue(listResource));

    }

    LiveData<Resource<List<GenreEntity>>> getGenresById(List<Integer> genreIds) {
        return moviesRepo.loadGenres(genreIds);
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

}
