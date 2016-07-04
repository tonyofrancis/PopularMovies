package com.tonyostudio.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.api.MovieService;
import com.tonyostudio.popularmovies.fragment.MovieListBaseFragment;
import com.tonyostudio.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyofrancis on 3/22/16.
 *
 * MovieListAdapter class used to populate the RecyclerView host by a Class that
 * subclasses the MovieListBaseFragment class
 *
 * The @param Callback variable has to also be an instance of Context. It just makes sense
 */
public final class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder> {

    private List<Movie> mDataSet;
    private final MovieListBaseFragment.Callback mCallback;

    public MovieListAdapter(MovieListBaseFragment.Callback callback) {
        mDataSet = new ArrayList<>();
        mCallback = callback;
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        holder.bindMovieItem(mDataSet.get(position),mCallback);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /*Method used to swap the DataSet*/
    public void swapDataSet(List<Movie> dataSet) {

        if (dataSet == null) {
            mDataSet = new ArrayList<>();
        } else {
            mDataSet = dataSet;
        }

        notifyDataSetChanged();
    }

    public void mergeDataSet(List<Movie> dataSet) {

        if (dataSet == null) {
            return;
        }

        final int endPoint = mDataSet.size();

        mDataSet.addAll(dataSet);

        notifyItemRangeChanged(endPoint,mDataSet.size());
    }

    public List<Movie> getDataSet() {
        return mDataSet;
    }

    /*ViewHolder class used to populate RecyclerView*/
    public static class MovieItemViewHolder extends RecyclerView.ViewHolder {

        final private ImageView mImageView;

        public MovieItemViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.movie_poster_image_view);
        }

        public void bindMovieItem(final Movie movie,final MovieListBaseFragment.Callback callback) {


            Picasso.with(mImageView.getContext())
                    .load(MovieService.getImageUrlString(movie.getPoster_path()))
                    .placeholder(R.drawable.poster_placeholder)
                    .into(mImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 callback.onMovieItemSelected(movie);
                }
            });
        }
    }
}
