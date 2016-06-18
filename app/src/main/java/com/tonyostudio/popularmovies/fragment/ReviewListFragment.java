package com.tonyostudio.popularmovies.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.Utils.SpacesItemDecoration;
import com.tonyostudio.popularmovies.adapter.ReviewAdapter;
import com.tonyostudio.popularmovies.api.AppCache;
import com.tonyostudio.popularmovies.api.MovieService;
import com.tonyostudio.popularmovies.model.Movie;
import com.tonyostudio.popularmovies.model.ReviewResults;

/**
 * Created by tonyofrancis on 5/19/16.
 */

public final class ReviewListFragment extends DialogFragment
        implements MovieService.Callback.ReviewListCallback {

    private static final String FRAGMENT_ID = "reviewListFragment";

    private Movie mMovie;
    private ReviewAdapter mReviewAdapter;


    public static ReviewListFragment newInstance(@NonNull Movie movie) {

        ReviewListFragment fragment = new ReviewListFragment();
        fragment.setMovie(movie);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mMovie = (Movie) AppCache.retrieve(FRAGMENT_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        AppCache.store(FRAGMENT_ID,mMovie);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_review_dialog,container,false);

        final RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_review_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0,0,0,16));

        TextView titleView = (TextView) view.findViewById(R.id.movie_title);
        titleView.setText(mMovie.getTitle());

        mReviewAdapter = new ReviewAdapter(getActivity());
        mRecyclerView.setAdapter(mReviewAdapter);

        return view;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        MovieService.fetchMovieReviewsListAsync(String.valueOf(mMovie.getId()),this);
    }

    private void setMovie(Movie movie) {
        mMovie = movie;
    }

    private void updateView(boolean isReviewAvailable) {

        final View recyclerView = getView().findViewById(R.id.movie_review_recycler_view);
        final TextView view = (TextView) getView().findViewById(R.id.no_movie_review_label);

        if(isReviewAvailable) {
            recyclerView.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
            view.setText(R.string.no_movie_review_label);
        }
    }

    @Override
    public void onReviewDataLoaded(ReviewResults reviewResults) {
        if(reviewResults == null || reviewResults.getResults().size() == 0) {
            updateView(false);
        } else {
            updateView(true);
        }

        mReviewAdapter.swapDataSet(reviewResults.getResults());
    }
}
