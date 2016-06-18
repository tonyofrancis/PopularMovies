package com.tonyostudio.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.Utils.SpacesItemDecoration;
import com.tonyostudio.popularmovies.adapter.TrailerListAdapter;
import com.tonyostudio.popularmovies.api.AppCache;
import com.tonyostudio.popularmovies.api.MovieService;
import com.tonyostudio.popularmovies.database.FavoriteMovieDatabase;
import com.tonyostudio.popularmovies.model.Movie;
import com.tonyostudio.popularmovies.model.TrailerResults;

/**
 * Created by tonyofrancis on 3/23/16.
 *
 * MovieDetailFragment contains the views and functionality
 * used to display the details of the selected movie.
 *
 */
public final class MovieDetailFragment extends Fragment implements
        MovieService.Callback.TrailerListCallback,
        FavoriteMovieDatabase.OnMovieIsFavoriteCallback {

    private static final String FRAGMENT_ID = "movieDetailFragment";
    private static final String REVIEW_TAG = "com.tonyostudio.popularmovies.review_tag";

    /*Holds the current movie object being displayed in the detail activity.*/
    private Movie mMovie;
    private TrailerListAdapter mTrailerListAdapter;
    ReviewListFragment mReviewListFragment;
    private CheckBox mFavoriteCheckbox;

    /*Get an instance of MovieDetailFragment with a proper
    * bundle extra passed. The EXTRA holds the id of specific
    * movie that will be displayed in the detail activity*/
    public static MovieDetailFragment newInstance(@NonNull Movie movie) {

        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setMovie(movie);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_movie_detail,container,false);

        /*Get a reference to the important views in the layout*/
        final TextView mTitleTextView = (TextView) view.findViewById(R.id.movie_title);
        final ImageView mPosterImageView = (ImageView) view.findViewById(R.id.movie_poster_image_view);
        final TextView mReleaseYearTextView = (TextView) view.findViewById(R.id.movie_year);
        final TextView mRatingTextView = (TextView) view.findViewById(R.id.movie_rating);
        final TextView mDescriptionTextView = (TextView) view.findViewById(R.id.movie_description);
        final TextView mReviewTextView = (TextView) view.findViewById(R.id.show_reviews_text_view);
        final RecyclerView trailerRecyclerView = (RecyclerView) view.findViewById(R.id.trailer_recycler_view);
        mFavoriteCheckbox = (CheckBox) view.findViewById(R.id.movie_favorite_button);

        /*Configure view for the selected movie*/

        if(savedInstanceState != null) {
            mMovie = (Movie) AppCache.retrieve(FRAGMENT_ID);
        }


        mFavoriteCheckbox.setEnabled(false);
        FavoriteMovieDatabase.isMovieAFavorite(mMovie.getId(),this);

        mTitleTextView.setText(mMovie.getTitle());

        if(getActivity() instanceof Callback) {
            ((Callback) getActivity()).setTitle(mMovie.getTitle());
        }

        mReleaseYearTextView.setText(mMovie.getRelease_date().substring(0,4));

        String ratingString = getString(R.string.rating_string,mMovie.getVote_average());
        mRatingTextView.setText(ratingString);

        mDescriptionTextView.setText(mMovie.getOverview());

        Picasso.with(getActivity())
                .load(MovieService.getImageUrlString(mMovie.getPoster_path()))
                .placeholder(R.drawable.poster_placeholder)
                .into(mPosterImageView);


        mReviewListFragment = (ReviewListFragment) getFragmentManager().findFragmentByTag(REVIEW_TAG);

        if(mReviewListFragment == null) {
            mReviewListFragment = ReviewListFragment.newInstance(mMovie);
        }

        /*Show Review*/
        mReviewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReviewListFragment.show(getFragmentManager(),REVIEW_TAG);
            }
        });


        /*Setup Trailer RecyclerView*/
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        trailerRecyclerView.addItemDecoration(new SpacesItemDecoration(0,0,16,0));

        mTrailerListAdapter = new TrailerListAdapter(getActivity());
        trailerRecyclerView.setAdapter(mTrailerListAdapter);

        MovieService.fetchTrailersListAsync(String.valueOf(mMovie.getId()),this);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        AppCache.store(FRAGMENT_ID,mMovie);
    }

    private void setMovie(Movie movie) {
        mMovie = movie;
    }

    public interface Callback {
        void setTitle(String title);
    }

    @Override
    public void onMovieIsFavorite(boolean isFavorite) {
        mFavoriteCheckbox.setEnabled(true);
        mFavoriteCheckbox.setChecked(isFavorite);

        mFavoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                if (isChecked) {
                    FavoriteMovieDatabase.put(mMovie);
                } else {
                    FavoriteMovieDatabase.remove(mMovie.getId());
                }
            }
        });
    }

    @Override
    public void onTrailerDataLoaded(TrailerResults trailerResults) {

        final View view = getView().findViewById(R.id.trailer_wrapper_view);

        //If no data is returned hide the trailer section
        if(trailerResults == null || trailerResults.getResults().size() == 0) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            mTrailerListAdapter.swapData(trailerResults.getResults());
        }
    }
}
