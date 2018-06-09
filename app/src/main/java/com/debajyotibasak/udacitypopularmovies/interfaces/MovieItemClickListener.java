package com.debajyotibasak.udacitypopularmovies.interfaces;

import android.widget.ImageView;

import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;

public interface MovieItemClickListener {
    void onMovieItemClick(int position, MovieEntity movieEntity, ImageView shareImageView, String transitionName);
}
