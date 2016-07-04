package com.tonyostudio.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.activity.MovieDetailActivity;
import com.tonyostudio.popularmovies.api.AppCache;
import com.tonyostudio.popularmovies.api.MovieService;
import com.tonyostudio.popularmovies.model.Movie;
import com.tonyostudio.popularmovies.model.MovieResults;

import java.util.List;

/**
 * Created by tonyofrancis on 3/31/16.
 *
 * This class displays a list of popular Movies
 */
public final class PopularMovieListFragment extends MovieListBaseFragment {

    @Override
    public String getFragmentId() {
        return "popularMovieListFragment";
    }

    /*Get a pre-configured instance of the PopularMovieListFragment Class*/
    public static PopularMovieListFragment newInstance() {
        return new PopularMovieListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Setup LayoutManager for the RecyclerView*/
        setLayoutManager(new GridLayoutManager(getActivity(),
                getActivity().getResources().getInteger(R.integer.default_grid_columns)));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Implementation of the RecyclerScrollListener that will handle pagination for
         * the Movie Web Service*/
        getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();

                int firstVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager())
                        .findFirstVisibleItemPosition();

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                        firstVisibleItemPosition >= 0 && totalItemCount >= getAdapter().getItemCount()) {

                    MovieService.fetchPopularMovieListAsync(PopularMovieListFragment.this,getCurrentPage() + 1);
                }
            }
        });


        if(savedInstanceState != null) {
            getAdapter().swapDataSet((List<Movie>) AppCache.retrieve(getFragmentId()));
        } else {
            MovieService.fetchPopularMovieListAsync(this,getCurrentPage());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        AppCache.store(getFragmentId(),getAdapter().getDataSet());
    }

    @Override
    protected Callback getCallback() {
        return mCallback;
    }

    private Callback mCallback = new Callback() {
        @Override
        public void onMovieItemSelected(Movie movie) {
            startActivity(MovieDetailActivity.newIntent(getActivity(),movie));
        }
    };

    @Override
    public void onMovieDataLoaded(MovieResults movieResults) {

        if(movieResults != null) {
            getAdapter().mergeDataSet(movieResults.getResults());
            setCurrentPage(movieResults.getPage());
        }
    }
}