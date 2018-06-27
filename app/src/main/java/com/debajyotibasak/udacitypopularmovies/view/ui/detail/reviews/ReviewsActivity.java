package com.debajyotibasak.udacitypopularmovies.view.ui.detail.reviews;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.api.model.ReviewResult;
import com.debajyotibasak.udacitypopularmovies.view.adapter.ReviewsAdapter;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.DetailViewModel;
import com.debajyotibasak.udacitypopularmovies.view.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.REVIEWS_PARCELABLE;

public class ReviewsActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DetailViewModel detailViewModel;

    private ReviewsAdapter adapter;

    @BindView(R.id.rv_reviews)
    RecyclerView mRvReviews;

    private void initViews() {
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
    }

    private void initData() {
        this.configureDagger();
        detailViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel.class);
        adapter = new ReviewsAdapter(this);
        mRvReviews.setLayoutManager(new LinearLayoutManager(this));
        mRvReviews.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        @SuppressWarnings("unchecked")
        ArrayList<ReviewResult> reviews = (ArrayList<ReviewResult>) getIntent().getSerializableExtra(REVIEWS_PARCELABLE);
        List<ReviewResult> reviewList = new ArrayList<>(reviews);

        populateUI(reviewList);
    }

    private void populateUI(List<ReviewResult> reviewList) {
        adapter.addReviews(reviewList);
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

}
