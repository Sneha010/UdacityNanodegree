package com.udacity.myappportfolio;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.myappportfolio.bean.Movie;
import com.udacity.myappportfolio.util.Constants;
import com.udacity.myappportfolio.util.MyUtil;
import com.udacity.myappportfolio.util.PaletteTransformation;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HP on 1/17/2016.
 */
public class MovieDetailActivity extends BaseActivity {

    @Bind(R.id.iv_movie_poster)
    ImageView iv_movie_poster;

    @Bind(R.id.ratingBar)
    RatingBar ratingBar;

    @Bind(R.id.tv_VoteCount)
    TextView tv_VoteCount;

    @Bind(R.id.tv_ReleaseDateValue)
    TextView tv_ReleaseDateValue;

    @Bind(R.id.tv_SynopsisValue)
    TextView tv_SynopsisValue;

    Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_main_layout);
        ButterKnife.bind(this);

        if(getIntent()!=null){
            movie = getIntent().getParcelableExtra("movieBean");
            fillUI();
        }
    }


    public void fillUI(){

        Picasso.with(MovieDetailActivity.this)
                .load(Constants.IMAGE_POSTER_PATH_BASE_URL + movie.getPoster_path())
                .transform(new PaletteTransformation())
                .into(iv_movie_poster, new Callback.EmptyCallback() {
                    @Override public void onSuccess() {
                        // TODO I can haz Palette?
                        Bitmap bitmap = ((BitmapDrawable) iv_movie_poster.getDrawable()).getBitmap(); // Ew!
                        Palette palette = PaletteTransformation.getPalette(bitmap);
                        if(palette!=null){

                        }

                    }
                });


        LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(0)), Color.LTGRAY);   // Empty star
        //DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(1)), Color.BLUE); // Partial star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(2)), getResources().getColor(R.color.colorAccent));  // Full star

        if(movie.getVote_count()!=0)
        {
            tv_VoteCount.setText(movie.getVote_count()+" Votes");
        }

        if(MyUtil.notEmpty(movie.getRelease_date())){
            tv_ReleaseDateValue.setText(movie.getRelease_date());
        }

        if(MyUtil.notEmpty(movie.getOverview())){
            tv_SynopsisValue.setText(movie.getOverview());
        }

        fillRatingStars();

    }

    public void fillRatingStars(){

        if(movie.getVote_average()!= 0.0){
            double vote_avg = movie.getVote_average();
            float final_rating = (float) vote_avg/2.0f;

            ratingBar.setRating(final_rating);
        }


    }


    }