package com.tonyostudio.popularmovies.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.adapter.MovieListAdapter;
import com.tonyostudio.popularmovies.api.MovieService;

/**
 * Created by tonyofrancis on 3/31/16.
 *
 * Base Class which can be subClasses by another
 * Class which will display a list of movie's.
 * This class prepares the recyclerView, LayoutManager
 * and adapter
 */
public abstract class MovieListBaseFragment extends Fragment
        implements MovieService.Callback {

    private String mTitle;
    private RecyclerView mMovieListRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*Inflate and initialize the fragment's view*/
        View view = inflater.inflate(R.layout.fragment_movie_list,container,false);
        mMovieListRecyclerView = (RecyclerView) view.findViewById(R.id.movie_list_recycler_view);

        /*Set the recyclerView LayoutManager */
        mMovieListRecyclerView.setLayoutManager(mLayoutManager);

        /*Setup RecyclerView Adapter*/
        mMovieListAdapter = new MovieListAdapter(getActivity());
        mMovieListRecyclerView.setAdapter(mMovieListAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof Callback)) {

            throw new ClassCastException("Context must implement the MovieListBaseFragment.Callback " +
                    "interface.");
        }
    }

    /*Callback which needs to be implemented by the hosting Activity*/
    public interface Callback {
        void onMovieItemSelected(int id);
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

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    /*Method used to get the title of the List*/
    public String getTitle(){return mTitle;}

    public void setTitle(String title) {
        mTitle = title;
    }
}
