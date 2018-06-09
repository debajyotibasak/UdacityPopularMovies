package com.debajyotibasak.udacitypopularmovies.view.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;
import com.debajyotibasak.udacitypopularmovies.utils.AppUtils;
import com.debajyotibasak.udacitypopularmovies.view.adapter.GenreAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.lang.reflect.Type;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.BACKDROP_BASE_PATH;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIE_IMAGE_TRANSITION;
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
        supportPostponeEnterTransition();
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

        Bundle extras = getIntent().getExtras();

        String movieItemJson = extras.getString(MOVIE_PARCELABLE);
        Gson gson = new Gson();
        Type type = new TypeToken<MovieEntity>() {
        }.getType();
        MovieEntity movieEntity = gson.fromJson(movieItemJson, type);

        Glide.with(this)
                .load(BACKDROP_BASE_PATH + movieEntity.getBackdropPath())
                .apply(new RequestOptions().placeholder(android.R.color.holo_blue_bright).error(android.R.color.holo_blue_bright))
                .into(mImvBackDrop);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(MOVIE_IMAGE_TRANSITION);
            mImvPoster.setTransitionName(imageTransitionName);
        }

        Glide.with(this)
                .load(POSTER_BASE_PATH + movieEntity.getPosterPath())
                .apply(new RequestOptions()
                        .placeholder(android.R.color.holo_blue_bright)
                        .error(android.R.color.holo_blue_bright)
                        .dontAnimate())
                .apply(RequestOptions
                        .bitmapTransform(new RoundedCornersTransformation(25, 0)))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(mImvPoster);

        mTxvMovieTitle.setText(movieEntity.getTitle());
        mTxvRating.setText(String.valueOf(movieEntity.getVoteAverage()));
        mTxvReleaseDate.setText(AppUtils.convertDate(movieEntity.getReleaseDate(), AppConstants.DF1, AppConstants.DF2));
        mTxvPlotDetails.setText(movieEntity.getOverview());

        detailViewModel.getGenresById(movieEntity.getGenreIds()).observe(this, genreEntities -> {
            if (genreEntities != null) {
                genreAdapter.addGenres(genreEntities);
            }
        });
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }
}
