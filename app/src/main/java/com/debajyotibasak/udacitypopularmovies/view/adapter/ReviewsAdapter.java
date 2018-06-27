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
import com.debajyotibasak.udacitypopularmovies.api.model.CastResult;
import com.debajyotibasak.udacitypopularmovies.api.model.ReviewResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.POSTER_BASE_PATH;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private List<ReviewResult> reviewList;
    private Context context;

    public ReviewsAdapter(Context context) {
        this.context = context;
    }

    public void addReviews(List<ReviewResult> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (reviewList != null) {
            return reviewList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txv_review_person) TextView txvReviewPerson;
        @BindView(R.id.txv_review) TextView txvReview;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void onBind(final int position) {

            final ReviewResult data = reviewList.get(position);

            if (data.getAuthor() != null) {
                txvReviewPerson.setText(data.getAuthor());
            }

            if (data.getContent() != null) {
                txvReview.setText(data.getContent());
            }
        }
    }
}
