package com.udacity.myappportfolio.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.ProgressView;
import com.udacity.myappportfolio.BaseActivity;
import com.udacity.myappportfolio.MovieDetailActivity;
import com.udacity.myappportfolio.R;
import com.udacity.myappportfolio.adapter.MovieRecyclerViewAdapter;
import com.udacity.myappportfolio.model.Movie;
import com.udacity.myappportfolio.model.MovieMainBean;
import com.udacity.myappportfolio.net.MovieRestAPI;
import com.udacity.myappportfolio.presenter.PopularMoviePresenter;
import com.udacity.myappportfolio.presenter.PopularMoviePresenterImpl;
import com.udacity.myappportfolio.util.Constants;
import com.udacity.myappportfolio.util.MyUtil;
import com.udacity.myappportfolio.util.RecyclerItemClickListener;
import com.udacity.myappportfolio.view.MovieMainView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by 587823 on 1/1/2016.
 */
public class PopularMoviesMainActivityNew extends BaseActivity implements MovieMainView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.list)
    RecyclerView lv_gridList;

    @Bind(R.id.rl_error)
    RelativeLayout rl_error;

    @Bind(R.id.rl_progress)
    RelativeLayout rl_progress;

    @Bind(R.id.tv_errorMessage)
    TextView tv_errorMessage;

    @Bind(R.id.rl_gridView)
    RelativeLayout rl_gridView;

    @Bind(R.id.progress_pv_circular_colors)
    ProgressView listProgressView;

    private boolean setSortByPopularityChecked = true;

    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private GridLayoutManager mLayoutManager;
    private MovieRestAPI myService;
    private String sortBy = Constants.POPULARITY;
    private int pageNo = 1;
    private List<Movie> dynamicResultList = new ArrayList<>();
    private MovieRecyclerViewAdapter newsListViewAdapter;


    private PopularMoviePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_list_layout);
        ButterKnife.bind(this);

        presenter = new PopularMoviePresenterImpl(this);

        prepareUI();
    }


    private void prepareUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name_proj2));
        setUpGridListView();
        setUpListeners();

        presenter.loadMovieList(getPageNo() , getSortBy());

    }

    @SuppressWarnings("unused")
    @OnClick(R.id.rl_error)
    public void fetchMovieAgain() {
        setPageNo(1);
        clearMovieList();
        presenter.loadMovieList(getPageNo() , getSortBy());
    }


    private void setUpListeners() {

        lv_gridList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    Log.d("@@@", "visibleItemCount = " + visibleItemCount + " totalItemCount = " + totalItemCount + " pastVisiblesItems = " + pastVisiblesItems);

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            //Do pagination.. i.e. fetch new data
                            presenter.loadMovieList(getPageNo() , getSortBy());
                        }
                    }
                }

            }
        });

        lv_gridList.addOnItemTouchListener(
                new RecyclerItemClickListener(PopularMoviesMainActivityNew.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        if (MyUtil.notEmpty(dynamicResultList)) {
                            Intent i = new Intent(PopularMoviesMainActivityNew.this, MovieDetailActivity.class);
                            i.putExtra("movieBean", dynamicResultList.get(position));
                            startActivity(i);
                        }
                    }
                })
        );
    }

    private void setUpGridListView() {
        lv_gridList.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(PopularMoviesMainActivityNew.this, 2);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_gridList.setLayoutManager(mLayoutManager);
    }




    public void displayMovieList(List<Movie> movieBeanList) {
        if (getPageNo() == 2) {
            newsListViewAdapter = new MovieRecyclerViewAdapter(
                    PopularMoviesMainActivityNew.this, movieBeanList);
            lv_gridList.setItemAnimator(new FadeInAnimator());
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(newsListViewAdapter);
            ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            lv_gridList.setAdapter(scaleAdapter);
        } else {
            newsListViewAdapter.updateMovieList(movieBeanList);
        }
    }

    public void clearMovieList() {
        dynamicResultList.clear();
    }

    @Override
    public void showProgress() {
        rl_progress.setVisibility(View.VISIBLE);
        rl_gridView.setVisibility(View.GONE);
        rl_error.setVisibility(View.GONE);
        listProgressView.setVisibility(View.GONE);
    }

    @Override
    public void showListProgress() {
        rl_progress.setVisibility(View.GONE);
        rl_gridView.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
        listProgressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showGrid() {
        rl_progress.setVisibility(View.GONE);
        rl_gridView.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
        listProgressView.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {

        loading = true;

        rl_progress.setVisibility(View.GONE);
        listProgressView.setVisibility(View.GONE);

        if (dynamicResultList != null && dynamicResultList.size() > 0) {

            MyUtil.showSnackbar(rl_gridView , message);

        } else {
            rl_gridView.setVisibility(View.GONE);
            rl_error.setVisibility(View.VISIBLE);
            tv_errorMessage.setText(message);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.sort_movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.action_sort:
                showPopUpMenu((View) findViewById(R.id.action_sort));
                break;
            default:

        }
        return true;
    }

    public void showPopUpMenu(View menuItemAnchor) {
        PopupMenu popup = new PopupMenu(this,
                menuItemAnchor);
        popup.getMenuInflater().inflate(R.menu.radio_menu, popup.getMenu());

        MenuItem item_popularity = popup.getMenu().findItem(R.id.sort_by_popularity);
        MenuItem item_voting = popup.getMenu().findItem(R.id.sort_by_voting);

        if (setSortByPopularityChecked) {
            item_popularity.setChecked(true);
        } else {
            item_voting.setChecked(false);
        }

        PopUpMenuEventHandle popUpMenuEventHandle = new PopUpMenuEventHandle(PopularMoviesMainActivityNew.this);
        popup.setOnMenuItemClickListener(popUpMenuEventHandle);
        popup.show();
    }

    @Override
    public List<Movie> getMovieList() {
        return dynamicResultList;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo){
        this.pageNo = pageNo;
    }

    public String getSortBy(){
        return sortBy;
    }

    public void setSortBy(String sortBy){
        this.sortBy = sortBy;
    }


    public void isLoading(boolean isloading) {
        loading = isloading;
    }

    @Override
    public void onSuccess(MovieMainBean bean) {
        if (bean != null) {

            if (bean.getResults() != null && bean.getResults().size() > 0) {
                int page = getPageNo();
                page++;
                setPageNo(page);
                isLoading(true);
                getMovieList().addAll(bean.getResults());
                displayMovieList(bean.getResults());
                showGrid();
            } else {
                showError(getResources().getString(R.string.no_results));
            }
        } else {
            showError(getResources().getString(R.string.no_results));
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if(t instanceof IOException)
            showError(getResources().getString(R.string.no_internet));
        else
            showError(getResources().getString(R.string.server_error));
    }


    public class PopUpMenuEventHandle implements PopupMenu.OnMenuItemClickListener {
        Context context;

        public PopUpMenuEventHandle(Context context) {
            this.context = context;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (!setSortByPopularityChecked && item.getItemId() == R.id.sort_by_popularity) {
                setSortByPopularityChecked = true;
                setSortBy(Constants.POPULARITY);
                setPageNo(1);
                clearMovieList();
                presenter.loadMovieList(getPageNo() , getSortBy());
                return true;
            }
            if (setSortByPopularityChecked && item.getItemId() == R.id.sort_by_voting) {
                setSortByPopularityChecked = false;
                setSortBy(Constants.VOTE_AVERAGE);
                setPageNo(1);
                clearMovieList();
                presenter.loadMovieList(getPageNo() , getSortBy());
                return true;
            }
            return false;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mLayoutManager.setSpanCount(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);


    }
}
