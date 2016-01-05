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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.udacity.myappportfolio.adapter.MovieRecyclerViewAdapter;
import com.udacity.myappportfolio.bean.Movie;
import com.udacity.myappportfolio.bean.MovieMainBean;
import com.udacity.myappportfolio.interfaces.MovieRestAPI;
import com.udacity.myappportfolio.util.Constants;

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
public class PopularMoviesMainActivity extends BaseActivity{

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

    @Bind(R.id.rl_gridview)
    RelativeLayout rl_gridview;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    boolean isRefresh = false;
    boolean setSortByPopularityChecked = true;
    Call<MovieMainBean> call;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_movies_main_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name_proj2));

        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                executeRetrofittask();
            }
        });

        showProgress();
        setUpGridListView();
        setUpRetrofit();
        executeRetrofittask();
    }

    public void setUpGridListView(){
        GridLayoutManager llm = new GridLayoutManager(PopularMoviesMainActivity.this , 2);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lv_gridList.setLayoutManager(llm);
    }

    public void setUpRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieRestAPI myService = retrofit.create(MovieRestAPI.class);
        call = myService.loadMovies("popularity.desc" , "c828d18e73df20d04a0a762f7a126d29" , "1");



    }

    public void executeRetrofittask(){
        call.enqueue(new Callback<MovieMainBean>() {
            @Override
            public void onResponse(Response<MovieMainBean> response,
                                   Retrofit retrofit) {

                MovieMainBean bean = response.body();

                if(bean!=null){
                    if(bean.getResults()!=null && bean.getResults().size()>0){
                        displayMovieList(bean.getResults());
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
            rl_gridview.setVisibility(View.VISIBLE);
            showGrid();
            newsListViewAdapter.notifyDataSetChanged();
            lv_gridList.setItemAnimator(new FadeInAnimator());
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(newsListViewAdapter);
            ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            lv_gridList.setAdapter(scaleAdapter);
    }

    public void showProgress(){
        rl_progress.setVisibility(View.VISIBLE);
        rl_gridview.setVisibility(View.GONE);
        rl_error.setVisibility(View.GONE);
    }
    public void showGrid(){
        rl_progress.setVisibility(View.GONE);
        rl_gridview.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
    }
    public void showError(String message){
        rl_progress.setVisibility(View.GONE);
        rl_gridview.setVisibility(View.GONE);
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
            if (item.getItemId()==R.id.sort_by_popularity) {
                setSortByPopularityChecked = true;
                return true;
            }
            if (item.getItemId()==R.id.sort_by_voting){
                setSortByPopularityChecked = false;
                return true;
            }
            return false;
        }
    }
}
