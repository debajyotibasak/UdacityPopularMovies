package com.debajyotibasak.udacitypopularmovies.view.ui.detail.trailers;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.VideoEntity;
import com.debajyotibasak.udacitypopularmovies.view.adapter.TrailersAdapter;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.DetailViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TRAILERS_PARCELABLE;

public class TrailerActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DetailViewModel detailViewModel;

    private TrailersAdapter adapter;

    @BindView(R.id.rv_trailers) RecyclerView mRvTrailers;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.txv_toolbar_title) TextView txvToolbar;

    private void initViews() {
        setContentView(R.layout.activity_trailer);
        ButterKnife.bind(this);
    }

    private void initData() {
        this.configureDagger();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.txt_empty_string);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        txvToolbar.setText(getResources().getString(R.string.txt_trailers));
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
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        @SuppressWarnings("unchecked")
        ArrayList<VideoEntity> trailers = (ArrayList<VideoEntity>) getIntent().getSerializableExtra(TRAILERS_PARCELABLE);
        List<VideoEntity> trailerList = new ArrayList<>(trailers);

        populateUI(trailerList);
    }

    private void populateUI(List<VideoEntity> trailerList) {
        adapter.addTrailers(trailerList);
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }
}
