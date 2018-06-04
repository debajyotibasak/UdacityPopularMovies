package com.debajyotibasak.udacitypopularmovies.view.ui.home;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.utils.AppUtils;
import com.debajyotibasak.udacitypopularmovies.view.adapter.MoviesAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.SORT_BY_TOP_RATED;

public class HomeActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private HomeViewModel homeViewModel;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private MoviesAdapter mAdapter;

    private void initViews() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void initData() {
        this.configureDagger();
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);
        mAdapter = new MoviesAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        showProgress();

        if (AppUtils.isNetworkAvailable(this)) {
            homeViewModel.getMovies(SORT_BY_TOP_RATED).observe(this, moviesResponseApiResponse -> {
                if (moviesResponseApiResponse != null) {
                    if (moviesResponseApiResponse.getResponse() != null) {
                        if (moviesResponseApiResponse.getResponse().getResults() != null
                                && !moviesResponseApiResponse.getResponse().getResults().isEmpty()) {
                            List<MovieEntity> movieList = moviesResponseApiResponse.getResponse().getResults();
                            mAdapter.addMoviesList(movieList);
                        }
                    } else if (moviesResponseApiResponse.getT() != null) {
                        Toast.makeText(this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }
                hideProgress();
            });
        } else {
            homeViewModel.getMoviesFromDb().observe(this, movieEntities -> {
                if (movieEntities != null) {
                    mAdapter.addMoviesList(movieEntities);
                }
                hideProgress();
            });
        }

    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }
}
