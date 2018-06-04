package com.debajyotibasak.udacitypopularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<MovieEntity> movieList;
    private Context context;

    public MoviesAdapter(Context context, List<MovieEntity> movies) {
        this.context = context;
        this.movieList = movies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false));
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

        @BindView(R.id.txv_movie_name)
        TextView mTxvMovieName;

        @BindView(R.id.txv_movie_rating)
        TextView mTxvMovieRating;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void onBind(final int position) {

            final MovieEntity data = movieList.get(position);

            if (data.getTitle() != null) {
                mTxvMovieName.setText(data.getTitle());
            }

            if (data.getVoteCount() != null) {
                mTxvMovieRating.setText(String.valueOf(data.getVoteCount()));
            }
        }
    }
}
