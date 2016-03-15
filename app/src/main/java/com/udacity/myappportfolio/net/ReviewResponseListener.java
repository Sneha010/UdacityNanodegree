package com.udacity.myappportfolio.net;


import com.udacity.myappportfolio.model.ReviewMainBean;

public interface ReviewResponseListener {

    void onReviewSuccess(ReviewMainBean bean);
    void onReviewFailure(Throwable t);

}
