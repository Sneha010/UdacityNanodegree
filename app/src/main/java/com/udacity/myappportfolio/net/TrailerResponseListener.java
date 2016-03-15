package com.udacity.myappportfolio.net;


import com.udacity.myappportfolio.model.TrailerMainBean;

public interface TrailerResponseListener {
    void onTrailerSuccess(TrailerMainBean bean);
    void onTrailerFailure(Throwable t);
}
