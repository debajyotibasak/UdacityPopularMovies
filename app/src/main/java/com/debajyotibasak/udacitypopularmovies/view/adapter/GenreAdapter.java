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
import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
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

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {

    private List<GenreEntity> genreList;
    private Context context;

    public GenreAdapter(Context context) {
        this.context = context;
    }

    public void addGenres(List<GenreEntity> genreList) {
        this.genreList = genreList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chip_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txv_genre)
        TextView mTxvGenre;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(final int position) {

            final GenreEntity data = genreList.get(position);

            if (data.getGenreName() != null) {
                mTxvGenre.setText(data.getGenreName());
            }
        }
    }
}
