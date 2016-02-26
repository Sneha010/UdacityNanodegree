package com.udacity.myappportfolio.net;

import android.content.Context;

import com.udacity.myappportfolio.model.MovieMainBean;
import com.udacity.myappportfolio.util.Constants;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by 587823 on 2/2/2016.
 */
public class TheMovieDBClient {

    private MovieRestAPI myService = null;

    private Context context;

    private static TheMovieDBClient client;



    private TheMovieDBClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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


}
