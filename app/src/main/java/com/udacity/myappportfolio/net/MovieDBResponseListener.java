package com.udacity.myappportfolio.net;

import com.udacity.myappportfolio.model.MovieMainBean;


public interface MovieDBResponseListener {

    void onSuccess(MovieMainBean bean);
    void onFailure(Throwable throwable);
}
