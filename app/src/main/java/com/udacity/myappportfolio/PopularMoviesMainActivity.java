package com.udacity.myappportfolio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.RelativeLayout;

import com.udacity.myappportfolio.bean.MovieMainBean;
import com.udacity.myappportfolio.interfaces.MovieRestAPI;
import com.udacity.myappportfolio.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by 587823 on 1/1/2016.
 */
public class PopularMoviesMainActivity extends BaseActivity{

    @Bind(R.id.list)
    private RecyclerView lv_gridList;

    @Bind(R.id.rl_error)
    private RelativeLayout rl_error;

    @Bind(R.id.rl_progress)
    private RelativeLayout rl_progress;

    @Bind(R.id.rl_gridview)
    private RelativeLayout rl_gridview;

    @Bind(R.id.swipe_container)
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean isRefresh = false;
    Call<MovieMainBean> call;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_movies_main_layout);
        ButterKnife.bind(this);

        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                executeRetrofittask();
            }
        });

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

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
