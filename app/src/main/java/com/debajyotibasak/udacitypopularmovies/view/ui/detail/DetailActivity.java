package com.debajyotibasak.udacitypopularmovies.view.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;
import com.debajyotibasak.udacitypopularmovies.utils.AppUtils;
import com.debajyotibasak.udacitypopularmovies.view.adapter.GenreAdapter;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.BACKDROP_BASE_PATH;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIE_PARCELABLE;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.POSTER_BASE_PATH;

public class DetailActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DetailViewModel detailViewModel;

    @BindView(R.id.imv_back_drop)
    ImageView mImvBackDrop;

    @BindView(R.id.imv_poster)
    ImageView mImvPoster;

    @BindView(R.id.txv_title)
    TextView mTxvMovieTitle;

    @BindView(R.id.txv_ratings)
    TextView mTxvRating;

    @BindView(R.id.rv_genres)
    RecyclerView mRvGenres;

    @BindView(R.id.txv_release_date)
    TextView mTxvReleaseDate;

    @BindView(R.id.txv_plot_details)
    TextView mTxvPlotDetails;

    private GenreAdapter genreAdapter;

    private void initViews() {
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
    }

    private void initData() {
        this.configureDagger();
        detailViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel.class);
        genreAdapter = new GenreAdapter(this);
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        mRvGenres.setLayoutManager(flowLayoutManager);
        mRvGenres.setAdapter(genreAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        MovieEntity parcelableData = getIntent().getParcelableExtra(MOVIE_PARCELABLE);

        Glide.with(this)
                .load(BACKDROP_BASE_PATH + parcelableData.getBackdropPath())
                .apply(new RequestOptions().placeholder(android.R.color.holo_blue_bright).error(android.R.color.holo_blue_bright))
                .into(mImvBackDrop);

        Glide.with(this)
                .load(POSTER_BASE_PATH + parcelableData.getPosterPath())
                .apply(new RequestOptions().placeholder(android.R.color.holo_blue_bright).error(android.R.color.holo_blue_bright))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0)))
                .into(mImvPoster);

        mTxvMovieTitle.setText(parcelableData.getTitle());
        mTxvRating.setText(String.valueOf(parcelableData.getVoteAverage()));
        mTxvReleaseDate.setText(AppUtils.convertDate(parcelableData.getReleaseDate(), AppConstants.DF1, AppConstants.DF2));
        mTxvPlotDetails.setText(parcelableData.getOverview());

        detailViewModel.getGenresById(parcelableData.getGenreIds()).observe(this, genreEntities -> {
            if (genreEntities != null) {
                genreAdapter.addGenres(genreEntities);
            }
        });
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }
}
