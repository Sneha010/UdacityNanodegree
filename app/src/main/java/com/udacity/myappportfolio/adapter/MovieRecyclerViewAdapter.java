package com.udacity.myappportfolio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.myappportfolio.R;
import com.udacity.myappportfolio.bean.Movie;
import com.udacity.myappportfolio.util.Constants;
import com.udacity.myappportfolio.util.MyUtil;

import java.util.List;

/**
 * Created by HP on 1/4/2016.
 */
public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> implements RecyclerView.OnItemTouchListener{

    private List<Movie> movieList;
    private Context context;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {
        Movie bean = movieList.get(i);

        if (bean != null) {
            if (MyUtil.notEmpty(bean.getPoster_path())) {
                Picasso.with(context).load(Constants.IMAGE_POSTER_PATH_BASE_URL+bean.getPoster_path()).into(movieViewHolder.iv_movie_poster);
            }

        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.movie_grid_layout, viewGroup, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_movie_poster;

        public MovieViewHolder(View v) {
            super(v);
            iv_movie_poster = (ImageView) v.findViewById(R.id.iv_movie_poster);
        }
    }

    public void updateMovieList(List<Movie> newMovieList){
        this.movieList.addAll(newMovieList);
        notifyDataSetChanged();
    }


}
