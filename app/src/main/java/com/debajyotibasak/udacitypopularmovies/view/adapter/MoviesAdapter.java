package com.debajyotibasak.udacitypopularmovies.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.debajyotibasak.udacitypopularmovies.interfaces.MovieItemClickListener;
import com.debajyotibasak.udacitypopularmovies.utils.AppConstants;
import com.debajyotibasak.udacitypopularmovies.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<MovieEntity> movieList;
    private Context context;
    private final MovieItemClickListener movieItemClickListener;

    public MoviesAdapter(Context context) {
        this.context = context;
        try {
            this.movieItemClickListener = ((MovieItemClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement the interfaces");
        }
    }

    public void addMoviesList(List<MovieEntity> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        } else {
            return movieList.size();
        }
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

            Bitmap placeholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.movie_placeholder);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), placeholder);
            roundedBitmapDrawable.setCornerRadius(25F);

            if (data.getPosterPath() != null) {
                Glide.with(context)
                        .load(AppConstants.POSTER_BASE_PATH + data.getPosterPath())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .placeholder(roundedBitmapDrawable)
                                .error(roundedBitmapDrawable))
                        .apply(RequestOptions
                                .bitmapTransform(new RoundedCornersTransformation(25, 0)))
                        .into(mImvMovieImage);
            }

            if (data.getTitle() != null) {
                mTxvMovieTitle.setText(data.getTitle());
            }

            if (data.getReleaseDate() != null) {
                mTxvReleaseDate.setText(AppUtils.convertDate(data.getReleaseDate(), AppConstants.DF1, AppConstants.DF3));
            }

            if (data.getVoteAverage() != null) {
                mTxvRatings.setText(String.valueOf(data.getVoteAverage()));
            }
        }

        @OnClick
        void onClick(View view) {
            String transitionName = "movie_item_" + String.valueOf(getAdapterPosition());
            movieItemClickListener.onMovieItemClick(
                    getAdapterPosition(),
                    movieList.get(getAdapterPosition()).getMovieId(),
                    mImvMovieImage,
                    transitionName,
                    AppConstants.ACTIVITY_NORMAL);
        }
    }
}
