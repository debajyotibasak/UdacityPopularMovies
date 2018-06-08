package com.debajyotibasak.udacitypopularmovies.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;
import com.debajyotibasak.udacitypopularmovies.utils.AppUtils;
import com.debajyotibasak.udacitypopularmovies.view.ui.detail.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.MOVIE_PARCELABLE;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<MovieEntity> movieList;
    private Context context;

    public MoviesAdapter(Context context) {
        this.context = context;
    }

    public void addMoviesList(List<MovieEntity> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imv_movie_poster)
        ImageView mImvMovieImage;

        @BindView(R.id.txv_movie_title)
        TextView mTxvMovieTitle;

        @BindView(R.id.txv_release_date)
        TextView mTxvReleaseDate;

        @BindView(R.id.txv_movie_rating)
        TextView mTxvRatings;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(final int position) {

            final MovieEntity data = movieList.get(position);

            if (data.getPosterPath() != null) {
                Glide.with(context)
                        .load(AppConstants.POSTER_BASE_PATH + data.getPosterPath())
                        .apply(new RequestOptions().placeholder(android.R.color.holo_blue_bright).error(android.R.color.holo_blue_bright))
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0)))
                        .into(mImvMovieImage);
            }

            if (data.getTitle() != null) {
                mTxvMovieTitle.setText(data.getTitle());
            }

            if (data.getReleaseDate() != null) {
                mTxvReleaseDate.setText(AppUtils.convertDate(data.getReleaseDate(), AppConstants.DF1, AppConstants.DF2));
            }

            if (data.getVoteAverage() != null) {
                mTxvRatings.setText(String.valueOf(data.getVoteAverage()));
            }
        }

        @OnClick
        void onClick(View view) {
            MovieEntity movieEntity = movieList.get(getAdapterPosition());
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(MOVIE_PARCELABLE, movieEntity);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
