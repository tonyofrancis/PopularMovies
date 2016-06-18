package com.tonyostudio.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyofrancis on 5/19/16.
 */

public final class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> mDataSet;
    private Context mContext;

    public ReviewAdapter(Context context) {
        mContext = context;
        mDataSet = new ArrayList<>();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(mContext).inflate(R.layout.review_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void swapDataSet(List<Review> reviewList) {

        if(reviewList == null) {
            mDataSet = new ArrayList<>();
        } else {
            mDataSet = reviewList;
        }

        notifyDataSetChanged();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        final private TextView mAuthorTextView;
        final private TextView mCommentTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            mAuthorTextView = (TextView) itemView.findViewById(R.id.review_author);
            mCommentTextView = (TextView) itemView.findViewById(R.id.review_comment);
        }

        public void bind(Review review) {

            mAuthorTextView.setText(review.getAuthor());
            mCommentTextView.setText(review.getContent());

        }
    }
}
