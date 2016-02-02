package com.udacity.myappportfolio.net;

import android.content.Context;

import com.udacity.myappportfolio.R;
import com.udacity.myappportfolio.bean.MovieMainBean;
import com.udacity.myappportfolio.util.Constants;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by 587823 on 2/2/2016.
 */
public class TheMovieDBClient {

    private MovieRestAPI myService;
    private Context context;


    public TheMovieDBClient(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myService = retrofit.create(MovieRestAPI.class);
    }


    public void loadMovies(String sortBy, int pageNo , final MovieDBResponseListener listener) {


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

                MovieDBError error = buildErrorResponse(t);
                listener.onFailure(error);

            }
        });
    }

    private MovieDBError buildErrorResponse(Throwable t) {

        String errorMessage =  null;

        MovieDBError error = new MovieDBError();
        if(t instanceof IOException)
            errorMessage = context.getResources().getString(R.string.no_internet);
        else
            errorMessage = context.getResources().getString(R.string.server_error);

        error.setErrorMessage(errorMessage);

        return error;

    }
}
