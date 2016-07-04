package com.tonyostudio.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.database.FavoriteMovieDatabase;
import com.tonyostudio.popularmovies.model.Movie;
import com.tonyostudio.popularmovies.model.MovieResults;

import java.util.List;

/**
 * Created by tonyofrancis on 3/31/16.
 *
 * This class displays a list of popular Movies
 */
public final class FavoriteMovieListFragment extends MovieListBaseFragment implements FavoriteMovieDatabase.OnDataLoadedCallback {

    @Override
    public String getFragmentId() {
        return "favoriteMovieListFragment";
    }

    /*Get a pre-configured instance of the PopularMovieListFragment Class*/
    public static FavoriteMovieListFragment newInstance() {
        return new FavoriteMovieListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Setup LayoutManager for the RecyclerView*/
        setLayoutManager(new GridLayoutManager(getActivity(),
                getActivity().getResources().getInteger(R.integer.default_grid_columns)));
    }

    @Override
    public void onResume() {
        super.onResume();

        FavoriteMovieDatabase.addOnDataLoadedCallbackListener(this);
        FavoriteMovieDatabase.fetchAll();
    }

    @Override
    public void onPause() {
        super.onPause();

        FavoriteMovieDatabase.removeOnDataLoadedCallbackListener(this);
    }

    @Override
    protected Callback getCallback() {
        return mCallback;
    }

    private Callback mCallback = new Callback() {
        @Override
        public void onMovieItemSelected(Movie movie) {

        }
    };

    @Override
    public void onDataLoaded(List<Movie> movies) {
        getAdapter().swapDataSet(movies);
    }

    @Override
    public void onMovieDataLoaded(MovieResults movieResults) {
        //Does not need to be implement because list comes from
        //local database not from the web server
    }
}
