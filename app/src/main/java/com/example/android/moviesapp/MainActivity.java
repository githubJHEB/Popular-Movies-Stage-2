package com.example.android.moviesapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

import org.json.JSONObject;

import static android.R.attr.fragment;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {
    private static final String DEBUG_TAG = "Movies";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private JSONObject mainObject;
    private boolean mTwoPane;

    @Override
    public void onItemSelected(int positionsd) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putInt("index", positionsd);
            args.putBoolean("TwoPane", mTwoPane);

            Detail_Activity.DetailFragment fragment = new Detail_Activity.DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, Detail_Activity.class);
            intent.putExtra("moviesStrDataP", positionsd);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        //Support the fragment to the main activity
        if (findViewById(R.id.movies_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movies_detail_container, new Detail_Activity.DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        ActionBar g = getSupportActionBar();
        g.setDisplayShowHomeEnabled(true);
        g.setIcon(R.drawable.camara);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        this.invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingactivity = new Intent(this, SettingsActivity.class);
                startActivity(settingactivity);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
