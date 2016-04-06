package com.tonyostudio.popularmovies.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.api.MovieService;
import com.tonyostudio.popularmovies.model.Movie;

/**
 * Created by tonyofrancis on 3/23/16.
 *
 * MovieDetailFragment contains the views and functionality
 * used to display the details of the selected movie.
 *
 */
public class MovieDetailFragment extends Fragment {

    private static final String EXTRA_MOVIE_ID = "com.tonyostudio.popularmovies.extra_movie_id";


    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseYearTextView;
    private TextView mRatingTextView;
    private TextView mDescriptionTextView;

    private Callback mCallback;

    /*Holds the current movie object being displayed in the detail activity.*/
    private Movie mMovie;

    /*Get an instance of MovieDetailFragment with a proper
    * bundle extra passed. The EXTRA holds the id of specific
    * movie that will be displayed in the detail activity*/
    public static MovieDetailFragment newInstance(int movieId) {
        
        Bundle args = new Bundle();
        args.putInt(EXTRA_MOVIE_ID,movieId);

        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail,container,false);


        /*Get a reference to the important views in the layout*/
        mTitleTextView = (TextView) view.findViewById(R.id.movie_title);
        mPosterImageView = (ImageView) view.findViewById(R.id.movie_poster_image_view);
        mReleaseYearTextView = (TextView) view.findViewById(R.id.movie_year);
        mRatingTextView = (TextView) view.findViewById(R.id.movie_rating);
        mDescriptionTextView = (TextView) view.findViewById(R.id.movie_description);

        /*Configure view for the selected movie*/
        int movieId = getArguments().getInt(EXTRA_MOVIE_ID);

        mMovie = MovieService.getInstance().fetchMovieWithId(movieId);

        if (mMovie != null) {

            mTitleTextView.setText(mMovie.getTitle());


            if (mCallback != null) {

                mCallback.setTitle(mMovie.getTitle());
            }

            mReleaseYearTextView.setText(mMovie.getRelease_date().substring(0,4));

            String ratingString = getString(R.string.rating_string,mMovie.getVote_average());
            mRatingTextView.setText(ratingString);

            mDescriptionTextView.setText(mMovie.getOverview());

            /*Fetch Movie Poster based on resolution*/
            boolean highRes;

            int imageWidth = getResources().getInteger(R.integer.default_image_width);
            if (imageWidth == MovieService.POSTER_IMAGE_RES_SIZE_780) {
                highRes = true;
            }else {
                highRes = false;
            }

            Picasso.with(getActivity())
                    .load(MovieService.getImageUrlString(mMovie.getPoster_path(),highRes))
                    .placeholder(R.drawable.poster_placeholder)
                    .into(mPosterImageView);

        }


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if ((context instanceof Callback)) {

            mCallback = (Callback) context;
        }
    }

    public interface Callback {
        void setTitle(String title);
    }

}
