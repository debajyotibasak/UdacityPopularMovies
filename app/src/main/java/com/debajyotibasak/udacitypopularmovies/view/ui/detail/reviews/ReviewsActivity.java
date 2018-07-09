package com.debajyotibasak.udacitypopularmovies.view.ui.detail.reviews;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.ReviewEntity;
import com.debajyotibasak.udacitypopularmovies.view.adapter.ReviewsAdapter;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.DetailViewModel;

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

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txv_toolbar_title)
    TextView txvToolbar;

    private void initViews() {
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
    }

    private void initData() {
        this.configureDagger();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.txt_empty_string);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        txvToolbar.setText("Reviews");
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
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        @SuppressWarnings("unchecked")
        ArrayList<ReviewEntity> reviews = (ArrayList<ReviewEntity>) getIntent().getSerializableExtra(REVIEWS_PARCELABLE);
        List<ReviewEntity> reviewList = new ArrayList<>(reviews);

        populateUI(reviewList);
    }

    private void populateUI(List<ReviewEntity> reviewList) {
        adapter.addReviews(reviewList);
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
