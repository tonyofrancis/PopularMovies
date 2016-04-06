package com.tonyostudio.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tonyostudio.popularmovies.fragment.MovieDetailFragment;

/**
 * Created by tonyofrancis on 3/23/16.
 *
 * This activity displays the details of the
 * selected movie.
 */
public class MovieDetailActivity extends SingleFragmentActivity implements
        MovieDetailFragment.Callback {

    private static final String EXTRA_MOVIE_ID = "com.tonyostudio.popularmovies.extra_movie_id";

    protected Fragment createFragment() {
        return MovieDetailFragment.newInstance(getIntent().getIntExtra(EXTRA_MOVIE_ID,0));
    }

    /*Create an intent that will launch an instance of MovieDetailActivity
     * and also pass it an extra for the movieId selected*/
    public static Intent newIntent(Context applicationContext, int movieId) {

        Intent intent = new Intent(applicationContext,MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID,movieId);

        return intent;

    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
