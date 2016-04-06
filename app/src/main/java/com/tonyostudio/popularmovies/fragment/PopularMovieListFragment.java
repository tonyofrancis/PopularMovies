package com.tonyostudio.popularmovies.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.api.MovieService;
import com.tonyostudio.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by tonyofrancis on 3/31/16.
 *
 * This class displays a list of popular Movies
 */
public class PopularMovieListFragment extends MovieListBaseFragment {

    /*Get a pre-configured instance of the PopularMovieListFragment Class*/
    public static PopularMovieListFragment newInstance(Context context) {
        
        PopularMovieListFragment fragment = new PopularMovieListFragment();
        fragment.setTitle(context.getString(R.string.most_popular));

        return fragment;
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

                    MovieService.getInstance()
                            .fetchMoreFromPopularMovieListAsync(PopularMovieListFragment.this);
                }
            }
        });

        //Load Movie Data
        MovieService.getInstance()
                .fetchPopularMovieListAsync(this);

    }

    @Override
    public void onDataLoaded(List<Movie> movieList) {
        //Update the Adapter with MovieList Data
        getAdapter().swapDataSet(movieList);
    }
}
