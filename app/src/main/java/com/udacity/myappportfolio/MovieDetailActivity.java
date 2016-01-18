package com.udacity.myappportfolio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.myappportfolio.bean.Movie;
import com.udacity.myappportfolio.util.Constants;
import com.udacity.myappportfolio.util.MyUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HP on 1/17/2016.
 */
public class MovieDetailActivity extends BaseActivity {

    @Bind(R.id.iv_movie_poster)
    ImageView iv_movie_poster;

    Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_main_layout);
        ButterKnife.bind(this);

        if(getIntent()!=null){
            movie = getIntent().getParcelableExtra("movieBean");
            prepareUI();
        }
    }

    public void prepareUI(){

        if (MyUtil.notEmpty(movie.getPoster_path())) {
            Picasso.with(MovieDetailActivity.this)
                    .load(Constants.IMAGE_POSTER_PATH_BASE_URL+movie.getPoster_path())
                    .into(iv_movie_poster);
        }


    }
}
