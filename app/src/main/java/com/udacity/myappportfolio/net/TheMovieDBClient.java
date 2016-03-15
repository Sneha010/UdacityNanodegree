package com.udacity.myappportfolio.net;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.udacity.myappportfolio.db.FavMovieContract;
import com.udacity.myappportfolio.model.Movie;
import com.udacity.myappportfolio.model.MovieMainBean;
import com.udacity.myappportfolio.model.ReviewMainBean;
import com.udacity.myappportfolio.model.TrailerMainBean;
import com.udacity.myappportfolio.util.Constants;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class TheMovieDBClient {

    private MovieRestAPI myService = null;

    private Context context;

    private static TheMovieDBClient client;



    private TheMovieDBClient() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(getLoggingClient())
                .build();

        myService = retrofit.create(MovieRestAPI.class);
    }


    public static TheMovieDBClient getInstance() {

        if (client == null) {
            client = new TheMovieDBClient();
        }

        return client;

    }

    public void loadMovies(int pageNo , String sortBy, final MovieDBResponseListener listener) {

        Call<MovieMainBean> call = myService.loadMovies(sortBy, Constants.API_KEY, pageNo + "");

        call.enqueue(new Callback<MovieMainBean>() {
            @Override
            public void onResponse(Response<MovieMainBean> response,
                                   Retrofit retrofit) {

                MovieMainBean bean = response.body();
                listener.onSuccess(bean);
            }

            @Override
            public void onFailure(Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public void loadTrailers(int movieId, final TrailerResponseListener listener) {

        Call<TrailerMainBean> call = myService.loadTrailers(movieId ,Constants.API_KEY);

        call.enqueue(new Callback<TrailerMainBean>() {
            @Override
            public void onResponse(Response<TrailerMainBean> response,
                                   Retrofit retrofit) {

                TrailerMainBean bean = response.body();
                listener.onTrailerSuccess(bean);
            }

            @Override
            public void onFailure(Throwable t) {
                listener.onTrailerFailure(t);
            }
        });
    }


    public void loadReviews(int movieId ,final ReviewResponseListener listener) {

        Call<ReviewMainBean> call = myService.loadReviews(movieId, Constants.API_KEY);

        call.enqueue(new Callback<ReviewMainBean>() {
            @Override
            public void onResponse(Response<ReviewMainBean> response,
                                   Retrofit retrofit) {

                ReviewMainBean bean = response.body();
                listener.onReviewSuccess(bean);
            }

            @Override
            public void onFailure(Throwable t) {
                listener.onReviewFailure(t);
            }
        });
    }

    public ArrayList<Movie> loadMovieFromDb(Activity context) {
        ArrayList<Movie> favMovieList = new ArrayList<>();

        Uri uri = FavMovieContract.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        String[] projection = new String[]{FavMovieContract.Columns.MOVIE_ID,
                FavMovieContract.Columns.MOVIE_TITLE,
                FavMovieContract.Columns.MOVIE_RELEASE_DATE,
                FavMovieContract.Columns.MOVIE_RATING,
                FavMovieContract.Columns.MOVIE_SYNOPSIS,
                FavMovieContract.Columns.MOVIE_POSTER_URL};
        Cursor cursor =
                resolver.query(uri, projection,null, null,null);

        if (cursor.moveToFirst()) {
            do {
                Movie m = new Movie();

                m.setId(cursor.getInt(0));
                m.setOriginal_title(cursor.getString(1));
                m.setRelease_date(cursor.getString(2));
                m.setVote_average(Double.parseDouble(cursor.getString(3)));
                m.setOverview(cursor.getString(4));
                m.setPoster_path(cursor.getString(5));

                favMovieList.add(m);

            } while (cursor.moveToNext());
        }

        return favMovieList;
    }


    private OkHttpClient getLoggingClient(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(logging);

        return client;

    }


}
