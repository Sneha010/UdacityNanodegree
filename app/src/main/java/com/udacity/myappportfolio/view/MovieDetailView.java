package com.udacity.myappportfolio.view;


import com.udacity.myappportfolio.model.ReviewMainBean;
import com.udacity.myappportfolio.model.TrailerMainBean;

public interface MovieDetailView {

    void onReviewSuccess(ReviewMainBean bean);

    void onReviewFailure(Throwable t);

    void onTrailerSuccess(TrailerMainBean bean);

    void onTrailerFailure(Throwable t);
}
