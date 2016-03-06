package com.udacity.myappportfolio.net;

import com.udacity.myappportfolio.model.MovieMainBean;
import com.udacity.myappportfolio.model.ReviewMainBean;
import com.udacity.myappportfolio.model.TrailerMainBean;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;


public interface MovieRestAPI {
    @GET("/discover/movie")
    Call<MovieMainBean> loadMovies(@Query("sort_by") String sort_by ,
                                   @Query("api_key") String api_key ,
                                   @Query("page") String page);

    @GET("/reviews")
    Call<ReviewMainBean> loadReviews(@Query("api_key") String api_key);

    @GET("/videos")
    Call<TrailerMainBean> loadTrailers(@Query("api_key") String api_key);

}
