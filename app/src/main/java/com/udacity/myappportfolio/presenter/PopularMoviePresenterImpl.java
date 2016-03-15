package com.udacity.myappportfolio.presenter;

import android.app.Activity;

import com.udacity.myappportfolio.model.Movie;
import com.udacity.myappportfolio.model.MovieMainBean;
import com.udacity.myappportfolio.net.MovieDBResponseListener;
import com.udacity.myappportfolio.net.TheMovieDBClient;
import com.udacity.myappportfolio.view.MovieMainView;

import java.util.ArrayList;

/**
 * Created by 587823 on 2/15/2016.
 */
public class PopularMoviePresenterImpl implements PopularMoviePresenter, MovieDBResponseListener {

    private MovieMainView movieMainView;
    private TheMovieDBClient interactor ;

    public PopularMoviePresenterImpl(MovieMainView movieMainView) {
        this.movieMainView = movieMainView;
        interactor = TheMovieDBClient.getInstance();
    }

    @Override
    public void fetchMovieList(int pageNo , String sortBy ) {

        if (movieMainView.getMovieList().size() > 0) {
                movieMainView.showListProgress();
            } else {
                movieMainView.showProgress();
            }

        interactor.loadMovies(pageNo,sortBy, this);
    }

    @Override
    public ArrayList<Movie> fetchMovieFromDb(Activity context) {

            movieMainView.showProgress();

        return interactor.loadMovieFromDb(context);
    }

    @Override
    public void onDestroy() {
        movieMainView = null;
    }

    @Override
    public void onSuccess(MovieMainBean bean) {
        movieMainView.onSuccess(bean);
    }

    @Override
    public void onFailure(Throwable t) {
        movieMainView.onFailure(t);
    }
}
