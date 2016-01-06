package com.udacity.myappportfolio;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.udacity.myappportfolio.adapter.MovieRecyclerViewAdapter;
import com.udacity.myappportfolio.bean.Movie;
import com.udacity.myappportfolio.bean.MovieMainBean;
import com.udacity.myappportfolio.interfaces.MovieRestAPI;
import com.udacity.myappportfolio.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by 587823 on 1/1/2016.
 */
public class PopularMoviesMainActivity extends BaseActivity {

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

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.progress_pv_circular_colors)
    ProgressView listProgressView;

    boolean isRefresh = false;
    boolean setSortByPopularityChecked = true;
    Call<MovieMainBean> call;

    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private GridLayoutManager mLayoutManager;
    private MovieRestAPI myService;
    private String sort_by = Constants.POPULARITY;
    private int page = 1;
    private ArrayList<Movie> dynamicResultList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_movies_main_layout);
        ButterKnife.bind(this);


        prepareUI();
        setUpRetrofit();
        executeRetrofittask();
    }

    public void prepareUI(){

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name_proj2));

        setUpGridListView();

        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                page = 1;
                executeRetrofittask();
            }
        });

        lv_gridList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    Log.d("@@@" , "visibleItemCount = "+visibleItemCount+" totalItemCount = "+totalItemCount+" pastVisiblesItems = "+pastVisiblesItems);

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            //Do pagination.. i.e. fetch new data
                            executeRetrofittask();
                        }
                    }
                }

            }
        });

    }
    public void setUpGridListView(){
        mLayoutManager = new GridLayoutManager(PopularMoviesMainActivity.this , 2);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_gridList.setLayoutManager(mLayoutManager);
    }

    public void setUpRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myService = retrofit.create(MovieRestAPI.class);
    }

    public void executeRetrofittask(){
        if(dynamicResultList.size() > 0){
            showListProgress();
        }else{
            showProgress();
        }

        call = myService.loadMovies(sort_by , Constants.API_KEY , page+"");
        call.enqueue(new Callback<MovieMainBean>() {
            @Override
            public void onResponse(Response<MovieMainBean> response,
                                   Retrofit retrofit) {

                MovieMainBean bean = response.body();

                if(bean!=null){
                    if(bean.getResults()!=null && bean.getResults().size()>0){
                        page++;
                        loading = true;
                        dynamicResultList.addAll(bean.getResults());
                        displayMovieList(dynamicResultList);
                    }else{
                        showError(getResources().getString(R.string.no_results));
                    }
                }
                else{
                    showError(getResources().getString(R.string.no_results));
                }

            }

            @Override
            public void onFailure(Throwable t) {
                showError(getResources().getString(R.string.server_error));
            }
        });
    }


    public void displayMovieList(List<Movie> movieBeanList) {
            MovieRecyclerViewAdapter newsListViewAdapter = new MovieRecyclerViewAdapter(
                    PopularMoviesMainActivity.this, movieBeanList);
            showGrid();
            newsListViewAdapter.notifyDataSetChanged();
            lv_gridList.setItemAnimator(new FadeInAnimator());
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(newsListViewAdapter);
            ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            lv_gridList.setAdapter(scaleAdapter);
    }

    public void showProgress(){
        rl_progress.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        rl_error.setVisibility(View.GONE);
    }

    public void showListProgress(){
        rl_progress.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        rl_error.setVisibility(View.GONE);
        listProgressView.setVisibility(View.VISIBLE);
    }
    public void showGrid(){
        rl_progress.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
    }
    public void showError(String message){
        rl_progress.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        rl_error.setVisibility(View.VISIBLE);
        tv_errorMessage.setText(message);

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

    public void showPopUpMenu(View menuItemAnchor){
        PopupMenu popup = new PopupMenu(this,
                menuItemAnchor);
        popup.getMenuInflater().inflate(R.menu.radio_menu, popup.getMenu());

        MenuItem item_popularity= popup.getMenu().findItem(R.id.sort_by_popularity);
        MenuItem item_voting= popup.getMenu().findItem(R.id.sort_by_voting);

        if(setSortByPopularityChecked){
            item_popularity.setChecked(true);
        }else{
            item_voting.setChecked(false);
        }

        PopUpMenuEventHandle popUpMenuEventHandle=new PopUpMenuEventHandle(PopularMoviesMainActivity.this);
        popup.setOnMenuItemClickListener(popUpMenuEventHandle);
        popup.show();
    }

    public class PopUpMenuEventHandle implements PopupMenu.OnMenuItemClickListener {
        Context context;
        public PopUpMenuEventHandle(Context context){
            this.context =context;
        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (!setSortByPopularityChecked && item.getItemId()==R.id.sort_by_popularity) {
                setSortByPopularityChecked = true;
                sort_by = Constants.POPULARITY;
                page = 1;
                dynamicResultList.clear();
                executeRetrofittask();
                return true;
            }
            if (setSortByPopularityChecked && item.getItemId()==R.id.sort_by_voting){
                setSortByPopularityChecked = false;
                sort_by = Constants.VOTE_AVERAGE;
                page = 1;
                dynamicResultList.clear();
                executeRetrofittask();
                return true;
            }
            return false;
        }
    }
}
