package com.udacity.myappportfolio.net;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.udacity.myappportfolio.model.MovieMainBean;
import com.udacity.myappportfolio.model.ReviewMainBean;
import com.udacity.myappportfolio.model.TrailerMainBean;
import com.udacity.myappportfolio.util.Constants;

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



    private OkHttpClient getLoggingClient(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(logging);

        return client;

    }


}
