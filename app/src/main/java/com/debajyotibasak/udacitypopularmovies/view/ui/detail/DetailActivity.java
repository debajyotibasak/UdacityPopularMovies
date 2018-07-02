package com.debajyotibasak.udacitypopularmovies.view.ui.detail;

import android.annotation.TargetApi;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.api.model.ReviewResult;
import com.debajyotibasak.udacitypopularmovies.api.model.VideoResults;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
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

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.BACKDROP_BASE_PATH;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIES_STATE_DETAILS;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIE_IMAGE_TRANSITION;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIE_PARCELABLE;
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

    private GenreAdapter genreAdapter;
    private CastAdapter castAdapter;
    private MovieEntity movieEntity;
    private RoundedBitmapDrawable roundedBitmapDrawable;
    private String transitionName;
    private int movieId;
    private List<Integer> genreId;


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

        genreAdapter = new GenreAdapter(this);
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        mRvGenres.setLayoutManager(flowLayoutManager);
        mRvGenres.setAdapter(genreAdapter);

        castAdapter = new CastAdapter(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvCast.setLayoutManager(llm);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieEntity = (MovieEntity) extras.getSerializable(MOVIE_PARCELABLE);
            transitionName = extras.getString(MOVIE_IMAGE_TRANSITION);
            movieId = movieEntity.getMovieId();
            genreId = movieEntity.getGenreIds();
        }

        supportPostponeEnterTransition();

        populateUi(transitionName);

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("transition", transitionName);
        outState.putInt("movieId", movieId);
        outState.putIntegerArrayList("genreId", new ArrayList<>(genreId));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            transitionName = savedInstanceState.getString("transition");
            movieId = savedInstanceState.getInt("movieId");
            genreId = savedInstanceState.getIntegerArrayList("genreId");
            Toast.makeText(this, "Restored" + transitionName + " " + movieId + " " + genreId, Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void populateUi(String transitionName) {
        Bitmap placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.movie_placeholder);
        roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), placeholder);
        roundedBitmapDrawable.setCornerRadius(25F);

        Glide.with(this)
                .load(BACKDROP_BASE_PATH + movieEntity.getBackdropPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.movie_detail_placeholder)
                        .error(R.drawable.movie_detail_placeholder))
                .into(mImvBackDrop);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImvPoster.setTransitionName(transitionName);
        }

        loadPosterImage(true);

        TransitionSet set = new TransitionSet();
        set.addTransition(new ChangeImageTransform());
        set.addTransition(new ChangeBounds());
        set.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                loadPosterImage(false);
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

        getWindow().setSharedElementEnterTransition(set);

        mTxvMovieTitle.setText(movieEntity.getTitle());
        mTxvRating.setText(String.valueOf(movieEntity.getVoteAverage()));
        mTxvReleaseDate.setText(AppUtils.convertDate(movieEntity.getReleaseDate(), AppConstants.DF1, AppConstants.DF2));
        mTxvPlotDetails.setText(movieEntity.getOverview());

        getData();
    }

    private void loadPosterImage(boolean retrieveFromCache) {
        Glide.with(this)
                .load(POSTER_BASE_PATH + movieEntity.getPosterPath())
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

    private void getData() {
        detailViewModel.getGenresById(genreId).observe(this, genreResource -> {
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

        detailViewModel.getCastById(movieId).observe(this, castResults -> {
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
                        Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        detailViewModel.getVideosById(movieId).observe(this, videoResults -> {
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
                                ArrayList<VideoResults> mList = new ArrayList<>(videoResults.getResponse());
                                intent.putExtra(TRAILERS_PARCELABLE, mList);
                                startActivity(intent);
                            });
                        }
                        break;
                    case LOADING:
                        break;
                    case ERROR:
                        progressDetails.setVisibility(View.GONE);
                        Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        detailViewModel.getReviewsById(movieId).observe(this, reviewResults -> {
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
                                ArrayList<ReviewResult> mList = new ArrayList<>(reviewResults.getResponse());
                                intent.putExtra(REVIEWS_PARCELABLE, mList);
                                startActivity(intent);
                            });
                        }
                        progressDetails.setVisibility(View.GONE);
                        break;
                    case LOADING:
                        break;
                    case ERROR:
                        progressDetails.setVisibility(View.GONE);
                        Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics()));
    }
}
