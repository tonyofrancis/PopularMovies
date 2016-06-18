package com.tonyostudio.popularmovies.activity;

import android.support.v4.app.Fragment;

import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.fragment.MovieDetailFragment;
import com.tonyostudio.popularmovies.fragment.MovieListBaseFragment;
import com.tonyostudio.popularmovies.fragment.MoviePagerFragment;
import com.tonyostudio.popularmovies.model.Movie;

/*This activity displays a list of popular and top rated movies
* in a Grid Layout*/
public final class MovieListActivity extends SingleFragmentActivity
        implements MovieListBaseFragment.Callback, MovieDetailFragment.Callback {

    @Override
    protected Fragment createFragment() {
        return MoviePagerFragment.newInstance();
    }

    /*If we are on a phone device, launch the detail activity when a movie is selected.
    * If we are on a tablet device, commit the detail fragment into the same activity(this) */
    @Override
    public void onMovieItemSelected(Movie movie) {

        if (findViewById(R.id.detail_container) == null) {

            startActivity(MovieDetailActivity.newIntent(this,movie));

        }else {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, MovieDetailFragment.newInstance(movie))
                    .commit();
        }
    }

    @Override
    public void setTitle(String title) {
    }
}
