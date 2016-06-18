package com.tonyostudio.popularmovies.api;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.tonyostudio.popularmovies.R;
import com.tonyostudio.popularmovies.model.MovieResults;
import com.tonyostudio.popularmovies.model.ReviewResults;
import com.tonyostudio.popularmovies.model.TrailerResults;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
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
public final class MovieService {

    private static MovieService sMovieService;
    private MovieServiceAPI mAPI;
    private Context mContext;

    private MovieService(Context context) {

        mContext = context;

        mAPI = getDefaultRetrofitConfiguration()
                .create(MovieServiceAPI.class);
    }

    /*Method used to access the single instance of MovieService*/
    public static void initialize(Application application) {

        if (sMovieService == null) {
            sMovieService = new MovieService(application);
        }
    }

    public static void fetchPopularMovieListAsync(@NonNull final Callback.MovieListCallback callback,int page) {

        sMovieService.mAPI.getPopularMovies(page).enqueue(new retrofit2.Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {

                if(response.isSuccessful()) {
                    callback.onMovieDataLoaded(response.body());
                } else {
                    callback.onMovieDataLoaded(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                callback.onMovieDataLoaded(null);
            }
        });
    }

    public static void fetchTopRatedMovieListAsync(@NonNull final Callback.MovieListCallback callback, int page) {
        sMovieService.mAPI.getTopRatedMovies(page)
                .enqueue(new retrofit2.Callback<MovieResults>() {
                    @Override
                    public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {

                        if(response.isSuccessful()) {
                            callback.onMovieDataLoaded(response.body());
                        } else {
                            callback.onMovieDataLoaded(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResults> call, Throwable t) {
                        callback.onMovieDataLoaded(null);
                    }
                });
    }

    public static void fetchTrailersListAsync(String movieId,@NonNull final Callback.TrailerListCallback callback) {

        sMovieService.mAPI.getMovieTrailers(movieId).enqueue(new retrofit2.Callback<TrailerResults>() {
            @Override
            public void onResponse(Call<TrailerResults> call, Response<TrailerResults> response) {

                if (response.isSuccessful()) {
                    callback.onTrailerDataLoaded(response.body());
                } else {
                    callback.onTrailerDataLoaded(null);
                }
            }

            @Override
            public void onFailure(Call<TrailerResults> call, Throwable t) {
                callback.onTrailerDataLoaded(null);
            }
        });
    }

    public static void fetchMovieReviewsListAsync(String movieId, final Callback.ReviewListCallback callback) {

        sMovieService.mAPI.getMovieReviews(movieId).enqueue(new retrofit2.Callback<ReviewResults>() {
            @Override
            public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {

                if (response.isSuccessful()) {
                    callback.onReviewDataLoaded(response.body());
                } else {
                    callback.onReviewDataLoaded(null);
                }
            }

            @Override
            public void onFailure(Call<ReviewResults> call, Throwable t) {
                callback.onReviewDataLoaded(null);
            }
        });
    }

    /* Callback Interfaces that should be implemented by the object
    * fetching data from the MovieService*/
    public static class Callback {

        public interface MovieListCallback {
            void onMovieDataLoaded(MovieResults movieResults);
        }

        public interface TrailerListCallback {
            void onTrailerDataLoaded(TrailerResults trailerResults);
        }

        public interface ReviewListCallback {
            void onReviewDataLoaded(ReviewResults reviewResults);
        }
    }

    /*This method returns the full url path of the movie poster
    * image depending on which quality*/
    public static String getImageUrlString(String imageName) {

        /*Fetch Movie Poster based on device resolution*/
        boolean highRes;

        final int imageWidth = sMovieService.mContext.getResources().getInteger(R.integer.default_image_width);

        if (imageWidth == MovieServiceAPI.POSTER_IMAGE_RES_SIZE_780) {
            highRes = true;
        }else {
            highRes = false;
        }

        if (highRes) {
            return MovieServiceAPI.BASE_IMAGE_API + "w" + MovieServiceAPI.POSTER_IMAGE_RES_SIZE_780 + "/" + imageName;
        }

        return MovieServiceAPI.BASE_IMAGE_API + "w" + MovieServiceAPI.POSTER_IMAGE_RES_SIZE_180 + "/" + imageName;
    }

    /*Method used to get the trailers URL*/
    public static String getTrailerUrlString(String key) {
        return MovieServiceAPI.BASE_TRAILER_URL + key;
    }

    /*Get a configured retrofit instance that will be
    * used to communicate with the movie web service*/
    private Retrofit getDefaultRetrofitConfiguration() {
        return new Retrofit.Builder()
//                .client(getOkHttpCacheClient())
                .baseUrl(MovieServiceAPI.BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    /**Get a configured OkHttpClient instance that supports local
     * caching of network requests.*/
    private OkHttpClient getOkHttpCacheClient() {
        return new OkHttpClient.Builder()
                .cache(new Cache(mContext.getExternalCacheDir(),MovieServiceAPI.API_CACHE_SIZE))
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        if(isNetworkAvailable()) {

                            //If the network is available fetch new data every 5 mins from the network
                            // otherwise use the local cache to fetch the last request.
                            request = request.newBuilder()
                                    .header("Cache-Control","public, max-age=" + 300)
                                    .build();
                        } else {

                            //If the network is not available used the last cached request up
                            // to 7 days ago
                            request = request.newBuilder()
                                    .header("Cache-Control","public, only-if-cached, max-stale="+ 60*60*24*7)
                                    .build();
                        }

                        return chain.proceed(request);
                    }
                })
                .build();
    }

    /**Method used to determine if the device is connected to a network*/
    private boolean isNetworkAvailable() {
        final ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }



    /*The API interface is used by the retrofit library
    * to make network API calls to the movie web api service*/
    private interface MovieServiceAPI{
        String BASE_API = "https://api.themoviedb.org/";
        String BASE_IMAGE_API = "http://image.tmdb.org/t/p/";
        String API_KEY = ""; //TODO: API KEY HERE
        String BASE_TRAILER_URL = "https://www.youtube.com/watch?v=";
        int  POSTER_IMAGE_RES_SIZE_180 = 185;
        int POSTER_IMAGE_RES_SIZE_780 = 780;
        int API_CACHE_SIZE = 10 * 1024 * 1024;

        @GET("/3/movie/popular?&api_key="+API_KEY)
        Call<MovieResults> getPopularMovies(@Query("page") int page);

        @GET("/3/movie/top_rated?&api_key="+API_KEY)
        Call<MovieResults> getTopRatedMovies(@Query("page") int page);

        @GET("/3/movie/{id}/videos?&api_key="+API_KEY)
        Call<TrailerResults>getMovieTrailers(@Path("id") String id);

        @GET("/3/movie/{id}/reviews?&api_key="+API_KEY)
        Call<ReviewResults>getMovieReviews(@Path("id") String id);
    }
}
