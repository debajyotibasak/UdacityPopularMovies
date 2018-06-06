package com.debajyotibasak.udacitypopularmovies.view.ui.home;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.utils.AppUtils;
import com.debajyotibasak.udacitypopularmovies.utils.GridSpacingItemDecoration;
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

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txv_toolbar_title)
    TextView txvToolbar;

    private MoviesAdapter mAdapter;

    private void initViews() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void initData() {
        this.configureDagger();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        getSupportActionBar().setTitle(R.string.txt_empty_string);
        txvToolbar.setText(R.string.txt_movies);
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);
        mAdapter = new MoviesAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration( 2, dpToPx(20), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        loadMovies(SORT_BY_TOP_RATED);
    }

    private void loadMovies(String sort) {
        showProgress();
        if (AppUtils.isNetworkAvailable(this)) {
            homeViewModel.getMovies(sort).observe(this, moviesResponseApiResponse -> {
                if (moviesResponseApiResponse != null) {
                    if (moviesResponseApiResponse.getResponse() != null) {
                        if (moviesResponseApiResponse.getResponse().getResults() != null
                                && !moviesResponseApiResponse.getResponse().getResults().isEmpty()) {
                            List<MovieEntity> movieList = moviesResponseApiResponse.getResponse().getResults();
                            mAdapter.addMoviesList(movieList);
                        }
                    } else if (moviesResponseApiResponse.getT() != null) {
                        Toast.makeText(this, R.string.txt_some_error_occured, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem sort = menu.findItem(R.id.action_sort);
        sort.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.action_sort) {

            }
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
