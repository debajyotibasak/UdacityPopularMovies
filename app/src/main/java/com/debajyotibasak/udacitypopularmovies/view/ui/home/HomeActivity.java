package com.debajyotibasak.udacitypopularmovies.view.ui.home;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.interfaces.MovieItemClickListener;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;
import com.debajyotibasak.udacitypopularmovies.utils.AppUtils;
import com.debajyotibasak.udacitypopularmovies.utils.GridSpacingItemDecoration;
import com.debajyotibasak.udacitypopularmovies.utils.SharedPreferenceHelper;
import com.debajyotibasak.udacitypopularmovies.view.adapter.FavMoviesAdapter;
import com.debajyotibasak.udacitypopularmovies.view.adapter.MoviesAdapter;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.DetailActivity;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.ACTIVITY_TYPE;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIE_ID_INTENT;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIE_IMAGE_TRANSITION;

public class HomeActivity extends AppCompatActivity implements MovieItemClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private HomeViewModel homeViewModel;

    @BindView(R.id.rv_movies)
    RecyclerView rvMovies;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txv_toolbar_title)
    TextView txvToolbar;

    @BindView(android.R.id.content)
    View snackBarView;

    @BindView(R.id.no_internet_layout)
    View noInternet;

    @BindView(R.id.btn_refresh)
    Button btnRefresh;

    @BindView(R.id.rv_favorites)
    RecyclerView rvFavMovies;

    private MoviesAdapter mAdapter;
    private FavMoviesAdapter mFavAdapter;

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
        mFavAdapter = new FavMoviesAdapter(this);
        int recyclerViewSpanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4;
        RecyclerView.LayoutManager mMoviesLayoutManager = new GridLayoutManager(this, recyclerViewSpanCount);
        RecyclerView.LayoutManager mFavMoviesLayoutManager = new GridLayoutManager(this, recyclerViewSpanCount);
        rvMovies.setLayoutManager(mMoviesLayoutManager);
        rvMovies.addItemDecoration(new GridSpacingItemDecoration(recyclerViewSpanCount, dpToPx(), true));
        rvMovies.setItemAnimator(new DefaultItemAnimator());
        rvMovies.setAdapter(mAdapter);
        rvFavMovies.setLayoutManager(mFavMoviesLayoutManager);
        rvFavMovies.addItemDecoration(new GridSpacingItemDecoration(recyclerViewSpanCount, dpToPx(), true));
        rvFavMovies.setItemAnimator(new DefaultItemAnimator());
        rvFavMovies.setAdapter(mFavAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();
        loadFromSharedPrefs();
    }

    private void loadFromSharedPrefs() {
        noInternet.setVisibility(View.GONE);

        int loadingIdentifier = SharedPreferenceHelper.contains(AppConstants.PREF_FILTER) ? 2 : 1;

        switch (loadingIdentifier) {
            case 1:
                SharedPreferenceHelper.setSharedPreferenceString(AppConstants.PREF_FILTER, AppConstants.SORT_BY_POPULAR);
                loadMovies(AppConstants.SORT_BY_POPULAR, loadingIdentifier);
                break;
            case 2:
                if (SharedPreferenceHelper.getSharedPreferenceString(AppConstants.PREF_FILTER, null).equals(AppConstants.SORT_BY_TOP_RATED)) {
                    loadMovies(AppConstants.SORT_BY_TOP_RATED, 2);
                } else if (SharedPreferenceHelper.getSharedPreferenceString(AppConstants.PREF_FILTER, null).equals(AppConstants.SORT_BY_POPULAR)) {
                    loadMovies(AppConstants.SORT_BY_POPULAR, 2);
                } else if (SharedPreferenceHelper.getSharedPreferenceString(AppConstants.PREF_FILTER, null).equals(AppConstants.SORT_BY_FAVORITE)) {
                    loadMovies(AppConstants.SORT_BY_FAVORITE, 2);
                }
                break;
            default:
                break;
        }
    }

    private void loadMovies(String sort, int loadingIdentifier) {

        boolean forceLoad = loadingIdentifier == 1;

        if (sort.equalsIgnoreCase(AppConstants.SORT_BY_FAVORITE)) {
            rvFavMovies.setVisibility(View.VISIBLE);
            rvMovies.setVisibility(View.GONE);
            homeViewModel.loadFavMoviesFromDb().observe(this, favMovieList -> {
                if (favMovieList != null) {
                    mFavAdapter.addMoviesList(favMovieList);
                }
            });
        } else {
            rvMovies.setVisibility(View.VISIBLE);
            rvFavMovies.setVisibility(View.GONE);
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
                            checkNetConnectivity();
                            break;
                    }
                }
            });
        }
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        rvMovies.setVisibility(View.VISIBLE);
        rvFavMovies.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        rvMovies.setVisibility(View.GONE);
        rvFavMovies.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
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
        View view = View.inflate(this, R.layout.bottom_sheet_filter_new, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);

        ImageView close = view.findViewById(R.id.imv_close);
        RadioGroup rgFilter = view.findViewById(R.id.rg_filter);
        RadioButton rbPopular = view.findViewById(R.id.rb_popular);
        RadioButton rbTopRated = view.findViewById(R.id.rb_top_rated);
        RadioButton rbFavorite = view.findViewById(R.id.rb_favorite);

        switch (SharedPreferenceHelper.getSharedPreferenceString(AppConstants.PREF_FILTER, null)) {
            case AppConstants.SORT_BY_TOP_RATED:
                rbTopRated.setChecked(true);
                break;
            case AppConstants.SORT_BY_POPULAR:
                rbPopular.setChecked(true);
                break;
            case AppConstants.SORT_BY_FAVORITE:
                rbFavorite.setChecked(true);
                break;
        }

        rgFilter.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            dialog.dismiss();
            switch (checkedId) {
                case R.id.rb_popular:
                    if (AppUtils.isNetworkAvailable()) {
                        SharedPreferenceHelper.setSharedPreferenceString(AppConstants.PREF_FILTER, AppConstants.SORT_BY_POPULAR);
                        loadMovies(AppConstants.SORT_BY_POPULAR, 1);
                    } else {
                        AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
                    }
                    break;
                case R.id.rb_top_rated:
                    if (AppUtils.isNetworkAvailable()) {
                        SharedPreferenceHelper.setSharedPreferenceString(AppConstants.PREF_FILTER, AppConstants.SORT_BY_TOP_RATED);
                        loadMovies(AppConstants.SORT_BY_TOP_RATED, 1);
                    } else {
                        AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
                    }
                    break;
                case R.id.rb_favorite:
                    SharedPreferenceHelper.setSharedPreferenceString(AppConstants.PREF_FILTER, AppConstants.SORT_BY_FAVORITE);
                    loadMovies(AppConstants.SORT_BY_FAVORITE, 1);
                    break;
                default:
                    break;
            }
        });

        close.setOnClickListener(view1 -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onMovieItemClick(int position, int movieId, ImageView shareImageView, String transitionName, String activityType) {
        Intent intent = new Intent(this, DetailActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID_INTENT, movieId);
        bundle.putString(MOVIE_IMAGE_TRANSITION, transitionName);
        bundle.putString(ACTIVITY_TYPE, activityType);
        intent.putExtras(bundle);

        SharedPreferenceHelper.setSharedPreferenceInt("mId", movieId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    shareImageView,
                    transitionName);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics()));
    }

    @OnClick(R.id.btn_refresh)
    public void buttonRefresh() {
        if (AppUtils.isNetworkAvailable()) {
            loadFromSharedPrefs();
        } else {
            checkNetConnectivity();
        }
    }

    private void checkNetConnectivity() {
        if (!AppUtils.isNetworkAvailable()) {
            noInternet.setVisibility(View.VISIBLE);
            AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
        }
    }
}
