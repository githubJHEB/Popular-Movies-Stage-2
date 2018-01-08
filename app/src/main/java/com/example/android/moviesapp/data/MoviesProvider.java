package com.example.android.moviesapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by jman on 24/12/16.
 */

public class MoviesProvider extends ContentProvider {
    private static final String LOG_TAG = MoviesProvider.class.getSimpleName();
    private static final String DEGBUG_TA = "TRAK";
    // Codes for the UriMatcher //////
    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 200;
    ////////
    private static final int FAVORITIES = 300;
    private static final int FAVORITIES_WITH_ID = 400;
    private static final int MOVIESHT = 500;
    private static final int MOVIES_WITH_ID_HT = 600;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, MoviesContract.MoviesEntryMostPopular.TABLE_NAME, MOVIES);
        matcher.addURI(authority, MoviesContract.MoviesEntryMostPopular.TABLE_NAME + "/#", MOVIES_WITH_ID);
        // add a code for each type of URI you want
        matcher.addURI(authority, MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE, FAVORITIES);
        matcher.addURI(authority, MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE + "/#", FAVORITIES_WITH_ID);
        // add a code for each type of URI you want
        matcher.addURI(authority, MoviesContract.MoviesEntryHightTop.TABLE_NAMEH, MOVIESHT);
        matcher.addURI(authority, MoviesContract.MoviesEntryHightTop.TABLE_NAMEH + "/#", MOVIES_WITH_ID_HT);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITIES: {
                return MoviesContract.MoviesFavoriteEntry.CONTENT_DIR_TYPEE;
            }
            case FAVORITIES_WITH_ID: {
                return MoviesContract.MoviesFavoriteEntry.CONTENT_ITEM_TYPEE;
            }
            case MOVIES: {
                return MoviesContract.MoviesEntryMostPopular.CONTENT_DIR_TYPE;
            }
            case MOVIES_WITH_ID: {
                return MoviesContract.MoviesEntryMostPopular.CONTENT_ITEM_TYPE;
            }
            case MOVIESHT: {
                return MoviesContract.MoviesEntryHightTop.CONTENT_DIR_TYPE;
            }
            case MOVIES_WITH_ID_HT: {
                return MoviesContract.MoviesEntryHightTop.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // All Movies selected
            case FAVORITIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }

            case MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntryMostPopular.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }

            case MOVIESHT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntryHightTop.TABLE_NAMEH,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }

            // Individual flavor based on Id selected
            case FAVORITIES_WITH_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE,
                        projection,
                        MoviesContract.MoviesFavoriteEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }


            // Individual flavor based on Id selected
            case MOVIES_WITH_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntryMostPopular.TABLE_NAME,
                        projection,
                        MoviesContract.MoviesEntryMostPopular._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }

            case MOVIES_WITH_ID_HT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntryHightTop.TABLE_NAMEH,
                        projection,
                        MoviesContract.MoviesEntryHightTop._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            default: {
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case FAVORITIES: {
                long _id = db.insert(MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = MoviesContract.MoviesFavoriteEntry.buildFavoritiesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            case MOVIES: {
                long _id = db.insert(MoviesContract.MoviesEntryMostPopular.TABLE_NAME, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = MoviesContract.MoviesEntryMostPopular.buildMoviesUriM(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            case MOVIESHT: {
                long _id = db.insert(MoviesContract.MoviesEntryHightTop.TABLE_NAMEH, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = MoviesContract.MoviesEntryHightTop.buildMoviesUriH(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch (match) {
            case FAVORITIES:
                numDeleted = db.delete(
                        MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE + "'");
                break;
            case FAVORITIES_WITH_ID:
                numDeleted = db.delete(MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE,
                        MoviesContract.MoviesFavoriteEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE + "'");

                break;
            case MOVIES:
                numDeleted = db.delete(
                        MoviesContract.MoviesEntryMostPopular.TABLE_NAME, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.MoviesEntryMostPopular.TABLE_NAME + "'");
                break;
            case MOVIES_WITH_ID:
                numDeleted = db.delete(MoviesContract.MoviesEntryMostPopular.TABLE_NAME,
                        MoviesContract.MoviesEntryMostPopular._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.MoviesEntryMostPopular.TABLE_NAME + "'");

                break;
            case MOVIESHT:
                numDeleted = db.delete(
                        MoviesContract.MoviesEntryHightTop.TABLE_NAMEH, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.MoviesEntryHightTop.TABLE_NAMEH + "'");
                break;
            case MOVIES_WITH_ID_HT:
                numDeleted = db.delete(MoviesContract.MoviesEntryHightTop.TABLE_NAMEH,
                        MoviesContract.MoviesEntryHightTop._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MoviesContract.MoviesEntryHightTop.TABLE_NAMEH + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        //Log.i(LOG_TAG, "match: " + match);
        switch (match) {
            case FAVORITIES: {
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try {
                            _id = db.insertOrThrow(MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE,
                                    null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.w(LOG_TAG, "Attempting to insert " +
                                    value.getAsString(
                                            MoviesContract.MoviesFavoriteEntry._ID)
                                    + " but value is already in database.");
                        }
                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                if (numInserted > 0) {
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                    //db.close();
                }
                return numInserted;
            }
            case MOVIES: {
                Log.v(DEGBUG_TA, "MOVIES=" + 1);
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try {
                            _id = db.insertOrThrow(MoviesContract.MoviesEntryMostPopular.TABLE_NAME,
                                    null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.w(LOG_TAG, "Attempting to insert " +
                                    value.getAsString(
                                            MoviesContract.MoviesEntryMostPopular._ID)
                                    + " but value is already in database.");
                        }
                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                if (numInserted > 0) {
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            }


            case MOVIESHT: {
                //Log.v(DEGBUG_TA, "MOVIESHT=" + 1);
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try {
                            _id = db.insertOrThrow(MoviesContract.MoviesEntryHightTop.TABLE_NAMEH,
                                    null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.w(LOG_TAG, "Attempting to insert " +
                                    value.getAsString(
                                            MoviesContract.MoviesEntryHightTop._ID)
                                    + " but value is already in database.");
                        }
                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                if (numInserted > 0) {
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            }

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch (sUriMatcher.match(uri)) {
            case FAVORITIES: {
                numUpdated = db.update(MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIES: {
                numUpdated = db.update(MoviesContract.MoviesEntryMostPopular.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIESHT: {
                numUpdated = db.update(MoviesContract.MoviesEntryHightTop.TABLE_NAMEH,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVORITIES_WITH_ID: {
                numUpdated = db.update(MoviesContract.MoviesFavoriteEntry.TABLE_NAMEE,
                        contentValues,
                        MoviesContract.MoviesFavoriteEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            case MOVIES_WITH_ID: {
                numUpdated = db.update(MoviesContract.MoviesEntryMostPopular.TABLE_NAME,
                        contentValues,
                        MoviesContract.MoviesEntryMostPopular._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            case MOVIES_WITH_ID_HT: {
                numUpdated = db.update(MoviesContract.MoviesEntryHightTop.TABLE_NAMEH,
                        contentValues,
                        MoviesContract.MoviesEntryHightTop._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

}
