package com.udacity.myappportfolio.net;

import com.udacity.myappportfolio.bean.MovieMainBean;

/**
 * Created by 587823 on 2/2/2016.
 */
public interface MovieDBResponseListener {

    void onSuccess(MovieMainBean bean);
    void onFailure(MovieDBError error);
}
