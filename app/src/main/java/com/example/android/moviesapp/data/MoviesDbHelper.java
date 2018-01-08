package com.example.android.moviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jman on 24/12/16.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = MoviesDbHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 5;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_FAVORITE_TABLE = "CREATE TABLE " +
                MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE + "(" + MoviesContract.MoviesFavoriteEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesFavoriteEntry.COLUMN_TITLEE + " TEXT NOT NULL, " +
                MoviesContract.MoviesFavoriteEntry.COLUMN_POSTERR + " BLOB NOT NULL, " +
                MoviesContract.MoviesFavoriteEntry.COLUMN_SYPNOPSISS + " TEXT NOT NULL, " +
                MoviesContract.MoviesFavoriteEntry.COLUMN_USER_RATINGG + " TEXT NOT NULL, " +
                MoviesContract.MoviesFavoriteEntry.COLUMN_RELEASE_DATEE + " TEXT NOT NULL, " +
                MoviesContract.MoviesFavoriteEntry.COLUMN_REVIEWW + " TEXT NOT NULL, " +
                MoviesContract.MoviesFavoriteEntry.COLUMN_TRAILERSS + " TEXT NOT NULL, " +
                " UNIQUE (" + MoviesContract.MoviesFavoriteEntry.COLUMN_TITLEE + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_MOVIES_MOST_POPULAR_TABLE = "CREATE TABLE " +
                MoviesContract.MoviesEntryMostPopular.TABLE_NAME + "(" + MoviesContract.MoviesEntryMostPopular._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntryMostPopular.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntryMostPopular.COLUMN_POSTER + " BLOB NOT NULL, " +
                MoviesContract.MoviesEntryMostPopular.COLUMN_SYPNOPSIS + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntryMostPopular.COLUMN_USER_RATING + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntryMostPopular.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntryMostPopular.COLUMN_REVIEW + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntryMostPopular.COLUMN_TRAILERS +
                " TEXT NOT NULL);";

        final String SQL_CREATE_MOVIES_HIGHT_TOP_TABLE = "CREATE TABLE " +
                MoviesContract.MoviesEntryHightTop.TABLE_NAMEH + "(" + MoviesContract.MoviesEntryHightTop._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntryHightTop.COLUMN_TITLEH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntryHightTop.COLUMN_POSTERH + " BLOB NOT NULL, " +
                MoviesContract.MoviesEntryHightTop.COLUMN_SYPNOPSISH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntryHightTop.COLUMN_USER_RATINGH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntryHightTop.COLUMN_RELEASE_DATEH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntryHightTop.COLUMN_REVIEWH + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntryHightTop.COLUMN_TRAILERSH +
                " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_FAVORITE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_MOST_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_HIGHT_TOP_TABLE);
    }

    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE);
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntryMostPopular.TABLE_NAME);
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntryHightTop.TABLE_NAMEH);
        // re-create database
        onCreate(sqLiteDatabase);
    }
}
