package com.debajyotibasak.udacitypopularmovies.interfaces;

import android.widget.ImageView;

public interface MovieItemClickListener {
    void onMovieItemClick(int position, int movieId, ImageView shareImageView, String transitionName, String activityType);
}
