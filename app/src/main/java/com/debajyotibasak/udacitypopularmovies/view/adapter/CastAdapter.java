package com.debajyotibasak.udacitypopularmovies.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.debajyotibasak.udacitypopularmovies.api.model.CastResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.POSTER_BASE_PATH;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {

    private List<CastResult> castList;
    private Context context;

    public CastAdapter(Context context) {
        this.context = context;
    }

    public void addCasts(List<CastResult> castList) {
        this.castList = castList;
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
        if (castList != null) {
            return castList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imv_cast) ImageView imvCast;
        @BindView(R.id.txv_cast_name) TextView txvCast;
        @BindView(R.id.txv_character_name) TextView txvCharacterName;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(final int position) {

            final CastResult data = castList.get(position);

            if (data.getProfilePath() != null) {
                Glide.with(context)
                        .load(POSTER_BASE_PATH + data.getProfilePath())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .placeholder(R.drawable.movie_detail_placeholder)
                                .error(R.drawable.movie_detail_placeholder))
                        .into(imvCast);
            }

            if (data.getName() != null) {
                txvCast.setText(data.getName());
            }

            if (data.getCharacter() != null) {
                txvCharacterName.setText(data.getCharacter());
            }
        }
    }
}
