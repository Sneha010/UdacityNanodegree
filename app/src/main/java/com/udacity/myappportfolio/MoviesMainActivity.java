package com.udacity.myappportfolio;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.udacity.myappportfolio.fragment.MovieDetailFragment;
import com.udacity.myappportfolio.fragment.MovieListFragment;
import com.udacity.myappportfolio.model.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 587823 on 3/1/2016.
 */
public class MoviesMainActivity extends BaseActivity implements MovieListFragment.OnItemSelectedListener{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.ll_listView)
    LinearLayout ll_listView;

    @Bind(R.id.ll_detailView)
    LinearLayout ll_detailView;

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

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MovieListFragment listFrament = MovieListFragment.getInstance();
        listFrament.setItemSelectlistener(this);
        ft.add(R.id.ll_listView, listFrament);
        ft.commit();

    }

    @Override
    public void itemSelected(Movie movie) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.ll_detailView, MovieDetailFragment.getInstance(movie));
        ft.commit();
    }
}
