package com.udacity.myappportfolio.presenter;


import android.app.Activity;
import android.database.Cursor;

import com.udacity.myappportfolio.model.Movie;
import com.udacity.myappportfolio.model.ReviewMainBean;
import com.udacity.myappportfolio.model.TrailerMainBean;
import com.udacity.myappportfolio.net.ReviewResponseListener;
import com.udacity.myappportfolio.net.TheMovieDBClient;
import com.udacity.myappportfolio.net.TrailerResponseListener;
import com.udacity.myappportfolio.view.MovieDetailView;

public class ReviewTrailerPresenterImpl implements ReviewTrailerPresenter,ReviewResponseListener , TrailerResponseListener{

    private MovieDetailView movieDetailView;
    private TheMovieDBClient interactor ;

    public ReviewTrailerPresenterImpl(MovieDetailView movieDetailView) {
        this.movieDetailView = movieDetailView;
        interactor = TheMovieDBClient.getInstance();
    }

    @Override
    public void fetchReviews(int id) {
        interactor.loadReviews(id, this);
    }

    @Override
    public void fetchTrailers(int id) {
        interactor.loadTrailers(id, this);
    }
    @Override
    public void onReviewSuccess(ReviewMainBean bean) {
        movieDetailView.onReviewSuccess(bean);
    }
    @Override
    public boolean addMovieToDb(Activity context , Movie movie){
        return interactor.insertMovieToDb(context , movie);
    }
    @Override
    public int removeMovieToDb(Activity context , int id){
        return interactor.deleteMovieToDb(context , id);
    }

    @Override
    public boolean checkForFavMovie(Activity context , int id) {

        Cursor cursor = interactor.loadAllFavMovies(context);

        if (cursor.moveToFirst()) {
            do {
                long _id = cursor.getInt(0);

                if(_id == id){
                   return true;
                }

            } while (cursor.moveToNext());
        }
        return false;
    }


    @Override
    public void onReviewFailure(Throwable t) {
        movieDetailView.onReviewFailure(t);
    }

    @Override
    public void onTrailerSuccess(TrailerMainBean bean) {
        movieDetailView.onTrailerSuccess(bean);
    }

    @Override
    public void onTrailerFailure(Throwable t) {
        movieDetailView.onTrailerFailure(t);
    }


}
