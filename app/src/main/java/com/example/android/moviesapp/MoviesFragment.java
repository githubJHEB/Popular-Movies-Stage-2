package com.example.android.moviesapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.android.moviesapp.data.AdpaterMovies;
import com.example.android.moviesapp.data.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by jman on 20/09/16.
 */

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int CURSOR_LOADER_ID = 0;
    private static final int CURSOR_LOADER_IDD = 1;
    private static final int CURSOR_LOADER_IDH = 2;
    private static final String DEGBUG_TA = "TRAK";
    Callback mCallback;
    String unitType;
    private AdpaterMovies mMoviesAdapterMain;
    private RecyclerView recyclerView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initialize loader
        getLoaderManager().initLoader(CURSOR_LOADER_IDH, null, this); //Init loader for each table highestrated
        getLoaderManager().initLoader(CURSOR_LOADER_IDD, null, this); //Init loader for each table favorities
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this); //Init loader for each table mostppopular
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Created diferent cursor, for acces each table
        if (unitType.equals("favorities")) {
            return new CursorLoader(getActivity(), MoviesContract.MoviesFavoriteEntry.CONTENT_URI, null, null, null, null);
        } else if (unitType.equals("highestrated")) {
            return new CursorLoader(getActivity(), MoviesContract.MoviesEntryHightTop.CONTENT_URI_H, null, null, null, null);
        } else
            return new CursorLoader(getActivity(), MoviesContract.MoviesEntryMostPopular.CONTENT_URIM, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesAdapterMain.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapterMain.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        //Inflate the content of the menu layout
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                //Checks if the settings option menu was selected, and then initilize a settings Activity, to save the option selected.
                Intent settingactivity = new Intent(getContext(), SettingsActivity.class);
                startActivity(settingactivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        Log.v(DEGBUG_TA, "oncreateView=" + 1);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        unitType = prefs.getString(getString(R.string.pref_Order_of_the_movies_key), getString(R.string.pref_Order_of_the_movies_Most_Popular));
        mMoviesAdapterMain = new AdpaterMovies(getActivity(), null, mCallback);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        //Log.v(DEGBUG_TA, "recyclerView1=" + recyclerView);
        recyclerView.setHasFixedSize(true);
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);


        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int number;
        //estimate teh numbre of poster
        number = (width / 2) / 170 + 1;


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), number));
        // query data if the setting is favorities
        if (unitType.equals("favorities")) {
            Cursor c =
                    getActivity().getContentResolver().query(MoviesContract.MoviesFavoriteEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
            mMoviesAdapterMain.swapCursor(c);
            recyclerView.setAdapter(mMoviesAdapterMain);
        } else {
            recyclerView.setAdapter(mMoviesAdapterMain);
        }
        return rootView;
    }

    private String[] getMoviesDataFromJson(String[] moviesJsonDoingBackGround)
            throws JSONException {
        //Obtains from JSON all the information necessary to see in the main and detail fragment. Adds labels to separate the information
        final String TMDB_RESULTS = "results";
        final String TMDB_POSTER = "poster_path";
        final String TMDB_ID = "id";
        final String TMDB_TITLE = "title";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_VOTE_AVERAGE = "vote_average";
        final String TMDB_SYPNOPSIS = "overview";
        final String TMDB_REVIEW = "reviews";
        final String TMDB_TRAILER = "videos";
        final String TMDB_CONTENT = "content";
        final String TMDB_KEYTRAILERYOUTUBE = "key";
        final String TMDB_AUTHOR = "author";

        String resultContentTrailReview = "";
        String resultKeyYoutubeTrail = "";
        String resultStrsPoster;
        String resultStrTitle;
        String resultStrsReleaseDate;
        String resultStrsVoteAverage;
        String resultStrsOverview;
        String moviesJsonResultStage1 = moviesJsonDoingBackGround[0];

        JSONObject moviesJsonTrailReview;
        JSONObject moviesReviewTrailReview;
        JSONArray resultReviewArray;
        JSONObject moviesTrailer;
        JSONArray resultTrailerArray;
        JSONObject moviesJson = new JSONObject(moviesJsonResultStage1);
        JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);

        String[] resultStrs = new String[moviesArray.length()];
        ContentValues[] moviesValuesArr = new ContentValues[moviesArray.length()];
        ContentValues[] moviesValuesArrh = new ContentValues[moviesArray.length()];
        int reviewZise = 0;
        int trailerZise = 0;
        if (moviesArray.length() != 0) {

            for (int i = 0; i < moviesArray.length(); ++i) {
                // obtain data review and trailers from json and store for future use
                String moviesJsonResultStage2 = moviesJsonDoingBackGround[i + 1];
                //Log.v(DEGBUG_TA, "moviesJsonResultStage2=" + moviesJsonResultStage2);
                if (moviesJsonResultStage2 != null && !moviesJsonResultStage2.isEmpty()) {
                    moviesJsonTrailReview = new JSONObject(moviesJsonResultStage2);
                    moviesReviewTrailReview = moviesJsonTrailReview.getJSONObject(TMDB_REVIEW);
                    resultReviewArray = moviesReviewTrailReview.getJSONArray(TMDB_RESULTS);
                    moviesTrailer = moviesJsonTrailReview.getJSONObject(TMDB_TRAILER);
                    resultTrailerArray = moviesTrailer.getJSONArray(TMDB_RESULTS);
                    reviewZise = resultReviewArray.length();
                    trailerZise = resultTrailerArray.length();

                    if (reviewZise > 4) {
                        for (int j = 0; j < 4; ++j) {
                            JSONObject reviews = resultReviewArray.getJSONObject(j);
                            if (j != 3) {
                                resultContentTrailReview = resultContentTrailReview + reviews.getString(TMDB_CONTENT) + "  By : " + reviews.getString(TMDB_AUTHOR) + "#" + j + "##";
                            } else {
                                resultContentTrailReview = resultContentTrailReview + reviews.getString(TMDB_CONTENT) + "  By : " + reviews.getString(TMDB_AUTHOR) + "#X##";
                            }
                        }

                    } else {
                        for (int j = 0; j < reviewZise; ++j) {
                            JSONObject reviews = resultReviewArray.getJSONObject(j);
                            if (j != reviewZise - 1) {
                                resultContentTrailReview = resultContentTrailReview + reviews.getString(TMDB_CONTENT) + "  By : " + reviews.getString(TMDB_AUTHOR) + "#" + j + "##";
                            } else {
                                resultContentTrailReview = resultContentTrailReview + reviews.getString(TMDB_CONTENT) + "  By : " + reviews.getString(TMDB_AUTHOR) + "#X##";
                            }
                        }
                    }
                    if (trailerZise > 4) {
                        for (int k = 0; k < 4; ++k) {
                            JSONObject trailers = resultTrailerArray.getJSONObject(k);
                            if (k != 3) {
                                resultKeyYoutubeTrail = resultKeyYoutubeTrail + trailers.getString(TMDB_KEYTRAILERYOUTUBE) + "#" + k + "##";
                            } else {
                                resultKeyYoutubeTrail = resultKeyYoutubeTrail + trailers.getString(TMDB_KEYTRAILERYOUTUBE) + "#X##";
                            }
                        }
                    } else {
                        for (int k = 0; k < trailerZise; ++k) {
                            JSONObject trailers = resultTrailerArray.getJSONObject(k);
                            if (k != trailerZise - 1) {
                                resultKeyYoutubeTrail = resultKeyYoutubeTrail + trailers.getString(TMDB_KEYTRAILERYOUTUBE) + "#" + k + "##";
                            } else {
                                resultKeyYoutubeTrail = resultKeyYoutubeTrail + trailers.getString(TMDB_KEYTRAILERYOUTUBE) + "#X##";
                            }
                        }
                    }
                }
                resultContentTrailReview = "####" + resultContentTrailReview;
                resultKeyYoutubeTrail = "####" + resultKeyYoutubeTrail;
                JSONObject movieObject = moviesArray.getJSONObject(i);
                //Obtain information about stage movie app 1, as a title, release date an so on, from json
                resultStrsPoster = movieObject.getString(TMDB_POSTER).trim();
                resultStrTitle = movieObject.getString(TMDB_TITLE);
                resultStrsReleaseDate = movieObject.getString(TMDB_RELEASE_DATE);
                resultStrsVoteAverage = movieObject.getString(TMDB_VOTE_AVERAGE);
                resultStrsOverview = movieObject.getString(TMDB_SYPNOPSIS);

                //store in diferent tables data. For most pupular and hightrate movies
                if (unitType.equals("highestrated")) {

                    moviesValuesArrh[i] = new ContentValues();
                    moviesValuesArrh[i].put(MoviesContract.MoviesEntryHightTop.COLUMN_TITLEH, resultStrTitle);
                    moviesValuesArrh[i].put(MoviesContract.MoviesEntryHightTop.COLUMN_POSTERH, resultStrsPoster);
                    moviesValuesArrh[i].put(MoviesContract.MoviesEntryHightTop.COLUMN_SYPNOPSISH, resultStrsOverview);
                    moviesValuesArrh[i].put(MoviesContract.MoviesEntryHightTop.COLUMN_USER_RATINGH, resultStrsVoteAverage);
                    moviesValuesArrh[i].put(MoviesContract.MoviesEntryHightTop.COLUMN_RELEASE_DATEH, resultStrsReleaseDate);
                    moviesValuesArrh[i].put(MoviesContract.MoviesEntryHightTop.COLUMN_REVIEWH, resultContentTrailReview);
                    moviesValuesArrh[i].put(MoviesContract.MoviesEntryHightTop.COLUMN_TRAILERSH, resultKeyYoutubeTrail);
                } else {
                    moviesValuesArr[i] = new ContentValues();
                    moviesValuesArr[i].put(MoviesContract.MoviesEntryMostPopular.COLUMN_TITLE, resultStrTitle);
                    moviesValuesArr[i].put(MoviesContract.MoviesEntryMostPopular.COLUMN_POSTER, resultStrsPoster);
                    moviesValuesArr[i].put(MoviesContract.MoviesEntryMostPopular.COLUMN_SYPNOPSIS, resultStrsOverview);
                    moviesValuesArr[i].put(MoviesContract.MoviesEntryMostPopular.COLUMN_USER_RATING, resultStrsVoteAverage);
                    moviesValuesArr[i].put(MoviesContract.MoviesEntryMostPopular.COLUMN_RELEASE_DATE, resultStrsReleaseDate);
                    moviesValuesArr[i].put(MoviesContract.MoviesEntryMostPopular.COLUMN_REVIEW, resultContentTrailReview);
                    moviesValuesArr[i].put(MoviesContract.MoviesEntryMostPopular.COLUMN_TRAILERS, resultKeyYoutubeTrail);

                }

                resultContentTrailReview = "";
                resultKeyYoutubeTrail = "";
            }
            if (unitType.equals("highestrated")) {
                getActivity().getContentResolver().delete(MoviesContract.MoviesEntryHightTop.CONTENT_URI_H, null, null);
                getActivity().getContentResolver().bulkInsert(MoviesContract.MoviesEntryHightTop.CONTENT_URI_H, moviesValuesArrh);
            } else {
                getActivity().getContentResolver().delete(MoviesContract.MoviesEntryMostPopular.CONTENT_URIM, null, null);
                getActivity().getContentResolver().bulkInsert(MoviesContract.MoviesEntryMostPopular.CONTENT_URIM, moviesValuesArr);
            }
        }
        return resultStrs;
    }

    private void updateMovies() {
        FetchMoviesTask weatherTask = new FetchMoviesTask();
        if (!unitType.equals("favorities")) {
            weatherTask.execute(unitType);
        } else {
            Cursor c =
                    getActivity().getContentResolver().query(MoviesContract.MoviesFavoriteEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
            mMoviesAdapterMain.swapCursor(c);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(int dateUri);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {
        //Async task with short duration to separate work of the main treat to a second treat, to obtain and format to the information from Picasso and TheMovies.org

        @Override
        //Modify the adpter with the new information requested by the menu option. (Most Popular, an The height rated
        protected void onPostExecute(String[] strings) {
            if (unitType.equals("mostpopular")) {
                Cursor c =
                        getActivity().getContentResolver().query(MoviesContract.MoviesEntryMostPopular.CONTENT_URIM,
                                null,
                                null,
                                null,
                                null);
                mMoviesAdapterMain.swapCursor(c);
            } else if (unitType.equals("highestrated")) {
                Cursor c =
                        getActivity().getContentResolver().query(MoviesContract.MoviesEntryHightTop.CONTENT_URI_H,
                                null,
                                null,
                                null,
                                null);
                mMoviesAdapterMain.swapCursor(c);

            }
        }

        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            BufferedReader readerTrailerReview = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr;
            String[] moviesJsonStrTrailerReview = new String[21];
            ;
            String apiKey = BuildConfig.OPEN_MOVIES_MAP_API_KEY;

            try {
                String PARAM = "sort_by";
                // Construct the URL for the TheMovieDb query
                // Possible parameters are available at TheMovieDb API page
                String parametro = params[0];
                if ("highestrated".equals(parametro)) {
                    PARAM = "top_rated";
                } else {
                    PARAM = "popular";
                }
                final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/" + PARAM + "?";
                final String API_KEY = "api_key";
                final String APPENDTORESPONSE = "append_to_response";
                final String VIDEOSREVIEWS = "videos,reviews";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, apiKey)
                        .build();
                URL url = new URL(builtUri.toString());
                // Create the request to TheMovieDb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Fore easy debug
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                try {
                    JSONObject moviesTrailerReviews = new JSONObject(moviesJsonStr);
                    JSONArray moviesTrailesReviewArray = moviesTrailerReviews.getJSONArray("results");
                    for (int i = 0; i < moviesTrailesReviewArray.length(); ++i) {
                        JSONObject movieObjectTrailerReview = moviesTrailesReviewArray.getJSONObject(i);
                        String resultStrId = movieObjectTrailerReview.getString("id");

                        final String MOVIES_BASE_URL_TRAILER_REVIEW = "https://api.themoviedb.org/3/movie/" + resultStrId + "?";
                        try {
                            //http://api.themoviedb.org/3/movie/movies_id?api_key=key&append_to_response=videos,reviews
                            Uri builtUriTrailerReview = Uri.parse(MOVIES_BASE_URL_TRAILER_REVIEW).buildUpon()
                                    .appendQueryParameter(API_KEY, apiKey)
                                    .appendQueryParameter(APPENDTORESPONSE, VIDEOSREVIEWS)
                                    .build();
                            URL urlReviewTrailer = new URL(builtUriTrailerReview.toString());
                            // Create the request to TheMovieDb, and open the connection
                            urlConnection = (HttpURLConnection) urlReviewTrailer.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.connect();
                            //Log.v(DEGBUG_TA, "urlConnection=" + urlConnection);
                            // Read the input stream into a String
                            InputStream inputStreamTrailerRiview = urlConnection.getInputStream();
                            StringBuilder bufferTrailerReview = new StringBuilder();

                            if (inputStreamTrailerRiview == null) {
                                // Nothing to do.
                                return null;
                            }
                            readerTrailerReview = new BufferedReader(new InputStreamReader(inputStreamTrailerRiview));

                            String lineTrailerReview;
                            while ((lineTrailerReview = readerTrailerReview.readLine()) != null) {
                                // For easy debug.
                                bufferTrailerReview.append(lineTrailerReview).append("\n");
                            }

                            if (bufferTrailerReview.length() == 0) {
                                // Stream was empty.  No point in parsing.
                                return null;
                            }
                            moviesJsonStrTrailerReview[i + 1] = bufferTrailerReview.toString();
                        } catch (IOException e) {
                        } finally {
                            if (urlConnection != null) {
                                urlConnection.disconnect();
                            }
                            if (readerTrailerReview != null) {
                                try {
                                    readerTrailerReview.close();
                                } catch (final IOException e) {
                                }
                            }
                        }

                    }
                } catch (JSONException e) {
                }
            } catch (IOException e) {
                Log.v(DEGBUG_TA, "IOException=" + 1);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }

            try {
                //Extrac a String [] whith the information necesary
                moviesJsonStrTrailerReview[0] = moviesJsonStr;
                return getMoviesDataFromJson(moviesJsonStrTrailerReview);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}