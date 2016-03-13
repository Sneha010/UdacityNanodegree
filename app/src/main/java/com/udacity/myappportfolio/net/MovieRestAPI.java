package com.udacity.myappportfolio.net;

import com.udacity.myappportfolio.model.MovieMainBean;
import com.udacity.myappportfolio.model.ReviewMainBean;
import com.udacity.myappportfolio.model.TrailerMainBean;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


public interface MovieRestAPI {
    @GET("discover/movie")
    Call<MovieMainBean> loadMovies(@Query("sort_by") String sort_by ,
                                   @Query("api_key") String api_key ,
                                   @Query("page") String page);

    @GET("movie/{id}/reviews")
    Call<ReviewMainBean> loadReviews(@Path("id") int movieId,
                                     @Query("api_key") String api_key);

    @GET("movie/{id}/videos")
    Call<TrailerMainBean> loadTrailers(@Path("id") int movieId,
                                       @Query("api_key") String api_key);

}
