package com.udacity.myappportfolio.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDBHelper extends SQLiteOpenHelper {

	public MovieDBHelper(Context context) {
		super(context, FavMovieContract.DB_NAME, null, FavMovieContract.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqlDB) {
		sqlDB.execSQL("CREATE TABLE "
				+ FavMovieContract.TABLE
				+ " ( "+FavMovieContract.Columns.MOVIE_ID+" INT PRIMARY KEY," +
				FavMovieContract.Columns.MOVIE_TITLE +" TEXT ,"+
				FavMovieContract.Columns.MOVIE_RELEASE_DATE +" TEXT ,"+
				FavMovieContract.Columns.MOVIE_RATING +" INT ,"+
				FavMovieContract.Columns.MOVIE_SYNOPSIS +" TEXT ,"+
				FavMovieContract.Columns.MOVIE_POSTER_URL +" TEXT )");

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
		sqlDB.execSQL("DROP TABLE IF EXISTS "+FavMovieContract.TABLE);
		onCreate(sqlDB);
	}
}
