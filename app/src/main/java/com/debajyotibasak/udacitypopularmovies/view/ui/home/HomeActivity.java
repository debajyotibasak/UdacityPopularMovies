package com.debajyotibasak.udacitypopularmovies.view.ui.home;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;
import com.debajyotibasak.udacitypopularmovies.utils.AppUtils;
import com.debajyotibasak.udacitypopularmovies.utils.GridSpacingItemDecoration;
import com.debajyotibasak.udacitypopularmovies.utils.SharedPreferenceHelper;
import com.debajyotibasak.udacitypopularmovies.view.adapter.MoviesAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

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
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        getGenres();

        if (SharedPreferenceHelper.contains(this, AppConstants.PREF_FILTER)) {
            if (SharedPreferenceHelper.getSharedPreferenceString(this, AppConstants.PREF_FILTER, null)
                    .equals(AppConstants.SORT_BY_TOP_RATED)) {
                loadMovies(AppConstants.SORT_BY_TOP_RATED);
            } else if (SharedPreferenceHelper.getSharedPreferenceString(this, AppConstants.PREF_FILTER, null)
                    .equals(AppConstants.SORT_BY_POPULAR)) {
                loadMovies(AppConstants.SORT_BY_POPULAR);
            }
        } else {
            loadMovies(AppConstants.SORT_BY_POPULAR);
        }
    }

    private void getGenres() {
        homeViewModel.getGenres().observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (apiResponse.getResponse() != null) {
                    if (apiResponse.getResponse().getGenres() == null || apiResponse.getResponse().getGenres().isEmpty()) {
                        Toast.makeText(this, R.string.txt_some_error_occured, Toast.LENGTH_SHORT).show();
                    }
                } else if(apiResponse.getT() != null) {
                    Toast.makeText(this, R.string.txt_some_error_occured, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                filterMovies();
            }
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void filterMovies() {
        View view = View.inflate(this, R.layout.bottom_sheet_filter, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);

        TextView txvPopular = view.findViewById(R.id.txv_popular);
        TextView txvTopRated = view.findViewById(R.id.txv_top_rated);
        ImageView imvPopular = view.findViewById(R.id.imv_popular);
        ImageView imvTopRated = view.findViewById(R.id.imv_top_rated);
        ImageView close = view.findViewById(R.id.imv_close);

        if (SharedPreferenceHelper.contains(this, AppConstants.PREF_FILTER)) {
            if (SharedPreferenceHelper.getSharedPreferenceString(this, AppConstants.PREF_FILTER, null)
                    .equals(AppConstants.SORT_BY_TOP_RATED)) {
                imvTopRated.setVisibility(View.VISIBLE);
                imvPopular.setVisibility(View.GONE);
            } else if (SharedPreferenceHelper.getSharedPreferenceString(this, AppConstants.PREF_FILTER, null)
                    .equals(AppConstants.SORT_BY_POPULAR)) {
                imvTopRated.setVisibility(View.GONE);
                imvPopular.setVisibility(View.VISIBLE);
            }
        } else {
            imvPopular.setVisibility(View.VISIBLE);
            imvTopRated.setVisibility(View.GONE);
        }

        txvPopular.setOnClickListener(v -> {
            SharedPreferenceHelper.setSharedPreferenceString(this, AppConstants.PREF_FILTER, AppConstants.SORT_BY_POPULAR);
            imvPopular.setVisibility(View.VISIBLE);
            imvTopRated.setVisibility(View.GONE);
            loadMovies(AppConstants.SORT_BY_POPULAR);
            dialog.dismiss();
        });

        txvTopRated.setOnClickListener(v -> {
            SharedPreferenceHelper.setSharedPreferenceString(this, AppConstants.PREF_FILTER, AppConstants.SORT_BY_TOP_RATED);
            imvPopular.setVisibility(View.GONE);
            imvTopRated.setVisibility(View.VISIBLE);
            loadMovies(AppConstants.SORT_BY_TOP_RATED);
            dialog.dismiss();
        });

        close.setOnClickListener(view1 -> dialog.dismiss());

        dialog.setCancelable(true);
        dialog.show();
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics()));
    }
}
