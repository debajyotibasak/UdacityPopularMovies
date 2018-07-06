package com.debajyotibasak.udacitypopularmovies.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.debajyotibasak.udacitypopularmovies.R;
import com.debajyotibasak.udacitypopularmovies.database.entity.FavMovieCastEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.POSTER_BASE_PATH;

public class FavCastAdapter extends RecyclerView.Adapter<FavCastAdapter.MyViewHolder> {

    private List<FavMovieCastEntity> favMovieCastList;
    private Context context;

    public FavCastAdapter(Context context) {
        this.context = context;
    }

    public void addCasts(List<FavMovieCastEntity> castList) {
        this.favMovieCastList = castList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cast, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (favMovieCastList != null) {
            return favMovieCastList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imv_cast) ImageView imvCast;
        @BindView(R.id.txv_cast_name) TextView txvCast;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(final int position) {

            final FavMovieCastEntity data = favMovieCastList.get(position);

            MultiTransformation<Bitmap> multi = new MultiTransformation<>(
                    new CenterCrop(),
                    new RoundedCornersTransformation(36, 0, RoundedCornersTransformation.CornerType.ALL));

            if (data.getProfilePath() != null) {
                Glide.with(context)
                        .load(POSTER_BASE_PATH + data.getProfilePath())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .placeholder(R.drawable.movie_detail_placeholder)
                                .error(R.drawable.movie_detail_placeholder))
                        .apply(RequestOptions
                                .bitmapTransform(multi))
                        .into(imvCast);
            }

            if (data.getName() != null) {
                txvCast.setText(data.getName());
            }
        }
    }
}
