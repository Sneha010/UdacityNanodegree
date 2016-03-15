package com.udacity.myappportfolio.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.udacity.myappportfolio.R;
import com.udacity.myappportfolio.fragment.MovieDetailFragment;
import com.udacity.myappportfolio.fragment.MovieListFragment;
import com.udacity.myappportfolio.model.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MoviesMainActivity extends BaseActivity implements MovieListFragment.OnItemSelectedListener{

    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_main_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name_proj2));

        displayMovieList();

    }

    private void displayMovieList(){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MovieListFragment listFrament = MovieListFragment.getInstance();

        if(getResources().getBoolean(R.bool.isTablet)){
            ft.add(R.id.rl_listView, listFrament);
            ft.commit();
        }else{
            ft.add(R.id.rl_listView, listFrament,"HOME");
            ft.addToBackStack("listFragment");
            ft.commit();
        }
    }

    @Override
    public void itemSelected(Movie movie) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(getResources().getBoolean(R.bool.isTablet)){
            ft.replace(R.id.rl_detailView, MovieDetailFragment.getInstance(movie));
            ft.commit();
        }else{
            ft.replace(R.id.rl_listView, MovieDetailFragment.getInstance(movie),"DETAILS");
            ft.addToBackStack("listFragment");
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.rl_listView);

        if (fragment instanceof MovieListFragment) {
            supportFinishAfterTransition();
        }else {
            super.onBackPressed();
        }

    }
}
