package com.udacity.myappportfolio;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
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

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                            applyPallateToWindow(palette);
                        }

                    }
                });


        LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(0)), Color.LTGRAY);   // Empty star
        //DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(1)), Color.BLUE); // Partial star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(2)), getResources().getColor(R.color.colorAccent));  // Full star

        if(MyUtil.notEmpty(movie.getOriginal_title())){
            collapsingToolbarLayout.setTitle(movie.getOriginal_title());
        }else
            collapsingToolbarLayout.setTitle(getResources().getString(R.string.not_available));

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

    //Apply toolbar status and navigation color from palatte
    public void applyPallateToWindow(Palette palette){

        int colorPrimary = getResources().getColor(R.color.colorPrimary);
        int colorPrimaryDark = getResources().getColor(R.color.colorPrimaryDark);


        if(palette.getDarkMutedSwatch()!=null){

            colorPrimary = palette.getDarkMutedSwatch().getRgb();
            colorPrimaryDark = colorPrimary;

            float[] hsv = new float[3];
            Color.colorToHSV(colorPrimaryDark, hsv);
            hsv[2] *= 1.5f; // value component
            colorPrimaryDark = Color.HSVToColor(hsv);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(colorPrimary);
            window.setNavigationBarColor(colorPrimary);

            collapsingToolbarLayout.setContentScrimColor(colorPrimaryDark);


        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            finish();

        } catch (Exception e) {
        }
        return true;
    }



}
