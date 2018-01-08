package com.example.android.moviesapp.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import com.example.android.moviesapp.Detail_Activity;
import com.example.android.moviesapp.MoviesFragment;
import com.example.android.moviesapp.R;
import com.squareup.picasso.Picasso;

import android.preference.PreferenceManager;
import android.content.SharedPreferences;

/**
 * Created by jman on 19/12/16.
 */

public class AdpaterMovies extends RecyclerView.Adapter<AdpaterMovies.MyViewHolder> {
    public int mPosition = -1;
    public Cursor dataCursor;
    public Context context;
    MoviesFragment.Callback mCallback;

    public AdpaterMovies(Context mContext, Cursor cursor, MoviesFragment.Callback Callback) {
        context = mContext;
        dataCursor = cursor;
        mCallback = Callback;
    }

    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        dataCursor.moveToPosition(position);
        String album = dataCursor.getString(dataCursor.getColumnIndex(MoviesContract.MoviesEntryMostPopular.COLUMN_POSTER));

        // loading album cover using Picaso library
        //picasso loading here
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/" + "w185" + album)
                .placeholder(R.drawable.timer)
                .into(holder.thumb);
        holder.thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hh = holder.getLayoutPosition();
                mPosition = hh;
                mCallback.onItemSelected(hh);
            }
        });
    }

    @Override
    public AdpaterMovies.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumb;

        public MyViewHolder(View view) {
            super(view);
            thumb = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }
}