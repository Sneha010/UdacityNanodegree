package com.udacity.myappportfolio.view;

import com.udacity.myappportfolio.model.Movie;
import com.udacity.myappportfolio.model.MovieMainBean;

import java.util.List;


public interface MovieMainView {

    void showProgress();

    void showListProgress();

    void showGrid();

    void allViewGone();

    void showError(String message);

    List<Movie> getMovieList();

    void onSuccess(MovieMainBean bean);

    void onFailure(Throwable t);

}
