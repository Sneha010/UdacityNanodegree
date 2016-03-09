package com.udacity.myappportfolio.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.myappportfolio.R;
import com.udacity.myappportfolio.adapter.ReviewAdapter;
import com.udacity.myappportfolio.adapter.TrailerAdapter;
import com.udacity.myappportfolio.model.Movie;
import com.udacity.myappportfolio.model.ReviewMainBean;
import com.udacity.myappportfolio.model.TrailerMainBean;
import com.udacity.myappportfolio.net.ReviewResponseListener;
import com.udacity.myappportfolio.net.TheMovieDBClient;
import com.udacity.myappportfolio.net.TrailerResponseListener;
import com.udacity.myappportfolio.util.Constants;
import com.udacity.myappportfolio.util.MyUtil;
import com.udacity.myappportfolio.util.PaletteTransformation;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailFragment extends BaseFragment implements TrailerResponseListener,ReviewResponseListener{


    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.iv_movie_poster)
    ImageView iv_movie_poster;

    @Bind(R.id.ratingBar)
    RatingBar ratingBar;

    @Bind(R.id.rl_RatingBar)
    RelativeLayout rl_RatingBar;

    @Bind(R.id.tv_VoteCount)
    TextView tv_VoteCount;

    @Bind(R.id.tv_ReleaseDateValue)
    TextView tv_ReleaseDateValue;

    @Bind(R.id.tv_SynopsisValue)
    TextView tv_SynopsisValue;

    @Bind(R.id.tv_movieTitle)
    TextView tv_movieTitle;

    @Bind(R.id.reviewsList)
    RecyclerView reviewRecyclerView;

    @Bind(R.id.trailersList)
    RecyclerView trailerRecyclerView;

    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    Movie movie;
    private TheMovieDBClient client;

    public static MovieDetailFragment getInstance(Movie movie){
        MovieDetailFragment frag = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("movieBean" , movie );
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

         if (getArguments() != null) {
            movie = getArguments().getParcelable("movieBean");

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_detail_layout, container, false);
        ButterKnife.bind(this, view);
        fillUI();

        if(movie != null){
            loadTrailerList(movie.getId());
            loadReviewList(movie.getId());
        }else{
            // make visiblity gone for trailer and reviews views

        }
        return view;
    }

    private void loadTrailerList(int id){

        client = TheMovieDBClient.getInstance("/movie/"+id);
        client.loadTrailers(this);

    }

    private void loadReviewList(int id){

        client = TheMovieDBClient.getInstance("/movie/"+id);
        client.loadReviews(this);

    }

    private void fillUI() {

        Picasso.with(getActivity())
                .load(Constants.IMAGE_POSTER_PATH_BASE_URL + movie.getPoster_path())
                .placeholder(R.drawable.movie_grid_plpaceholder)
                .transform(new PaletteTransformation())
                .into(iv_movie_poster, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        // TODO I can haz Palette?
                        Bitmap bitmap = ((BitmapDrawable) iv_movie_poster.getDrawable()).getBitmap(); // Ew!
                        Palette palette = PaletteTransformation.getPalette(bitmap);
                        if (palette != null) {
                            //applyPalleteToWindow(palette);
                        }

                    }
                });


        LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(0)), Color.LTGRAY);   // Empty star
        //DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(1)), Color.BLUE); // Partial star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(2)), getResources().getColor(R.color.colorAccent));  // Full star

        if (MyUtil.notEmpty(movie.getOriginal_title())) {
            collapsingToolbarLayout.setTitle(movie.getOriginal_title());
        } else
            collapsingToolbarLayout.setTitle(getResources().getString(R.string.not_available));

        if (MyUtil.notEmpty(movie.getOriginal_title())) {
            tv_movieTitle.setText(movie.getOriginal_title());
        } else
            tv_movieTitle.setText(getResources().getString(R.string.not_available));

        if (movie.getVote_count() != 0) {
            tv_VoteCount.setText(movie.getVote_count() + " Votes");
        }

        if (MyUtil.notEmpty(movie.getRelease_date())) {
            tv_ReleaseDateValue.setText(movie.getRelease_date());
        }

        if (MyUtil.notEmpty(movie.getOverview())) {
            tv_SynopsisValue.setText(movie.getOverview());
        }

        fillRatingStars();

    }

    private void fillRatingStars() {

        if (movie.getVote_average() != 0.0) {
            double vote_avg = movie.getVote_average();
            float final_rating = (float) vote_avg / 2.0f;

            ratingBar.setRating(final_rating);
        }


    }

    private void setGradientView(int colorCode) {

        int colors[] = {colorCode, Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF")};

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors);

        rl_RatingBar.setBackgroundDrawable(gradientDrawable);


    }


    @Override
    public void onReviewSuccess(ReviewMainBean bean) {

        if(bean!=null){
            reviewAdapter = new ReviewAdapter(getActivity(), bean.getResults());
            reviewRecyclerView.setAdapter(reviewAdapter);
        }

    }

    @Override
    public void onReviewFailure(Throwable t) {

    }

    @Override
    public void onTrailerSuccess(TrailerMainBean bean) {
        if(bean!=null){
            trailerAdapter = new TrailerAdapter(getActivity(), bean.getResults());
            trailerRecyclerView.setAdapter(trailerAdapter);
        }
    }

    @Override
    public void onTrailerFailure(Throwable t) {

    }
    //Apply toolbar status and navigation color from palatte
    private void applyPalleteToWindow(Palette palette) {


        //Default colors for window
        int colorPrimary = getResources().getColor(R.color.colorPrimary);
        int colorPrimaryDark = getResources().getColor(R.color.colorPrimaryDark);


        if (palette.getDarkMutedSwatch() != null) {

            colorPrimaryDark = palette.getDarkMutedSwatch().getRgb();
            colorPrimary = colorPrimaryDark;

            float[] hsv = new float[3];
            Color.colorToHSV(colorPrimaryDark, hsv);
            hsv[2] *= 1.5f;
            colorPrimary = Color.HSVToColor(hsv);

            setGradientView(colorPrimaryDark);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(colorPrimaryDark);
            window.setNavigationBarColor(colorPrimaryDark);

            collapsingToolbarLayout.setContentScrimColor(colorPrimary);


        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //finish();
        return true;
    }


}

