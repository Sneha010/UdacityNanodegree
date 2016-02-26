package com.udacity.myappportfolio.presenter;

/**
 * Created by 587823 on 2/15/2016.
 */
public interface PopularMoviePresenter {

    void loadMovieList( int pageNo ,String sortBy);


    void onDestroy();

}
