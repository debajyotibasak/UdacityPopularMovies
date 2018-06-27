package com.debajyotibasak.udacitypopularmovies.view.ui.detail.trailers;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.api.model.ReviewResult;
import com.debajyotibasak.udacitypopularmovies.api.model.VideoResponse;
import com.debajyotibasak.udacitypopularmovies.api.model.VideoResults;
import com.debajyotibasak.udacitypopularmovies.view.adapter.ReviewsAdapter;
import com.debajyotibasak.udacitypopularmovies.view.adapter.TrailersAdapter;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.DetailViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.REVIEWS_PARCELABLE;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TRAILERS_PARCELABLE;

public class TrailerActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DetailViewModel detailViewModel;

    private TrailersAdapter adapter;

    @BindView(R.id.rv_trailers)
    RecyclerView mRvTrailers;

    private void initViews() {
        setContentView(R.layout.activity_trailer);
        ButterKnife.bind(this);
    }

    private void initData() {
        this.configureDagger();
        detailViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel.class);
        adapter = new TrailersAdapter(this);
        mRvTrailers.setLayoutManager(new LinearLayoutManager(this));
        mRvTrailers.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        @SuppressWarnings("unchecked")
        ArrayList<VideoResults> trailers = (ArrayList<VideoResults>) getIntent().getSerializableExtra(TRAILERS_PARCELABLE);
        List<VideoResults> trailerList = new ArrayList<>(trailers);

        populateUI(trailerList);
    }

    private void populateUI(List<VideoResults> trailerList) {
        adapter.addTrailers(trailerList);
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }
}
