package com.udacity.myappportfolio;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.myappportfolio.bean.Movie;
import com.udacity.myappportfolio.util.Constants;
import com.udacity.myappportfolio.util.PaletteTransformation;

import java.util.Collections;
import java.util.Comparator;

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

    @Bind(R.id.vibrant)
    View vibrant;
    @Bind(R.id.vibrantDark)
    View vibrantDark;
    @Bind(R.id.vibrantLight)
    View vibrantLight;
    @Bind(R.id.muted)
    View muted;
    @Bind(R.id.mutedDark)
    View mutedDark;
    @Bind(R.id.mutedLight)
    View mutedLight;

    public void prepareUI(){

       /* if (MyUtil.notEmpty(movie.getPoster_path())) {
            Picasso.with(MovieDetailActivity.this)
                    .load(Constants.IMAGE_POSTER_PATH_BASE_URL+movie.getPoster_path())
                    .into(iv_movie_poster);
        }*/


        Picasso.with(MovieDetailActivity.this)
                .load(Constants.IMAGE_POSTER_PATH_BASE_URL + movie.getPoster_path())
                .transform(new PaletteTransformation())
                .into(iv_movie_poster, new Callback.EmptyCallback() {
                    @Override public void onSuccess() {
                        // TODO I can haz Palette?
                        Bitmap bitmap = ((BitmapDrawable) iv_movie_poster.getDrawable()).getBitmap(); // Ew!
                        Palette palette = PaletteTransformation.getPalette(bitmap);
                        if(palette!=null){
                            if(palette.getVibrantSwatch()!=null)
                                vibrant.setBackgroundColor(palette.getVibrantSwatch().getRgb());
                            if(palette.getDarkVibrantSwatch()!=null)
                                vibrantDark.setBackgroundColor(palette.getDarkVibrantSwatch().getRgb());
                            if(palette.getLightVibrantSwatch()!=null)
                                vibrantLight.setBackgroundColor(palette.getLightVibrantSwatch().getRgb());
                            if(palette.getMutedSwatch()!=null)
                                muted.setBackgroundColor(palette.getMutedSwatch().getRgb());
                            if(palette.getDarkMutedSwatch()!=null)
                                mutedDark.setBackgroundColor(palette.getDarkMutedSwatch().getRgb());
                            if(palette.getLightMutedSwatch()!=null)
                                mutedLight.setBackgroundColor(palette.getLightMutedSwatch().getRgb());
                        }
                        /*
                        Its not necessary that Palette will always return VibrantSwatch. Check for null before calling getRgb()
                        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                        if(vibrantSwatch != null) {
                            //get rgb
                        } else {
                            //get another swatch
                        }
                        You can also get all swatches available from Palette using getSwatches().
                        Then, to get most used color, pick the swatch having maximum pixel population.
                         You can get pixel population by calling getPopulation() on swatches*/

                    }
                });


    }

    public static Palette.Swatch getDominantSwatch(Palette palette) {
        // find most-represented swatch based on population
        return Collections.max(palette.getSwatches(), new Comparator<Palette.Swatch>() {
            @Override
            public int compare(Palette.Swatch sw1, Palette.Swatch sw2) {
                return Integer.compare(sw1.getPopulation(), sw2.getPopulation());
            }
        });
    }
}
