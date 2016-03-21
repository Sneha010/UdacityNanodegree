package com.udacity.myappportfolio.presenter;


import android.app.Activity;

import com.udacity.myappportfolio.model.Movie;

public interface ReviewTrailerPresenter {

    void fetchReviews(int id);
    void fetchTrailers(int id);
    boolean addMovieToDb(Activity context , Movie movie);
    int removeMovieToDb(Activity context , int id);
    boolean checkForFavMovie(Activity context , int id);
}
