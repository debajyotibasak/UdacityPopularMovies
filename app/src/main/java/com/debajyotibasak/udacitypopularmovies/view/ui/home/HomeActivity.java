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
import android.widget.TextView;

import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.interfaces.MovieItemClickListener;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;
import com.debajyotibasak.udacitypopularmovies.utils.AppUtils;
import com.debajyotibasak.udacitypopularmovies.utils.GridSpacingItemDecoration;
import com.debajyotibasak.udacitypopularmovies.utils.SharedPreferenceHelper;
import com.debajyotibasak.udacitypopularmovies.view.adapter.MoviesAdapter;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.DetailActivity;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(android.R.id.content)
    View snackBarView;

    @BindView(R.id.no_internet_layout)
    View noInternet;

    @BindView(R.id.btn_refresh)
    Button btnRefresh;

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
        int recyclerViewSpanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, recyclerViewSpanCount);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(recyclerViewSpanCount, dpToPx(), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
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
                }
                break;
            default:
                break;
        }
    }

    private void loadMovies(String sort, int loadingIdentifier) {

        boolean forceLoad = loadingIdentifier == 1;

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

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
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
        View view = View.inflate(this, R.layout.bottom_sheet_filter, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);

        TextView txvPopular = view.findViewById(R.id.txv_popular);
        TextView txvTopRated = view.findViewById(R.id.txv_top_rated);
        ImageView imvPopular = view.findViewById(R.id.imv_popular);
        ImageView imvTopRated = view.findViewById(R.id.imv_top_rated);
        ImageView close = view.findViewById(R.id.imv_close);

        int filterIdentifier = SharedPreferenceHelper.contains(AppConstants.PREF_FILTER) ? 2 : 1;

        switch (filterIdentifier) {
            case 1:
                imvPopular.setVisibility(View.VISIBLE);
                imvTopRated.setVisibility(View.GONE);
                break;
            case 2:
                if (SharedPreferenceHelper.getSharedPreferenceString(AppConstants.PREF_FILTER, null).equals(AppConstants.SORT_BY_TOP_RATED)) {
                    imvTopRated.setVisibility(View.VISIBLE);
                    imvPopular.setVisibility(View.GONE);
                } else if (SharedPreferenceHelper.getSharedPreferenceString(AppConstants.PREF_FILTER, null).equals(AppConstants.SORT_BY_POPULAR)) {
                    imvTopRated.setVisibility(View.GONE);
                    imvPopular.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }

        txvPopular.setOnClickListener(v -> {
            dialog.dismiss();
            if (AppUtils.isNetworkAvailable()) {
                SharedPreferenceHelper.setSharedPreferenceString(AppConstants.PREF_FILTER, AppConstants.SORT_BY_POPULAR);
                imvPopular.setVisibility(View.VISIBLE);
                imvTopRated.setVisibility(View.GONE);
                loadMovies(AppConstants.SORT_BY_POPULAR, 1);
            } else {
                AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
            }
        });

        txvTopRated.setOnClickListener(v -> {
            dialog.dismiss();
            if (AppUtils.isNetworkAvailable()) {
                SharedPreferenceHelper.setSharedPreferenceString(AppConstants.PREF_FILTER, AppConstants.SORT_BY_TOP_RATED);
                imvPopular.setVisibility(View.GONE);
                imvTopRated.setVisibility(View.VISIBLE);
                loadMovies(AppConstants.SORT_BY_TOP_RATED, 1);
            } else {
                AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
            }
        });

        close.setOnClickListener(view1 -> dialog.dismiss());

        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onMovieItemClick(int position, MovieEntity movieEntity, ImageView shareImageView, String transitionName) {
        Intent intent = new Intent(this, DetailActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(MOVIE_PARCELABLE, movieEntity);
        bundle.putString(MOVIE_IMAGE_TRANSITION, transitionName);
        intent.putExtras(bundle);

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

    public void checkNetConnectivity() {
        if (!AppUtils.isNetworkAvailable()) {
            noInternet.setVisibility(View.VISIBLE);
            AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
        }
    }
}
