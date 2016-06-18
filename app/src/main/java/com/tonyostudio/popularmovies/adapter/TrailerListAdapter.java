package com.tonyostudio.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.api.MovieService;
import com.tonyostudio.popularmovies.model.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyofrancis on 4/26/16.
 */
public final class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder> {

    private Context mContext;
    private List<Trailer> mTrailerList;

    public TrailerListAdapter(Context context) {
        mContext = context;
        mTrailerList = new ArrayList<>();
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrailerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.trailer_item,parent,false));
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bindTrailer(mTrailerList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public void swapData(List<Trailer> trailerList) {

        if (trailerList == null) {
            mTrailerList = new ArrayList<>();
        } else {
            mTrailerList = trailerList;
        }

        notifyDataSetChanged();
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final private TextView mTitleTextView;
        private Trailer mTrailer;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.trailer_title_text_view);
        }

        public void bindTrailer(Trailer trailer) {

            mTitleTextView.setText(trailer.getName());
            mTrailer = trailer;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(MovieService.getTrailerUrlString(mTrailer.getKey())));

            v.getContext().startActivity(intent);
        }
    }
}
