package com.udacity.myappportfolio.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.myappportfolio.R;
import com.udacity.myappportfolio.adapter.ReviewAdapter;
import com.udacity.myappportfolio.adapter.TrailerAdapter;
import com.udacity.myappportfolio.model.Movie;
import com.udacity.myappportfolio.model.ReviewMainBean;
import com.udacity.myappportfolio.model.TrailerMainBean;
import com.udacity.myappportfolio.presenter.ReviewTrailerPresenter;
import com.udacity.myappportfolio.presenter.ReviewTrailerPresenterImpl;
import com.udacity.myappportfolio.util.Constants;
import com.udacity.myappportfolio.util.MyUtil;
import com.udacity.myappportfolio.util.PaletteTransformation;
import com.udacity.myappportfolio.util.WrappingLinearLayoutManager;
import com.udacity.myappportfolio.view.MovieDetailView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailFragment extends BaseFragment implements MovieDetailView {

    private static final String MOVIE_BEAN_KEY = "movieBean";

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

    @Bind(R.id.tv_ReviewLabel)
    TextView tv_ReviewLabel;

    @Bind(R.id.tv_TrailerLabel)
    TextView tv_TrailerLabel;

    @Bind(R.id.reviewsList)
    RecyclerView reviewRecyclerView;

    @Bind(R.id.trailersList)
    RecyclerView trailerRecyclerView;

    @Bind(R.id.iv_fav_icon)
    ImageView iv_fav_icon;

    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    private WrappingLinearLayoutManager reviewLLmanager;
    private WrappingLinearLayoutManager trailerLLmanager;

    private Movie movie;
    boolean isFavorite = false;
    private ReviewTrailerPresenter presenter;

    private TrailerMainBean trailerData;

    public static MovieDetailFragment getInstance(Movie movie) {
        MovieDetailFragment frag = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_BEAN_KEY, movie);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

            movie = getArguments().getParcelable(MOVIE_BEAN_KEY);

        }

        presenter = new ReviewTrailerPresenterImpl(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_detail_layout, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fillUI();

        setUpRecyclerView();

        requestForReviewAndTrailer();

    }



    @SuppressWarnings("unused")
    @OnClick(R.id.iv_fav_icon)
    void favIconSelected() {
        if (isFavorite) {
            if (presenter.removeMovieToDb(getActivity(), movie.getId()) == 0) {
                MyUtil.displayCustomToast(getActivity(), getActivity().getResources().getString(R.string.movie_removed));
                iv_fav_icon.setImageResource(R.drawable.grey_trans_heart);
                isFavorite = false;
            } else {
                MyUtil.displayCustomToast(getActivity(), getActivity().getResources().getString(R.string.error_movie_removed));
                iv_fav_icon.setImageResource(R.drawable.red_heart);
                isFavorite = true;
            }
        } else {
            isFavorite = presenter.addMovieToDb(getActivity(), movie);
            if (isFavorite) {
                MyUtil.displayCustomToast(getActivity(), getActivity().getResources().getString(R.string.movie_added));
                iv_fav_icon.setImageResource(R.drawable.red_heart);
            } else {
                MyUtil.displayCustomToast(getActivity(), getActivity().getResources().getString(R.string.error_movie_added));
            }
        }

    }

    private void requestForReviewAndTrailer() {
        if (movie != null) {
            loadTrailerList(movie.getId());
            loadReviewList(movie.getId());
        } else {
            tv_ReviewLabel.setVisibility(View.GONE);
            reviewRecyclerView.setVisibility(View.GONE);
            tv_TrailerLabel.setVisibility(View.GONE);
            trailerRecyclerView.setVisibility(View.GONE);
        }
    }

    private void setUpRecyclerView() {
        reviewLLmanager = new WrappingLinearLayoutManager(getActivity());
        reviewRecyclerView.setNestedScrollingEnabled(false);
        reviewRecyclerView.setHasFixedSize(false);
        reviewRecyclerView.setLayoutManager(reviewLLmanager);

        trailerLLmanager = new WrappingLinearLayoutManager(getActivity());
        trailerRecyclerView.setNestedScrollingEnabled(false);
        trailerRecyclerView.setHasFixedSize(false);
        trailerRecyclerView.setLayoutManager(trailerLLmanager);


    }

    private void loadTrailerList(int id) {

        presenter.fetchTrailers(id);
    }

    private void loadReviewList(int id) {

        presenter.fetchReviews(id);

    }

    private void fillUI() {

        Picasso.with(getActivity())
                .load(Constants.IMAGE_POSTER_PATH_BASE_URL + movie.getPoster_path())
                .placeholder(R.drawable.movie_grid_plpaceholder)
                .transform(new PaletteTransformation())
                .into(iv_movie_poster);


        LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(0)), Color.LTGRAY);   // Empty star
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
        checkForFavMovie();
    }

    private void checkForFavMovie() {

        isFavorite = presenter.checkForFavMovie(getActivity(), movie.getId());

        if (isFavorite)
            iv_fav_icon.setImageResource(R.drawable.red_heart);
        else
            iv_fav_icon.setImageResource(R.drawable.grey_trans_heart);

        iv_fav_icon.setVisibility(View.VISIBLE);
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
        if (isAdded()) {

            if (bean != null) {
                if (bean.getResults().size() > 0) {
                    reviewAdapter = new ReviewAdapter(getActivity(), bean.getResults(), movie.getOriginal_title());
                    reviewRecyclerView.setAdapter(reviewAdapter);
                    tv_ReviewLabel.setVisibility(View.VISIBLE);
                    reviewRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    tv_ReviewLabel.setVisibility(View.GONE);
                    reviewRecyclerView.setVisibility(View.GONE);
                }

            }

        }

    }

    @Override
    public void onReviewFailure(Throwable t) {
        if (isAdded()) {
            tv_ReviewLabel.setVisibility(View.GONE);
            reviewRecyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onTrailerSuccess(TrailerMainBean bean) {
        if (isAdded()) {
            if (bean != null) {
                if (bean.getResults().size() > 0) {
                    //Saving the trailer data as global for share intent
                    this.trailerData = bean;
                    trailerAdapter = new TrailerAdapter(getActivity(), bean.getResults());
                    trailerRecyclerView.setAdapter(trailerAdapter);
                    tv_TrailerLabel.setVisibility(View.VISIBLE);
                    trailerRecyclerView.setVisibility(View.VISIBLE);

                    updateOptionMenuStatus();

                } else {
                    tv_TrailerLabel.setVisibility(View.GONE);
                    trailerRecyclerView.setVisibility(View.GONE);
                }
            }
        }

    }

    private void updateOptionMenuStatus() {
        getActivity().invalidateOptionsMenu();

    }

    @Override
    public void onTrailerFailure(Throwable t) {
        if (isAdded()) {
            tv_TrailerLabel.setVisibility(View.GONE);
            trailerRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

        if (trailerData != null) {

            menu.findItem(R.id.share_trailer).setVisible(true);

        }

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.share_trailer:
                createShareChooser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void createShareChooser( ) {
        String shareBody = "Watch the trailer for awesome movie : " + movie.getTitle();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, movie.getTitle());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody + "\n" + Constants.YOUTUBE_BASE_URL
                + trailerData.getResults().get(0).getKey());
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }

}

