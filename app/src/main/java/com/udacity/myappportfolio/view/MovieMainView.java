package com.udacity.myappportfolio.view;

import com.udacity.myappportfolio.model.Movie;
import com.udacity.myappportfolio.model.MovieMainBean;

import java.util.List;

/**
 * Created by 587823 on 2/15/2016.
 */
public interface MovieMainView {

    void showProgress();

    void showListProgress();

    void showGrid();

    void showError(String message);

    List<Movie> getMovieList();

    void onSuccess(MovieMainBean bean);

    void onFailure(Throwable t);

}
