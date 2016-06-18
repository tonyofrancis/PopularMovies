package com.tonyostudio.popularmovies;

import android.app.Application;

import com.tonyostudio.popularmovies.api.AppCache;
import com.tonyostudio.popularmovies.api.MovieService;
import com.tonyostudio.popularmovies.database.FavoriteMovieDatabase;

/**
 * Created by tonyofrancis on 5/30/16.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /*Init MovieService & Realm Database here*/
        MovieService.initialize(this);
        FavoriteMovieDatabase.newInstance(this);
        AppCache.initialize();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AppCache.reduceCache();
    }
}
