package com.udacity.myappportfolio.db;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class FavMovieContract {
    public static final String DB_NAME = "com.udacity.myappportfolio.favMovies";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "favMovies";
    public static final String AUTHORITY = "com.udacity.myappportfolio.myFavMovies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final int MOVIES_LIST = 1;
    public static final int MOVIE_ITEM = 2;
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/udacity.favMoviesDB/"+TABLE;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/example/favMoviesDB" + TABLE;


    public class Columns {
        public static final String MOVIE_ID = "movie_id";
        public static final String MOVIE_TITLE = "movie_title";
        public static final String MOVIE_RELEASE_DATE = "movie_release_date";
        public static final String MOVIE_RATING = "movie_rating";
        public static final String MOVIE_SYNOPSIS = "movie_synopsis";
        public static final String MOVIE_POSTER_URL = "movie_poster_url";
        public static final String _ID = BaseColumns._ID;
    }
}
