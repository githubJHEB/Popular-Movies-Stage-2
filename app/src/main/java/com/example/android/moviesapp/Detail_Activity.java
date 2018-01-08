package com.example.android.moviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.moviesapp.data.AdpaterMovies;
import com.example.android.moviesapp.data.MoviesContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.widget.ToggleButton;

import static android.R.attr.button;
import static android.R.attr.data;
import static android.R.attr.description;
import static android.R.attr.visibility;
import static android.R.id.toggle;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

import static com.example.android.moviesapp.R.id.movies_detail_container;


public class Detail_Activity extends AppCompatActivity {

    private static final String DEGBUG_TA = "TRAK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(movies_detail_container, new DetailFragment())
                    .commit();
            ActionBar g = getSupportActionBar();
            g.setDisplayShowHomeEnabled(true);
            g.setIcon(R.drawable.camara);
        }

    }

    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        private static final int CURSOR_LOADER_ID = 0;
        String unitType;
        Integer position = 0;
        float number;
        String AuthorReview1;
        String AuthorReview2;
        String AuthorReview3;
        String AuthorReview4;
        String Review1;
        String Review2;
        String Review3;
        String Review4;
        String keyTrailer1;
        String keyTrailer2;
        String keyTrailer3;
        String keyTrailer4;
        String detailTitle;
        String detailReview;
        String detailTrailers;
        String detailReleaeDate;
        String detailVote;
        String detailSypnopsis;
        String urlImage;
        @BindView(R.id.reviews)
        TextView textReviews;
        @BindView(R.id.trailers)
        TextView textTrailers;
        @BindView(R.id.textReview1)
        TextView textReview1;
        @BindView(R.id.textTrailer1)
        TextView textTrailer1;
        @BindView(R.id.textTrailer2)
        TextView textTrailer2;
        @BindView(R.id.textTrailer3)
        TextView textTrailer3;
        @BindView(R.id.textTrailer4)
        TextView textTrailer4;
        @BindView(R.id.buttonReviewTrailer1)
        TextView buttonReview1;
        @BindView(R.id.buttonReviewTrailer2)
        TextView buttonReview2;
        @BindView(R.id.buttonReviewTrailer3)
        TextView buttonReview3;
        @BindView(R.id.buttonReviewTrailer4)
        TextView buttonReview4;
        @BindView(R.id.trailer1)
        Button trailer1;
        @BindView(R.id.trailer2)
        Button trailer2;
        @BindView(R.id.trailer3)
        Button trailer3;
        @BindView(R.id.trailer4)
        Button trailer4;
        @BindView(R.id.addfavorities)
        Button addFavorite;
        @BindView(R.id.detail_title_textview)
        TextView titleDisplay;
        @BindView(R.id.detail_release_date_textview)
        TextView releaseDate;
        @BindView(R.id.rating)
        RatingBar mRatingBar;
        @BindView(R.id.detail_vote_average_textview)
        TextView voteDisplay;
        @BindView(R.id.detail_sypnopsis_textview)
        TextView sypnopsisDate;
        @BindView(R.id.detail_image_poster)
        ImageView image;
        View.OnClickListener myhandler = new View.OnClickListener() {
            public void onClick(View v) {
                insertData();
            }
        };
        private String mForecast;
        private ShareActionProvider mShareActionProvider;
        private Cursor mDetailCursor;
        private Unbinder unbinder;


        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        public static DetailFragment newInstance(int index) {
            DetailFragment f = new DetailFragment();
            Bundle argss = new Bundle();
            argss.putInt("index", index);
            f.setArguments(argss);
            return f;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detailshared, menu);
            // Retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);
            // Get the provider and hold onto it to set/change the share intent.
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            // If onLoadFinished happens before this, we can go ahead and set the share intent now.
            if (mForecast != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }

        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast);
            return shareIntent;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // initialize loader
            getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (unitType.equals("favorities")) {
                return new CursorLoader(getActivity(), MoviesContract.MoviesFavoriteEntry.CONTENT_URI, null, null, null, null);
            } else if (unitType.equals("highestrated")) {
                return new CursorLoader(getActivity(), MoviesContract.MoviesEntryHightTop.CONTENT_URI_H, null, null, null, null);
            } else {
                return new CursorLoader(getActivity(), MoviesContract.MoviesEntryMostPopular.CONTENT_URIM, null, null, null, null);
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            ;
            mDetailCursor = data;
            mDetailCursor.moveToFirst();
            mDetailCursor.moveToPosition(position);
            detailTitle = mDetailCursor.getString(1);
            urlImage = mDetailCursor.getString(2);
            detailSypnopsis = mDetailCursor.getString(3);
            detailVote = mDetailCursor.getString(4);
            detailReleaeDate = mDetailCursor.getString(5);
            detailReview = mDetailCursor.getString(6);
            detailTrailers = mDetailCursor.getString(7);
            number = Float.valueOf(detailVote) / 2;
            titleDisplay.setText(detailTitle);
            releaseDate.setText(detailReleaeDate);
            mRatingBar.setRating(Float.valueOf(number));
            voteDisplay.setText(String.valueOf(number));
            sypnopsisDate.setText(detailSypnopsis);

            Picasso
                    .with(getActivity())
                    .load("http://image.tmdb.org/t/p/" + "w185" + urlImage)
                    .placeholder(R.drawable.timer)
                    .fit()
                    .into(image);

            if (detailReview.contains("#0##") && detailReview.contains("#1##") && detailReview.contains("#2##")) {
                Review1 = detailReview.substring(detailReview.indexOf("####") + 4, detailReview.indexOf("#0##"));
                Review2 = detailReview.substring(detailReview.indexOf("#0##") + 4, detailReview.indexOf("#1##"));
                Review3 = detailReview.substring(detailReview.indexOf("#1##") + 4, detailReview.indexOf("#2##"));
                Review4 = detailReview.substring(detailReview.indexOf("#2##") + 4, detailReview.indexOf("#X##"));
                textReviews.setVisibility(View.VISIBLE);
                AuthorReview1 = Review1.substring(Review1.indexOf("By : ") + 5);
                AuthorReview2 = Review2.substring(Review2.indexOf("By : ") + 5);
                AuthorReview3 = Review3.substring(Review3.indexOf("By : ") + 5);
                AuthorReview4 = Review4.substring(Review4.indexOf("By : ") + 5);
                buttonReview1.setVisibility(View.VISIBLE);
                buttonReview2.setVisibility(View.VISIBLE);
                buttonReview3.setVisibility(View.VISIBLE);
                buttonReview4.setVisibility(View.VISIBLE);
                buttonReview1.setText(AuthorReview1);
                buttonReview2.setText(AuthorReview2);
                buttonReview3.setText(AuthorReview3);
                buttonReview4.setText(AuthorReview4);


            } else if (detailReview.contains("#0##") && detailReview.contains("#1##")) {
                Review1 = detailReview.substring(detailReview.indexOf("####") + 4, detailReview.indexOf("#0##"));
                Review2 = detailReview.substring(detailReview.indexOf("#0##") + 4, detailReview.indexOf("#1##"));
                Review3 = detailReview.substring(detailReview.indexOf("#1##") + 4, detailReview.indexOf("#X##"));
                textReviews.setVisibility(View.VISIBLE);
                AuthorReview1 = Review1.substring(Review1.indexOf("By : ") + 5);
                AuthorReview2 = Review2.substring(Review2.indexOf("By : ") + 5);
                AuthorReview3 = Review3.substring(Review3.indexOf("By : ") + 5);
                buttonReview1.setVisibility(View.VISIBLE);
                buttonReview2.setVisibility(View.VISIBLE);
                buttonReview3.setVisibility(View.VISIBLE);
                buttonReview1.setText(AuthorReview1);
                buttonReview2.setText(AuthorReview2);
                buttonReview3.setText(AuthorReview3);


            } else if (detailReview.contains("#0##")) {
                Review1 = detailReview.substring(detailReview.indexOf("####") + 4, detailReview.indexOf("#0##"));
                Review2 = detailReview.substring(detailReview.indexOf("#0##") + 4, detailReview.indexOf("#X##"));
                textReviews.setVisibility(View.VISIBLE);
                AuthorReview1 = Review1.substring(Review1.indexOf("By : ") + 5);
                AuthorReview2 = Review2.substring(Review2.indexOf("By : ") + 5);
                buttonReview1.setVisibility(View.VISIBLE);
                buttonReview2.setVisibility(View.VISIBLE);
                buttonReview1.setText(AuthorReview1);
                buttonReview2.setText(AuthorReview2);


            } else if (detailReview.contains("#X##")) {
                Review1 = detailReview.substring(detailReview.indexOf("####") + 4, detailReview.indexOf("#X##"));
                textReviews.setVisibility(View.VISIBLE);
                AuthorReview1 = Review1.substring(Review1.indexOf("By : ") + 5);
                buttonReview1.setVisibility(View.VISIBLE);
                buttonReview1.setText(AuthorReview1);
            }

            if (detailTrailers.contains("#0##") && detailTrailers.contains("#1##") && detailTrailers.contains("#2##")) {
                keyTrailer1 = detailTrailers.substring(detailTrailers.indexOf("####") + 4, detailTrailers.indexOf("#0##"));
                keyTrailer2 = detailTrailers.substring(detailTrailers.indexOf("#0##") + 4, detailTrailers.indexOf("#1##"));
                keyTrailer3 = detailTrailers.substring(detailTrailers.indexOf("#1##") + 4, detailTrailers.indexOf("#2##"));
                keyTrailer4 = detailTrailers.substring(detailTrailers.indexOf("#2##") + 4, detailTrailers.indexOf("#X##"));
                textTrailers.setVisibility(View.VISIBLE);
                trailer1.setVisibility(View.VISIBLE);
                trailer2.setVisibility(View.VISIBLE);
                trailer3.setVisibility(View.VISIBLE);
                trailer4.setVisibility(View.VISIBLE);
                textTrailer1.setVisibility(View.VISIBLE);
                textTrailer2.setVisibility(View.VISIBLE);
                textTrailer3.setVisibility(View.VISIBLE);
                textTrailer4.setVisibility(View.VISIBLE);
            } else if (detailTrailers.contains("#0##") && detailTrailers.contains("#1##")) {
                keyTrailer1 = detailTrailers.substring(detailTrailers.indexOf("####") + 4, detailTrailers.indexOf("#0##"));
                keyTrailer2 = detailTrailers.substring(detailTrailers.indexOf("#0##") + 4, detailTrailers.indexOf("#1##"));
                keyTrailer3 = detailTrailers.substring(detailTrailers.indexOf("#1##") + 4, detailTrailers.indexOf("#X##"));
                textTrailers.setVisibility(View.VISIBLE);
                trailer1.setVisibility(View.VISIBLE);
                trailer2.setVisibility(View.VISIBLE);
                trailer3.setVisibility(View.VISIBLE);
                textTrailer1.setVisibility(View.VISIBLE);
                textTrailer2.setVisibility(View.VISIBLE);
                textTrailer3.setVisibility(View.VISIBLE);
            } else if (detailTrailers.contains("#0##")) {
                keyTrailer1 = detailTrailers.substring(detailTrailers.indexOf("####") + 4, detailTrailers.indexOf("#0##"));
                keyTrailer2 = detailTrailers.substring(detailTrailers.indexOf("#0##") + 4, detailTrailers.indexOf("#X##"));
                textTrailers.setVisibility(View.VISIBLE);
                trailer1.setVisibility(View.VISIBLE);
                trailer2.setVisibility(View.VISIBLE);
                textTrailer1.setVisibility(View.VISIBLE);
                textTrailer2.setVisibility(View.VISIBLE);
            } else if (detailTrailers.contains("#X##")) {
                keyTrailer1 = detailTrailers.substring(detailTrailers.indexOf("####") + 4, detailTrailers.indexOf("#X##"));
                textTrailers.setVisibility(View.VISIBLE);
                trailer1.setVisibility(View.VISIBLE);
                trailer1.setMovementMethod(new ScrollingMovementMethod());
                textTrailer1.setVisibility(View.VISIBLE);
            }
            // We still need this for the share intent
            mForecast = "http://www.youtube.com/watch?v=" + keyTrailer1;
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mDetailCursor = null;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            final Intent intent = getActivity().getIntent();
            Bundle extras = intent.getExtras();


            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            unitType = prefs.getString(getString(R.string.pref_Order_of_the_movies_key), getString(R.string.pref_Order_of_the_movies_Most_Popular));
            if (unitType.equals("favorities")) {
                addFavorite.setOnClickListener(myhandler);
            } else {
                addFavorite.setVisibility(View.VISIBLE);
                addFavorite.setOnClickListener(myhandler);
            }

            if (arguments != null) {
                position = arguments.getInt("index", 0);
            } else if (intent != null && extras != null) {
                position = extras.getInt("moviesStrDataP");
                Log.v(DEGBUG_TA, "onCreateViewposition=" + position);
            }

            Log.v(DEGBUG_TA, "position=" + position);
            trailer1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + keyTrailer1)));
                    Log.v(DEGBUG_TA, "keyTrailer1=" + keyTrailer1);
                }
            });
            trailer2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + keyTrailer2)));
                    Log.v(DEGBUG_TA, "keyTrailer2=" + keyTrailer2);
                }
            });
            trailer3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + keyTrailer3)));
                    Log.v(DEGBUG_TA, "keyTrailer3=" + keyTrailer3);
                }
            });
            trailer4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + keyTrailer4)));
                    Log.v(DEGBUG_TA, "keyTrailer4=" + keyTrailer4);
                }
            });


            buttonReview1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    textReview1.setBackgroundColor(getResources().getColor(R.color.backgroundTextReview1));
                    textReview1.setVisibility(View.VISIBLE);
                    textReview1.setText(Review1);
                }
            });
            buttonReview2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    textReview1.setBackgroundColor(getResources().getColor(R.color.backgroundTextReview2));
                    textReview1.setVisibility(View.VISIBLE);

                    textReview1.setText(Review2);
                }
            });
            buttonReview3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    textReview1.setBackgroundColor(getResources().getColor(R.color.backgroundTextReview3));
                    textReview1.setVisibility(View.VISIBLE);
                    textReview1.setText(Review3);
                }
            });
            buttonReview4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    textReview1.setBackgroundColor(getResources().getColor(R.color.backgroundTextReview4));
                    textReview1.setVisibility(View.VISIBLE);
                    textReview1.setText(Review4);
                }
            });
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        // insert data into database for favorite selection
        public void insertData() {
            ContentValues flavorValuesArr;
            // Save favorities information in data base.

            flavorValuesArr = new ContentValues();
            flavorValuesArr.put(MoviesContract.MoviesFavoriteEntry.COLUMN_TITLEE, detailTitle);
            flavorValuesArr.put(MoviesContract.MoviesFavoriteEntry.COLUMN_POSTERR, urlImage);
            flavorValuesArr.put(MoviesContract.MoviesFavoriteEntry.COLUMN_SYPNOPSISS, detailSypnopsis);
            flavorValuesArr.put(MoviesContract.MoviesFavoriteEntry.COLUMN_USER_RATINGG, String.valueOf(number));
            flavorValuesArr.put(MoviesContract.MoviesFavoriteEntry.COLUMN_RELEASE_DATEE, detailReleaeDate);
            flavorValuesArr.put(MoviesContract.MoviesFavoriteEntry.COLUMN_REVIEWW, detailReview);
            flavorValuesArr.put(MoviesContract.MoviesFavoriteEntry.COLUMN_TRAILERSS, detailTrailers);
            getActivity().getContentResolver().insert(MoviesContract.MoviesFavoriteEntry.CONTENT_URI,
                    flavorValuesArr);
        }
    }
}
