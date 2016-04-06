package com.tonyostudio.popularmovies.api;

import android.support.annotation.NonNull;

import com.tonyostudio.popularmovies.model.Movie;
import com.tonyostudio.popularmovies.model.MovieResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tonyofrancis on 3/22/16.
 *
 * The MovieService class is a Singleton class used
 * to communicate with the Movie web service. This class
 * uses the retrofit library to make network calls to the web service.
 * This class performs all network fetches asynchronously and
 * requires all network 'fetch' methods to provide a callback object
 * which is an instance of the inner interface called 'Callback'
 */
public class MovieService {

    public static final int  POSTER_IMAGE_RES_SIZE_180 = 185;
    public static final int POSTER_IMAGE_RES_SIZE_780 = 780;
    public static final int PAGE_MINIMUM = 1;

    private static MovieResults sPopularMovieResults;
    private static MovieResults sTopRatedMovieResults;

    private static MovieService sMovieService;
    private MovieServiceAPI mAPI;

    private MovieService() {
        mAPI = getDefaultRetrofitConfiguration()
                .create(MovieServiceAPI.class);
    }

    /*Method used to access the single instance of MovieService*/
    public static MovieService getInstance() {

        if (sMovieService == null) {
            sMovieService = new MovieService();
        }

        return sMovieService;
    }

    private synchronized static MovieResults getPopularMovieResults() {
        return sPopularMovieResults;
    }

    private synchronized static void setPopularMovieResults(MovieResults popularMovieResults) {
        sPopularMovieResults = popularMovieResults;
    }

    private synchronized static MovieResults getTopRatedMovieResults() {
        return sTopRatedMovieResults;
    }

    private synchronized static void setTopRatedMovieResults(MovieResults topRatedMovieResults) {
        sTopRatedMovieResults = topRatedMovieResults;
    }

    /*Method used to query the MovieService for a Movie object. This method searches the local
     * database cache*/
    public Movie fetchMovieWithId(int movieId) {

        if (getPopularMovieResults() != null) {
            for (Movie movie: getPopularMovieResults().getResults()) {

                if (movie.getId() == movieId) {
                    return movie;
                }
            }
        }

        if (getTopRatedMovieResults() != null) {
            for (Movie movie: getTopRatedMovieResults().getResults()) {

                if (movie.getId() == movieId) {
                    return movie;
                }
            }
        }

        return null;
    }

    public void fetchPopularMovieListAsync(@NonNull Callback callback) {
        fetchMovieListAsync(API.Popular,callback);
    }

    public void fetchTopRatedMovieListAsync(@NonNull Callback callback) {
        fetchMovieListAsync(API.TopRated,callback);
    }

    private void fetchMovieListAsync(API api,Callback callback) {

        if (api == API.Popular) {
            if (getPopularMovieResults() != null) {
                callback.onDataLoaded(getPopularMovieResults().getResults());
                return;
            }

            mAPI.getPopularMovies(PAGE_MINIMUM).enqueue(new CallbackBridge(api,callback));

        } else if(API.TopRated == api) {
            if (getTopRatedMovieResults() != null) {
                callback.onDataLoaded(getTopRatedMovieResults().getResults());
                return;
            }

            mAPI.getTopRatedMovies(PAGE_MINIMUM).enqueue(new CallbackBridge(api,callback));
        }
    }


    public void fetchMoreFromPopularMovieListAsync(@NonNull Callback callback) {
        fetchMoreFromMovieListAsync(API.Popular,callback);
    }

    public void fetchMoreFromTopRatedMovieListAsync(@NonNull Callback callback) {
        fetchMoreFromMovieListAsync(API.TopRated,callback);
    }

    private void fetchMoreFromMovieListAsync(API api,Callback callback) {

        if (api == API.Popular) {

            /*No more pages to fetch. Return localDataSet*/
            if (getPopularMovieResults().getPage() + 1 > getPopularMovieResults().getTotal_pages() ) {
                fetchPopularMovieListAsync(callback);
                return;
            }

            /*Fetch next page data*/
            getPopularMovieResults().setPage(getPopularMovieResults().getPage() + 1);
            mAPI.getPopularMovies(getPopularMovieResults().getPage())
                    .enqueue(new CallbackBridge(API.Popular,callback));

        }else if (api == API.TopRated) {

            if (getTopRatedMovieResults().getPage() + 1 > getTopRatedMovieResults().getTotal_pages() ) {
                fetchTopRatedMovieListAsync(callback);
                return;
            }

            getTopRatedMovieResults().setPage(getTopRatedMovieResults().getPage() + 1);
            mAPI.getTopRatedMovies(getTopRatedMovieResults().getPage())
                    .enqueue(new CallbackBridge(API.TopRated,callback));
        }
    }

    /*The Bridge class is used to pair the MovieService callback with
     * a retrofit callback instance*/
    private static class CallbackBridge implements retrofit2.Callback<MovieResults> {

        private Callback mCallback;
        private API mApi;

        public CallbackBridge(API api,Callback callback) {
            mCallback = callback;
            mApi = api;
        }

        @Override
        public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
            if (response.isSuccessful() && response.body() != null) {

                /*Retrieved movie data from the web service. Store this data
                * and pass it to the callback method*/
                switch (mApi) {

                    case Popular: {

                        if (getPopularMovieResults() == null) {
                            setPopularMovieResults(response.body());
                        } else {

                            //A page fetch was done append data to current dataSet
                            getPopularMovieResults().setPage(response.body().getPage());
                            getPopularMovieResults().getResults().addAll(response.body().getResults());
                        }

                        mCallback.onDataLoaded(getPopularMovieResults().getResults());
                        break;
                    }

                    case TopRated: {
                        if (getTopRatedMovieResults() == null) {
                            setTopRatedMovieResults(response.body());
                        } else {

                            //A page fetch was done append data to current dataSet
                            getTopRatedMovieResults().setPage(response.body().getPage());
                            getTopRatedMovieResults().getResults().addAll(response.body().getResults());
                        }

                        mCallback.onDataLoaded(getTopRatedMovieResults().getResults());

                        break;
                    }
                }

            } else {

                /*Return local data if fetch fails*/
                returnLocalData();
            }

            destroyBridge();
        }

        @Override
        public void onFailure(Call<MovieResults> call, Throwable t) {
            returnLocalData();
        }

        private void returnLocalData() {

            /*Return local data if fetch fails*/
            if (mApi == API.Popular) {
                mCallback.onDataLoaded(getPopularMovieResults().getResults());
            } else if (mApi == API.TopRated) {
                mCallback.onDataLoaded(getTopRatedMovieResults().getResults());
            }
        }

        /*Perform cleanup after data is passed to the callback*/
        private void destroyBridge() {
            mCallback = null;
            mApi = null;
        }
    }

    /*Interface that should be implemented by the object
    * fetching data from the MovieService*/
    public interface Callback {
        void onDataLoaded(List<Movie> movieList);
    }

    /*This method returns the full url path of the movie poster image*/
    public static String getImageUrlString(String imageName,boolean highRes) {

        if (highRes) {
            return MovieServiceAPI.BASE_IMAGE_API + "w" + POSTER_IMAGE_RES_SIZE_780 + "/" + imageName;
        }

        return MovieServiceAPI.BASE_IMAGE_API + "w" + POSTER_IMAGE_RES_SIZE_180 + "/" + imageName;
    }

    /*Get a configured retrofit instance that will be
    * used to communicate with the movie web service*/
    private Retrofit getDefaultRetrofitConfiguration() {
        return  new Retrofit.Builder()
                .baseUrl(MovieServiceAPI.BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /*enum used to make clear which movie service API is being used*/
    private enum API {
        Popular,
        TopRated
    }

    /*The API interface is used by the retrofit library
    * to make network API calls to the movie web api service*/
    private interface MovieServiceAPI{
        String BASE_API = "https://api.themoviedb.org/";
        String BASE_IMAGE_API ="http://image.tmdb.org/t/p/";
        String API_KEY = APIKEY; //Replace with API_KEY


        @GET("/3/movie/popular?&api_key="+API_KEY)
        Call<MovieResults> getPopularMovies(@Query("page") int page);

        @GET("/3/movie/top_rated?&api_key="+API_KEY)
        Call<MovieResults> getTopRatedMovies(@Query("page") int page);
    }
}
