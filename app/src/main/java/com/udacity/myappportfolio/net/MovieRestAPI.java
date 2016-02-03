package com.udacity.myappportfolio.net;

import com.udacity.myappportfolio.bean.MovieMainBean;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by HP on 1/3/2016.
 */
public interface MovieRestAPI {
    @GET("/3/discover/movie")
    Call<MovieMainBean> loadMovies(@Query("sort_by") String sort_by ,
                                   @Query("api_key") String api_key ,
                                   @Query("page") String page);
}
