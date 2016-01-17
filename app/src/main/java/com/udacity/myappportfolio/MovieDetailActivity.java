package com.udacity.myappportfolio;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;

/**
 * Created by HP on 1/17/2016.
 */
public class MovieDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_main_layout);
        ButterKnife.bind(this);


        //prepareUI();
    }
}
