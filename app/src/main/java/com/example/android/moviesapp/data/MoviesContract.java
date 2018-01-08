package com.example.android.moviesapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.android.moviesapp.data.MoviesContract.MoviesEntryMostPopular.TABLE_NAME;


/**
 * Created by jman on 24/12/16.
 */

public class MoviesContract implements BaseColumns {

    public static final String CONTENT_AUTHORITY = "com.example.android.moviesapp.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MoviesFavoriteEntry implements BaseColumns {

        // table name
        public static final String TABLE_NAMEE = "moviesFavorities";
        // columns
        public static final String _IDD = "_id";
        public static final String COLUMN_TITLEE = "icon";
        public static final String COLUMN_POSTERR = "poster";
        public static final String COLUMN_SYPNOPSISS = "sypnopsis";
        public static final String COLUMN_USER_RATINGG = "user_rating";
        public static final String COLUMN_RELEASE_DATEE = "release_date";
        public static final String COLUMN_REVIEWW = "reviews";
        public static final String COLUMN_TRAILERSS = "videos";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAMEE).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPEE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAMEE;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPEE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAMEE;

        // for building URIs on insertion
        public static Uri buildFavoritiesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class MoviesEntryMostPopular implements BaseColumns {

        // table name
        public static final String TABLE_NAME = "moviesmostpopular";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_TITLE = "icon";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_SYPNOPSIS = "sypnopsis";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_REVIEW = "reviews";
        public static final String COLUMN_TRAILERS = "videos";

        // create content uri
        public static final Uri CONTENT_URIM = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // for building URIs on insertion
        public static Uri buildMoviesUriM(long id) {
            return ContentUris.withAppendedId(CONTENT_URIM, id);
        }
    }


    public static final class MoviesEntryHightTop implements BaseColumns {

        // table name
        public static final String TABLE_NAMEH = "movieshighttop";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_TITLEH = "icon";
        public static final String COLUMN_POSTERH = "poster";
        public static final String COLUMN_SYPNOPSISH = "sypnopsis";
        public static final String COLUMN_USER_RATINGH = "user_rating";
        public static final String COLUMN_RELEASE_DATEH = "release_date";
        public static final String COLUMN_REVIEWH = "reviews";
        public static final String COLUMN_TRAILERSH = "videos";

        // create content uri
        public static final Uri CONTENT_URI_H = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAMEH).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAMEH;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAMEH;

        // for building URIs on insertion
        public static Uri buildMoviesUriH(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_H, id);
        }
    }
}
