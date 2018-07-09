package com.debajyotibasak.udacitypopularmovies.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
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
import com.debajyotibasak.udacitypopularmovies.database.entity.VideoEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.YOUTUBE;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.YOUTUBE_THUMBNAIL;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.YOUTUBE_URL;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MyViewHolder> {

    private List<VideoEntity> videoList;
    private Context context;

    public TrailersAdapter(Context context) {
        this.context = context;
    }

    public void addTrailers(List<VideoEntity> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (videoList != null) {
            return videoList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imv_video_thumbnail)
        ImageView imvVideoThumb;
        @BindView(R.id.txv_trailer_title)
        TextView txvTrailerTitle;
        @BindView(R.id.txv_trailer_type)
        TextView txvTrailerType;
        @BindView(R.id.imv_more)
        ImageView imvMore;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(final int position) {

            final VideoEntity data = videoList.get(position);

            if (data.getName() != null) {
                txvTrailerTitle.setText(data.getName());
            }

            if (data.getType() != null) {
                txvTrailerType.setText(data.getType());
            }

            if (data.getKey() != null) {
                Glide.with(context)
                        .load(String.format(YOUTUBE_THUMBNAIL, data.getKey()))
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .placeholder(R.drawable.movie_detail_placeholder)
                                .error(R.drawable.movie_detail_placeholder))
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(25, 0)))
                        .into(imvVideoThumb);
            }

            imvMore.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, imvMore);
                popup.inflate(R.menu.share);
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_share:
                            return true;
                        default:
                            popup.dismiss();
                            return false;
                    }
                });
                popup.show();
            });

            imvVideoThumb.setOnClickListener(v -> {
                if (!videoList.isEmpty() && videoList.get(getAdapterPosition()).getSite().equals(YOUTUBE)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(YOUTUBE_URL + videoList.get(getAdapterPosition()).getKey()));
                    context.startActivity(intent);
                }
            });
        }

    }
}
