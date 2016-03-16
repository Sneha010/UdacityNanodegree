package com.udacity.myappportfolio.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class FavMovieProvider extends ContentProvider{

    private SQLiteDatabase db;
    private MovieDBHelper movieDBHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(FavMovieContract.AUTHORITY,FavMovieContract.TABLE,FavMovieContract.MOVIES_LIST);
        uriMatcher.addURI(FavMovieContract.AUTHORITY,FavMovieContract.TABLE+"/#",FavMovieContract.MOVIE_ITEM);
    }

    @Override
    public boolean onCreate() {
        boolean ret = true;
        movieDBHelper = new MovieDBHelper(getContext());
        db = movieDBHelper.getWritableDatabase();

        if (db == null) {
            ret = false;
        }

        if (db.isReadOnly()) {
            db.close();
            db = null;
            ret = false;
        }

        return ret;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(FavMovieContract.TABLE);

        switch (uriMatcher.match(uri)) {
            case FavMovieContract.MOVIES_LIST:
                break;

            case FavMovieContract.MOVIE_ITEM:
                qb.appendWhere(FavMovieContract.Columns._ID + " = "+ uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        Cursor cursor = qb.query(db,projection,selection,selectionArgs,null,null,null);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case FavMovieContract.MOVIES_LIST:
                return FavMovieContract.CONTENT_TYPE;

            case FavMovieContract.MOVIE_ITEM:
                return FavMovieContract.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Invalid URI: "+uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        if (uriMatcher.match(uri) != FavMovieContract.MOVIES_LIST) {
            throw new IllegalArgumentException("Invalid URI: "+uri);
        }

        long id = db.insert(FavMovieContract.TABLE,null,contentValues);

        if (id>0) {
            return ContentUris.withAppendedId(uri, id);
        }
        throw new SQLException("Error inserting into table: "+FavMovieContract.TABLE);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int deleted = 0;

        switch (uriMatcher.match(uri)) {
            case FavMovieContract.MOVIES_LIST:
                db.delete(FavMovieContract.TABLE,selection,selectionArgs);
                break;

            case FavMovieContract.MOVIE_ITEM:
                String where = FavMovieContract.Columns._ID + " = " + uri.getLastPathSegment();
                if (!selection.isEmpty()) {
                    where += " AND "+selection;
                }

                deleted = db.delete(FavMovieContract.TABLE,where,selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: "+uri);
        }

        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        int updated = 0;

        switch (uriMatcher.match(uri)) {
            case FavMovieContract.MOVIES_LIST:
                db.update(FavMovieContract.TABLE,contentValues,s,strings);
                break;

            case FavMovieContract.MOVIE_ITEM:
                String where = FavMovieContract.Columns._ID + " = " + uri.getLastPathSegment();
                if (!s.isEmpty()) {
                    where += " AND "+s;
                }
                updated = db.update(FavMovieContract.TABLE,contentValues,where,strings);
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: "+uri);
        }

        return updated;
    }
}
