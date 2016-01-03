package com.udacity.myappportfolio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;

import com.udacity.myappportfolio.bean.MovieMainBean;
import com.udacity.myappportfolio.interfaces.MovieRestAPI;
import com.udacity.myappportfolio.util.Constants;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_movies_main_layout);
        ButterKnife.bind(this);

        setUpRetrofit();
    }


    public void setUpRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieRestAPI myService = retrofit.create(MovieRestAPI.class);
        Call<MovieMainBean> call = myService.loadMovies("popularity.desc" , "c828d18e73df20d04a0a762f7a126d29" , "1");

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
