package com.debajyotibasak.udacitypopularmovies.view.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.CastEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.FavMovieEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.ReviewEntity;
import com.debajyotibasak.udacitypopularmovies.database.entity.VideoEntity;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;
import com.debajyotibasak.udacitypopularmovies.utils.AppUtils;
import com.debajyotibasak.udacitypopularmovies.view.adapter.CastAdapter;
import com.debajyotibasak.udacitypopularmovies.view.adapter.GenreAdapter;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.reviews.ReviewsActivity;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.trailers.TrailerActivity;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.ACTIVITY_TYPE;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.BACKDROP_BASE_PATH;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIE_ID_INTENT;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIE_IMAGE_TRANSITION;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.POSTER_BASE_PATH;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.REVIEWS_PARCELABLE;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TRAILERS_PARCELABLE;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.YOUTUBE;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.YOUTUBE_THUMBNAIL;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.YOUTUBE_URL;

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

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(android.R.id.content)
    View snackBarView;

    @BindView(R.id.lay_cast)
    View mLayCast;

    @BindView(R.id.lay_trailer)
    View mLayTrailer;

    @BindView(R.id.lay_reviews)
    View mLayReviews;

    @BindView(R.id.progress_detail)
    ProgressBar progressDetails;

    @BindView(R.id.rv_cast)
    RecyclerView mRvCast;

    @BindView(R.id.imv_video_thumb)
    ImageView mImvTrailerThumb;

    @BindView(R.id.txv_trailer_title)
    TextView mTxvVideoTitle;

    @BindView(R.id.txv_review_person)
    TextView mTxvReviewPerson;

    @BindView(R.id.txv_review_body)
    TextView mTxvReviewBody;

    @BindView(R.id.txv_see_all_reviews)
    TextView mTxvSeeAllReviews;

    @BindView(R.id.txv_see_all_trailers)
    TextView mTxvSeeAllTrailers;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.imv_favourite)
    ImageView mImvFavourite;

    @BindView(R.id.progress_rating)
    ProgressBar progressRating;

    private GenreAdapter genreAdapter;
    private CastAdapter castAdapter;
    private RoundedBitmapDrawable roundedBitmapDrawable;
    private String transitionName;
    private String activityType;
    private int movieId;

    private Boolean isMovieFav;
    private FavMovieEntity favMovieEntity, tempFavMovieEntity;

    private void initViews() {
        setContentView(R.layout.activity_detail);
        supportPostponeEnterTransition();
        ButterKnife.bind(this);
    }

    private void initData() {
        this.configureDagger();
        detailViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel.class);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.AppBarCollapsed);
        genreAdapter = new GenreAdapter(this);
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        mRvGenres.setLayoutManager(flowLayoutManager);
        mRvGenres.setAdapter(genreAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvCast.setLayoutManager(llm);
        castAdapter = new CastAdapter(this);
        mRvCast.setAdapter(castAdapter);
        mRvCast.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = dpToPx();
                outRect.right = dpToPx() == state.getItemCount() - 1 ? dpToPx() : 0;
                outRect.top = 0;
                outRect.bottom = 0;
            }
        });
        mRvCast.setNestedScrollingEnabled(true);
    }

    private void setUpToolbarTitle(String movieName) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(movieName);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            transitionName = extras.getString(MOVIE_IMAGE_TRANSITION);
            movieId = extras.getInt(MOVIE_ID_INTENT);
            activityType = extras.getString(ACTIVITY_TYPE);
        }

        loadPlaceholder();
        populateUi();
        supportPostponeEnterTransition();

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mImvFavourite.setOnClickListener(v -> {
            if (isMovieFav) {
                isMovieFav = false;
                deleteFavMovies();
                getData();
            } else {
                isMovieFav = true;
                saveFavMovies(movieId);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("transition", transitionName);
        outState.putInt("movieId", movieId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            transitionName = savedInstanceState.getString("transition");
            movieId = savedInstanceState.getInt("movieId");
        }
    }

    private void populateUi() {
        detailViewModel.getMovieResult().observe(this, movie -> {
            if (movie != null) {
                setUpToolbarTitle(movie.getTitle());
                loadBackDropImage(movie.getBackdropPath());
                loadMainImage(movie.getPosterPath());
                setMovieTitle(movie.getTitle());
                setMovieRating(String.valueOf(movie.getVoteAverage()));
                setMovieReleaseDate(movie.getReleaseDate());
                setMoviePlotDetails(movie.getOverview());
                loadGenres(movie.getGenreIds());
            } else {
                detailViewModel.loadFavMoviesById(movieId).observe(this, favMovies -> {
                    if (favMovies != null) {
                        setUpToolbarTitle(favMovies.getTitle());
                        loadBackDropImage(favMovies.getBackdropPath());
                        loadMainImage(favMovies.getPosterPath());
                        setMovieTitle(favMovies.getTitle());
                        setMovieRating(String.valueOf(favMovies.getVoteAverage()));
                        setMovieReleaseDate(favMovies.getReleaseDate());
                        setMoviePlotDetails(favMovies.getOverview());
                        loadGenres(favMovies.getGenreIds());
                        saveTempFavMovies(favMovies);
                    }
                });
            }
        });

        detailViewModel.loadFavMoviesById(movieId)
                .observe(this, favMovie -> {
                    progressDetails.setVisibility(View.GONE);
                    if (favMovie != null) {
                        isMovieFav = true;
                        mImvFavourite.setImageResource(R.drawable.ic_favorite);
                        getFavMovies(favMovie);
                    } else {
                        isMovieFav = false;
                        mImvFavourite.setImageResource(R.drawable.ic_favorite_border);
                        getData();
                    }
                });
    }

    private void getFavMovies(FavMovieEntity favMovie) {
        detailViewModel.getFavCasts(favMovie.getCastIds()).observe(this, favMovieCasts -> {
            if (favMovieCasts != null && !favMovieCasts.isEmpty()) {
                mLayCast.setVisibility(View.VISIBLE);
                castAdapter.addCasts(favMovieCasts);
            }
        });

        detailViewModel.getFavReviews(favMovie.getMovieId()).observe(this, favMovieReviews -> {
            if (favMovieReviews != null && !favMovieReviews.isEmpty()) {
                mLayReviews.setVisibility(View.VISIBLE);
                mTxvReviewPerson.setText(favMovieReviews.get(0).getAuthor());
                mTxvReviewBody.setText(favMovieReviews.get(0).getContent());
                if (favMovieReviews.size() < 2) {
                    mTxvSeeAllReviews.setVisibility(View.GONE);
                }
                mTxvSeeAllReviews.setOnClickListener(v -> {
                    Intent intent = new Intent(this, ReviewsActivity.class);
                    intent.putExtra(REVIEWS_PARCELABLE, new ArrayList<>(favMovieReviews));
                    startActivity(intent);
                });
            }
        });

        detailViewModel.getFavVideos(favMovie.getMovieId()).observe(this, favMoviesVideo -> {
            if (favMoviesVideo != null && !favMoviesVideo.isEmpty()) {
                mLayTrailer.setVisibility(View.VISIBLE);
                mTxvVideoTitle.setText(favMoviesVideo.get(0).getName());
                Glide.with(this)
                        .load(String.format(YOUTUBE_THUMBNAIL, favMoviesVideo.get(0).getKey()))
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .placeholder(R.drawable.movie_detail_placeholder)
                                .error(R.drawable.movie_detail_placeholder))
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0)))
                        .into(mImvTrailerThumb);
                mImvTrailerThumb.setOnClickListener(view -> {
                    if (favMoviesVideo.get(0) != null && favMoviesVideo.get(0).getSite().equals(YOUTUBE)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL + favMoviesVideo.get(0).getKey()));
                        startActivity(intent);
                    }
                });
                if (favMoviesVideo.size() < 2) {
                    mTxvSeeAllTrailers.setVisibility(View.GONE);
                }
                mTxvSeeAllTrailers.setOnClickListener(v -> {
                    Intent intent = new Intent(this, TrailerActivity.class);
                    intent.putExtra(TRAILERS_PARCELABLE, new ArrayList<>(favMoviesVideo));
                    startActivity(intent);
                });
            }
        });
    }

    private void getData() {
        detailViewModel.getCastResults().observe(this, castResults -> {
            if (castResults != null) {
                switch (castResults.getStatus()) {
                    case SUCCESS:
                        if (castResults.getResponse() != null && !castResults.getResponse().isEmpty()) {
                            mLayCast.setVisibility(View.VISIBLE);
                            castAdapter.addCasts(castResults.getResponse());
                        }
                        break;
                    case LOADING:
                        mLayCast.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        mLayCast.setVisibility(View.GONE);
                        progressDetails.setVisibility(View.GONE);
                        AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
                        break;
                }
            }
        });

        detailViewModel.getVideoResults().observe(this, videoResults -> {
            if (videoResults != null) {
                switch (videoResults.getStatus()) {
                    case SUCCESS:
                        if (videoResults.getResponse() != null && !videoResults.getResponse().isEmpty()) {
                            mLayTrailer.setVisibility(View.VISIBLE);
                            mTxvVideoTitle.setText(videoResults.getResponse().get(0).getName());
                            Glide.with(this)
                                    .load(String.format(YOUTUBE_THUMBNAIL, videoResults.getResponse().get(0).getKey()))
                                    .apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .placeholder(R.drawable.movie_detail_placeholder)
                                            .error(R.drawable.movie_detail_placeholder))
                                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0)))
                                    .into(mImvTrailerThumb);
                            mImvTrailerThumb.setOnClickListener(view -> {
                                if (videoResults.getResponse().get(0) != null && videoResults.getResponse().get(0).getSite().equals(YOUTUBE)) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(YOUTUBE_URL + videoResults.getResponse().get(0).getKey()));
                                    startActivity(intent);
                                }
                            });
                            if (videoResults.getResponse().size() < 2) {
                                mTxvSeeAllTrailers.setVisibility(View.GONE);
                            }
                            mTxvSeeAllTrailers.setOnClickListener(v -> {
                                Intent intent = new Intent(this, TrailerActivity.class);
                                intent.putExtra(TRAILERS_PARCELABLE, new ArrayList<>(videoResults.getResponse()));
                                startActivity(intent);
                            });
                        }
                        break;
                    case LOADING:
                        break;
                    case ERROR:
                        progressDetails.setVisibility(View.GONE);
                        AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
                        break;
                }
            }
        });

        detailViewModel.getReviewResult().observe(this, reviewResults -> {
            if (reviewResults != null) {
                switch (reviewResults.getStatus()) {
                    case SUCCESS:
                        if (reviewResults.getResponse() != null && !reviewResults.getResponse().isEmpty()) {
                            mLayReviews.setVisibility(View.VISIBLE);
                            mTxvReviewPerson.setText(reviewResults.getResponse().get(0).getAuthor());
                            mTxvReviewBody.setText(reviewResults.getResponse().get(0).getContent());
                            if (reviewResults.getResponse().size() < 2) {
                                mTxvSeeAllReviews.setVisibility(View.GONE);
                            }
                            mTxvSeeAllReviews.setOnClickListener(v -> {
                                Intent intent = new Intent(this, ReviewsActivity.class);
                                intent.putExtra(REVIEWS_PARCELABLE, new ArrayList<>(reviewResults.getResponse()));
                                startActivity(intent);
                            });
                        }
                        progressDetails.setVisibility(View.GONE);
                        break;
                    case LOADING:
                        break;
                    case ERROR:
                        progressDetails.setVisibility(View.GONE);
                        AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
                        break;
                }
            }
        });
    }

    private void loadGenres(List<Integer> genreIds) {
        detailViewModel.getGenresById(genreIds).observe(this, genreResource -> {
            if (genreResource != null) {
                switch (genreResource.getStatus()) {
                    case SUCCESS:
                        if (genreResource.getResponse() != null && !genreResource.getResponse().isEmpty()) {
                            mRvGenres.setVisibility(View.VISIBLE);
                            genreAdapter.addGenres(genreResource.getResponse());
                        }
                        break;
                    case LOADING:
                        mRvGenres.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        mRvGenres.setVisibility(View.GONE);
                        AppUtils.setSnackBar(snackBarView, getString(R.string.error_no_internet));
                        break;
                }
            }
        });
    }

    private void setMoviePlotDetails(String overview) {
        mTxvPlotDetails.setText(overview);
    }

    private void setMovieReleaseDate(String releaseDate) {
        mTxvReleaseDate.setText(AppUtils.convertDate(releaseDate, AppConstants.DF1, AppConstants.DF2));
    }

    private void setMovieRating(String rating) {
        mTxvRating.setText(rating);
        Double ratingVal = Double.parseDouble(rating) * 10;
        Integer ratingInt = ratingVal.intValue();
        progressRating.setProgress(ratingInt);
    }

    private void setMovieTitle(String title) {
        mTxvMovieTitle.setText(title);
    }

    private void loadMainImage(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImvPoster.setTransitionName(transitionName);
        }

        loadImageFromCache(true, path);

        TransitionSet set = new TransitionSet();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            set.addTransition(new ChangeImageTransform());
        }
        set.addTransition(new ChangeBounds());
        set.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                loadImageFromCache(false, path);
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(set);
        }

        if (activityType.equalsIgnoreCase(AppConstants.ACTIVITY_FAVOURITE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setSharedElementExitTransition(null);
                mImvPoster.setTransitionName(null);
            }
        }
    }


    private void loadBackDropImage(String backdropPath) {
        Glide.with(this)
                .load(BACKDROP_BASE_PATH + backdropPath)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.movie_detail_placeholder)
                        .error(R.drawable.movie_detail_placeholder))
                .into(mImvBackDrop);
    }

    private void loadImageFromCache(boolean retrieveFromCache, String path) {
        Glide.with(this)
                .load(POSTER_BASE_PATH + path)
                .apply(new RequestOptions()
                        .placeholder(roundedBitmapDrawable)
                        .error(roundedBitmapDrawable)
                        .dontAnimate()
                        .dontTransform()
                        .onlyRetrieveFromCache(retrieveFromCache))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0)))
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
    }

    private void loadPlaceholder() {
        Bitmap placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.movie_placeholder);
        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), placeholder);
        roundedBitmapDrawable.setCornerRadius(25F);
    }

    private void saveFavMovies(int movieId) {
        List<Integer> castIds = new ArrayList<>();
        List<CastEntity> favMovieCast = new ArrayList<>();
        List<ReviewEntity> favMovieReviews = new ArrayList<>();
        List<VideoEntity> favMovieTrailers = new ArrayList<>();

        detailViewModel.getCastResults().observe(this, castListResource -> {
            if (castListResource != null) {
                if (castListResource.getResponse() != null && !castListResource.getResponse().isEmpty()) {
                    for (CastEntity item : castListResource.getResponse()) {
                        castIds.add(item.getId());
                        favMovieCast.add(new CastEntity(item.getId(), item.getName(), item.getProfilePath()));
                    }
                }
            }
        });

        detailViewModel.getReviewResult().observe(this, reviewListResource -> {
            if (reviewListResource != null) {
                if (reviewListResource.getResponse() != null) {
                    for (ReviewEntity item : reviewListResource.getResponse()) {
                        favMovieReviews.add(new ReviewEntity(
                                item.getAuthor(),
                                item.getContent(),
                                item.getId(),
                                item.getUrl(),
                                movieId));
                    }
                }
            }
        });

        detailViewModel.getVideoResults().observe(this, videoListResource -> {
            if (videoListResource != null) {
                if (videoListResource.getResponse() != null) {
                    for (VideoEntity results : videoListResource.getResponse()) {
                        favMovieTrailers.add(new VideoEntity(
                                results.getId(),
                                results.getKey(),
                                results.getName(),
                                results.getSite(),
                                results.getType(),
                                movieId));
                    }
                }
            }
        });

        detailViewModel.getMovieResult().observe(this, movie -> {
            if (movie != null) {
                favMovieEntity = new FavMovieEntity(
                        movie.getMovieId(),
                        movie.getVoteCount(),
                        movie.getVoteAverage(),
                        movie.getTitle(),
                        movie.getPosterPath(),
                        movie.getGenreIds(),
                        movie.getBackdropPath(),
                        movie.getOverview(),
                        movie.getReleaseDate(),
                        castIds,
                        System.currentTimeMillis());
            } else {
                detailViewModel.loadFavMoviesById(movieId).observe(this, favMovie -> {
                    if (favMovie != null) {
                        favMovieEntity = new FavMovieEntity(
                                favMovie.getMovieId(),
                                favMovie.getVoteCount(),
                                favMovie.getVoteAverage(),
                                favMovie.getTitle(),
                                favMovie.getPosterPath(),
                                favMovie.getGenreIds(),
                                favMovie.getBackdropPath(),
                                favMovie.getOverview(),
                                favMovie.getReleaseDate(),
                                castIds,
                                System.currentTimeMillis());
                    }
                });
            }
        });

        if (favMovieEntity != null) {
            detailViewModel.saveFavMovies(favMovieEntity);
        } else {
            detailViewModel.saveFavMovies(tempFavMovieEntity);
        }

        if (!castIds.isEmpty() && !favMovieCast.isEmpty()) {
            detailViewModel.saveFavCast(favMovieCast);
        }

        if (!favMovieReviews.isEmpty()) {
            detailViewModel.saveFavReviews(favMovieReviews);
        }

        if (!favMovieTrailers.isEmpty()) {
            detailViewModel.saveFavTrailers(favMovieTrailers);
        }

        AppUtils.setSnackBar(snackBarView, "Added to favorites");
    }

    public void deleteFavMovies() {
        detailViewModel.deleteMovieById(movieId).observe(this, integer -> {
            AppUtils.setSnackBar(snackBarView, "Removed from favorites");
        });
    }

    public void saveTempFavMovies(FavMovieEntity favMovieEntity) {
        tempFavMovieEntity = favMovieEntity;
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (progressDetails.getVisibility() == View.VISIBLE) {
            progressDetails.setVisibility(View.GONE);
        }
    }
}
