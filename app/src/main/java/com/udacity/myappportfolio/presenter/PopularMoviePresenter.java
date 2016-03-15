package com.udacity.myappportfolio.presenter;

import android.app.Activity;

import com.udacity.myappportfolio.model.Movie;

import java.util.ArrayList;

public interface PopularMoviePresenter {

    void fetchMovieList( int pageNo ,String sortBy);

    ArrayList<Movie> fetchMovieFromDb(Activity context);

    void onDestroy();

}
