package com.tonyostudio.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.Utils.SpacesItemDecoration;
import com.tonyostudio.popularmovies.adapter.MovieListAdapter;
import com.tonyostudio.popularmovies.api.MovieService;
import com.tonyostudio.popularmovies.model.Movie;

/**
 * Created by tonyofrancis on 3/31/16.
 *
 * Base Class which can be subClasses by another
 * Class which will display a list of movie's.
 * This class prepares the recyclerView, LayoutManager
 * and adapter
 */
public abstract class MovieListBaseFragment extends Fragment implements MovieService.Callback.MovieListCallback{

    private RecyclerView mMovieListRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int currentPage;

    public abstract String getFragmentId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPage = 1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        /*Inflate and initialize the fragment's view*/
        final View view = inflater.inflate(R.layout.fragment_movie_list,container,false);
        mMovieListRecyclerView = (RecyclerView) view.findViewById(R.id.movie_list_recycler_view);

        /*Set the recyclerView LayoutManager */
        mMovieListRecyclerView.setLayoutManager(mLayoutManager);

        final int posterSpacing = getResources().getDimensionPixelOffset(R.dimen.default_poster_spacing);
        mMovieListRecyclerView.addItemDecoration(new SpacesItemDecoration(posterSpacing));

        /*Setup RecyclerView Adapter*/
        mMovieListAdapter = new MovieListAdapter(getCallback());
        mMovieListRecyclerView.setAdapter(mMovieListAdapter);

        return view;
    }

    protected abstract Callback getCallback();


    /*Callback which needs to be implemented by the hosting Activity*/
    public interface Callback {
        void onMovieItemSelected(Movie movie);
    }


    /*Setters & Getters that Sub Classes can use*/
    public RecyclerView getRecyclerView() {
        return mMovieListRecyclerView;
    }

    public MovieListAdapter getAdapter() {
        return mMovieListAdapter;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
