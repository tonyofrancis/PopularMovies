package com.tonyostudio.popularmovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.tonyostudio.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tonyofrancis on 6/9/16.
 */

public final class FavoriteMovieDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "favoritemovies.db";
    private static FavoriteMovieDatabase sFavoriteMovieDatabase;

    private List<OnDataLoadedCallback> mOnDataLoadedCallbackListeners;

    private static final class Table {

        static final String TABLE_NAME = "favs";

        static final class Cols {
            static final String ID = "movie_id";
            static final String POSTER_PATH = "poster_path";
            static final String VOTE_AVERAGE = "vote_average";
            static final String RELEASE_DATE = "release_date";
            static final String TITLE = "title";
            static final String ADULT = "adult";
            static final String OVERVIEW = "overview";
            static final String ORIGINAL_TITLE = "original_title";
            static final String ORIGINAL_LANGUAGE = "original_language";
            static final String BACKDROP_PATH = "backdrop_path";
            static final String POPULARITY = "popularity";
            static final String VOTE_COUNT = "vote_count";
            static final String VIDEO = "video";
        }
    }

    private FavoriteMovieDatabase(Context context) {
        super(context,DATABASE_NAME,null, VERSION);
        mOnDataLoadedCallbackListeners = new ArrayList<>();
    }

    public static FavoriteMovieDatabase newInstance(Context context) {

        if (sFavoriteMovieDatabase == null) {
            sFavoriteMovieDatabase = new FavoriteMovieDatabase(context);
        }

        return sFavoriteMovieDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create database Table.
        db.execSQL("CREATE TABLE " + Table.TABLE_NAME + " ( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Table.Cols.ID + ", "
                + Table.Cols.POSTER_PATH + ", "
                + Table.Cols.VOTE_AVERAGE + ", "
                + Table.Cols.RELEASE_DATE + ", "
                + Table.Cols.TITLE + ", "
                + Table.Cols.ADULT + ", "
                + Table.Cols.OVERVIEW + ", "
                + Table.Cols.ORIGINAL_TITLE + ", "
                + Table.Cols.ORIGINAL_LANGUAGE + ", "
                + Table.Cols.BACKDROP_PATH + ", "
                + Table.Cols.POPULARITY + ", "
                + Table.Cols.VOTE_COUNT + ", "
                + Table.Cols.VIDEO + " );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public static void fetchAll() {
        sFavoriteMovieDatabase.updateOnDataLoadedCallbackListeners();
    }

    private Cursor getAllDatabaseData() {
        return sFavoriteMovieDatabase.getReadableDatabase()
                .query(Table.TABLE_NAME,null,null,null,null,null,null);
    }

    private List<Movie> getMovieListFromCursor(Cursor cursor) {

        if (cursor == null) {
            return null;
        }

        final List<Movie> movieList = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            movieList.add(sFavoriteMovieDatabase.getMovieAtCursorLocation(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return movieList;
    }

    private Map<Integer,Movie> getMovieMapFromCursor(Cursor cursor) {

        if (cursor == null) {
            return null;
        }

        final Map<Integer, Movie> movieMap = new HashMap<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            final Movie movie = sFavoriteMovieDatabase.getMovieAtCursorLocation(cursor);
            movieMap.put(movie.getId(),movie);
            cursor.moveToNext();
        }
        cursor.close();

        return movieMap;
    }

    private Movie getMovieAtCursorLocation(Cursor cursor) {
       final Movie movie = new Movie();

        movie.setId(cursor.getInt(cursor.getColumnIndex(Table.Cols.ID)));
        movie.setPoster_path(cursor.getString(cursor.getColumnIndex(Table.Cols.POSTER_PATH)));
        movie.setVote_average(cursor.getFloat(cursor.getColumnIndex(Table.Cols.VOTE_AVERAGE)));
        movie.setRelease_date(cursor.getString(cursor.getColumnIndex(Table.Cols.RELEASE_DATE)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(Table.Cols.TITLE)));
        movie.setAdult(cursor.getInt(cursor.getColumnIndex(Table.Cols.ADULT)) == 1);
        movie.setOverview(cursor.getString(cursor.getColumnIndex(Table.Cols.OVERVIEW)));
        movie.setOriginal_title(cursor.getString(cursor.getColumnIndex(Table.Cols.ORIGINAL_TITLE)));
        movie.setOriginal_language(cursor.getString(cursor.getColumnIndex(Table.Cols.ORIGINAL_LANGUAGE)));
        movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(Table.Cols.BACKDROP_PATH)));
        movie.setPopularity(cursor.getFloat(cursor.getColumnIndex(Table.Cols.POPULARITY)));
        movie.setVote_count(cursor.getInt(cursor.getColumnIndex(Table.Cols.VOTE_COUNT)));
        movie.setVideo(cursor.getInt(cursor.getColumnIndex(Table.Cols.VIDEO)) == 1);

        return movie;
    }

    public static void put(final Movie movie) {
        sFavoriteMovieDatabase.getWritableDatabase()
                .insert(Table.TABLE_NAME,null,sFavoriteMovieDatabase.getContentValue(movie));

        sFavoriteMovieDatabase.updateOnDataLoadedCallbackListeners();
    }

    private ContentValues getContentValue(Movie movie) {

        final ContentValues content = new ContentValues();

        content.put(Table.Cols.ID,movie.getId());
        content.put(Table.Cols.POSTER_PATH,movie.getPoster_path());
        content.put(Table.Cols.VOTE_AVERAGE,movie.getVote_average());
        content.put(Table.Cols.RELEASE_DATE,movie.getRelease_date());
        content.put(Table.Cols.TITLE,movie.getTitle());
        content.put(Table.Cols.ADULT,movie.isAdult());
        content.put(Table.Cols.OVERVIEW,movie.getOverview());
        content.put(Table.Cols.ORIGINAL_TITLE,movie.getOriginal_title());
        content.put(Table.Cols.ORIGINAL_LANGUAGE,movie.getOriginal_language());
        content.put(Table.Cols.BACKDROP_PATH,movie.getBackdrop_path());
        content.put(Table.Cols.POPULARITY,movie.getPopularity());
        content.put(Table.Cols.VOTE_COUNT,movie.getVote_count());
        content.put(Table.Cols.VIDEO,movie.isVideo());

        return content;
    }

    public static void remove(final int movieId) {

        sFavoriteMovieDatabase.getWritableDatabase()
                .execSQL("DELETE FROM "+ Table.TABLE_NAME +" WHERE "
                        + Table.Cols.ID + " = " + String.valueOf(movieId)+ ";");

        sFavoriteMovieDatabase.updateOnDataLoadedCallbackListeners();
    }

    public static void isMovieAFavorite(final int movieId,@NonNull final OnMovieIsFavoriteCallback callback) {

        Cursor cursor = sFavoriteMovieDatabase.getAllDatabaseData();
        final Map<Integer,Movie> movieMap = sFavoriteMovieDatabase
                .getMovieMapFromCursor(cursor);

        cursor.close();

        if (movieMap == null || movieMap.size() == 0) {
            callback.onMovieIsFavorite(false);
        }

        callback.onMovieIsFavorite(movieMap.containsKey(movieId));
    }

    public static void addOnDataLoadedCallbackListener(OnDataLoadedCallback listener) {
        sFavoriteMovieDatabase.mOnDataLoadedCallbackListeners.add(listener);
    }

    public static void removeOnDataLoadedCallbackListener(OnDataLoadedCallback listener) {
        sFavoriteMovieDatabase.mOnDataLoadedCallbackListeners.remove(listener);
    }

    private void updateOnDataLoadedCallbackListeners() {

        Cursor cursor = sFavoriteMovieDatabase.getAllDatabaseData();
        final List<Movie> movieList = sFavoriteMovieDatabase
                .getMovieListFromCursor(cursor);

        cursor.close();

        for (OnDataLoadedCallback onDataLoadedCallbackListener : sFavoriteMovieDatabase.mOnDataLoadedCallbackListeners) {
            onDataLoadedCallbackListener.onDataLoaded(movieList);
        }
    }

    public interface OnDataLoadedCallback {
        void onDataLoaded(List<Movie> movies);
    }

    public interface OnMovieIsFavoriteCallback {
        void onMovieIsFavorite(boolean isFavorite);
    }
}
