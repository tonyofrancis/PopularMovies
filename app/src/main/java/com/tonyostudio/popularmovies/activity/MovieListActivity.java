package com.tonyostudio.popularmovies.activity;

import android.support.v4.app.Fragment;

import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.fragment.MovieDetailFragment;
import com.tonyostudio.popularmovies.fragment.MovieListBaseFragment;
import com.tonyostudio.popularmovies.fragment.MoviePagerFragment;

/*This activity displays a list of popular and top rated movies
* in a Grid Layout*/
public class MovieListActivity extends SingleFragmentActivity
        implements MovieListBaseFragment.Callback {

    @Override
    protected Fragment createFragment() {
        return MoviePagerFragment.newInstance();
    }

    /*If we are on a phone device, launch the detail activity when a movie is selected.
    * If we are on a tablet device, commit the detail fragment into the same activity(this) */
    @Override
    public void onMovieItemSelected(int movieId) {

        if (findViewById(R.id.detail_container) == null) {

            startActivity(MovieDetailActivity.newIntent(this,movieId));

        }else {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container,MovieDetailFragment.newInstance(movieId))
                    .commit();
        }
    }
}
