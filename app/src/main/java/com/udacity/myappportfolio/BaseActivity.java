package com.udacity.myappportfolio;

import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by HP on 1/3/2016.
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
