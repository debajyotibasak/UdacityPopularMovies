package com.debajyotibasak.udacitypopularmovies.view.ui.home;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityOptionsCompat;
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
import com.debajyotibasak.udacitypopularmovies.interfaces.MovieItemClickListener;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;
import com.debajyotibasak.udacitypopularmovies.utils.GridSpacingItemDecoration;
import com.debajyotibasak.udacitypopularmovies.utils.SharedPreferenceHelper;
import com.debajyotibasak.udacitypopularmovies.view.adapter.MoviesAdapter;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.DetailActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIE_IMAGE_TRANSITION;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIE_PARCELABLE;

public class HomeActivity extends AppCompatActivity implements MovieItemClickListener {

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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(), true));
        } else {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 4);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(), true));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        if (SharedPreferenceHelper.contains(AppConstants.PREF_FILTER)) {
            if (SharedPreferenceHelper.getSharedPreferenceString(AppConstants.PREF_FILTER, null).equals(AppConstants.SORT_BY_TOP_RATED)) {
                loadMovies(AppConstants.SORT_BY_TOP_RATED, 2);
            } else if (SharedPreferenceHelper.getSharedPreferenceString(AppConstants.PREF_FILTER, null)
                    .equals(AppConstants.SORT_BY_POPULAR)) {
                loadMovies(AppConstants.SORT_BY_POPULAR, 2);
            }
        } else {
            SharedPreferenceHelper.setSharedPreferenceString(AppConstants.PREF_FILTER, AppConstants.SORT_BY_POPULAR);
            loadMovies(AppConstants.SORT_BY_POPULAR, 1);
        }
    }

    private void loadMovies(String sort, int loadingIdentifer) {

        boolean forceLoad = loadingIdentifer == 1;

        homeViewModel.loadMovies(forceLoad, sort).observe(this, movieResource -> {
            if (movieResource != null) {
                switch (movieResource.getStatus()) {
                    case SUCCESS:
                        hideProgress();
                        if (movieResource.getResponse() != null) {
                            mAdapter.addMoviesList(movieResource.getResponse());
                        }
                        break;
                    case LOADING:
                        showProgress();
                        break;
                    case ERROR:
                        hideProgress();

                        //TODO: If No network show diff error
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
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

        if (SharedPreferenceHelper.contains(AppConstants.PREF_FILTER)) {
            if (SharedPreferenceHelper.getSharedPreferenceString(AppConstants.PREF_FILTER, null).equals(AppConstants.SORT_BY_TOP_RATED)) {
                imvTopRated.setVisibility(View.VISIBLE);
                imvPopular.setVisibility(View.GONE);
            } else if (SharedPreferenceHelper.getSharedPreferenceString(AppConstants.PREF_FILTER, null).equals(AppConstants.SORT_BY_POPULAR)) {
                imvTopRated.setVisibility(View.GONE);
                imvPopular.setVisibility(View.VISIBLE);
            }
        } else {
            imvPopular.setVisibility(View.VISIBLE);
            imvTopRated.setVisibility(View.GONE);
        }

        txvPopular.setOnClickListener(v -> {
            SharedPreferenceHelper.setSharedPreferenceString(AppConstants.PREF_FILTER, AppConstants.SORT_BY_POPULAR);
            imvPopular.setVisibility(View.VISIBLE);
            imvTopRated.setVisibility(View.GONE);
            loadMovies(AppConstants.SORT_BY_POPULAR, 1);
            dialog.dismiss();
        });

        txvTopRated.setOnClickListener(v -> {
            SharedPreferenceHelper.setSharedPreferenceString(AppConstants.PREF_FILTER, AppConstants.SORT_BY_TOP_RATED);
            imvPopular.setVisibility(View.GONE);
            imvTopRated.setVisibility(View.VISIBLE);
            loadMovies(AppConstants.SORT_BY_TOP_RATED, 1);
            dialog.dismiss();
        });

        close.setOnClickListener(view1 -> dialog.dismiss());

        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onMovieItemClick(int position, MovieEntity movieEntity, ImageView shareImageView, String transitionName) {
        Intent intent = new Intent(this, DetailActivity.class);
        Gson gson = new Gson();
        Type type = new TypeToken<MovieEntity>() {
        }.getType();
        String movieDetails = gson.toJson(movieEntity, type);
        intent.putExtra(MOVIE_PARCELABLE, movieDetails);
        intent.putExtra(MOVIE_IMAGE_TRANSITION, transitionName);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                shareImageView,
                transitionName);

        startActivity(intent, options.toBundle());
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics()));
    }
}
