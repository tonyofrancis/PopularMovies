package com.tonyostudio.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tonyostudio.popularmovies.api.AppCache;
import com.tonyostudio.popularmovies.fragment.MovieDetailFragment;
import com.tonyostudio.popularmovies.model.Movie;


/**
 * Created by tonyofrancis on 3/23/16.
 *
 * This activity displays the details of the
 * selected movie.
 */
public final class MovieDetailActivity extends SingleFragmentActivity implements
        MovieDetailFragment.Callback {

    private static final String EXTRA_MOVIE_ID = "com.tonyostudio.popularmovies.extra_movie_id";

    protected Fragment createFragment() {
        return MovieDetailFragment.newInstance((Movie) AppCache.retrieve(EXTRA_MOVIE_ID));
    }

    public static Intent newIntent(Context context, Movie movie) {

        AppCache.store(EXTRA_MOVIE_ID,movie);

        return new Intent(context,MovieDetailActivity.class);
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
