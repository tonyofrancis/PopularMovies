package com.tonyostudio.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
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
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder> {

    private List<Movie> mDataSet;
    private Context mContext;

    public MovieListAdapter(Context context) {
        mContext = context;
        mDataSet = new ArrayList<>();
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_poster_item,parent,false);

        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {

        holder.bindMovieItem(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /*Method used to swap the DataSet*/
    public void swapDataSet(@Nullable List<Movie> dataSet) {

        if (dataSet == null) {
            mDataSet = new ArrayList<>();
        } else {
            mDataSet = dataSet;
        }

        notifyDataSetChanged();
    }

    /*ViewHolder class used to populate RecyclerView*/
    public class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;
        private int mMovieId;

        public MovieItemViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.movie_poster_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindMovieItem(Movie movie) {

            mMovieId = movie.getId();

            /*Fetch Movie Poster based on device resolution*/
            boolean highRes;

            int imageWidth = mContext.getResources().getInteger(R.integer.default_image_width);

            if (imageWidth == MovieService.POSTER_IMAGE_RES_SIZE_780) {
                highRes = true;
            }else {
                highRes = false;
            }

            Picasso.with(mContext)
                    .load(MovieService.getImageUrlString(movie.getPoster_path(),highRes))
                    .placeholder(R.drawable.poster_placeholder)
                    .into(mImageView);

        }

        @Override
        public void onClick(View v) {
            ((MovieListBaseFragment.Callback)mContext).onMovieItemSelected(mMovieId);
        }
    }

}
