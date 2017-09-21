package com.akristic.www.tkrally;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.akristic.www.tkrally.data.PlayerContract.MatchEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Toni on 2.6.2017..
 */

public class MatchDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private Uri mCurrentPlayerUri;
    private static final int EXISTING_PLAYER_LOADER = 0;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private TextView textViewDate;
    private TextView textViewTime;
    private TextView textViewFinish;
    private TextView textViewArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        Intent intent = getIntent();
        mCurrentPlayerUri = intent.getData();

        getLoaderManager().initLoader(EXISTING_PLAYER_LOADER, null, this);

        // Find all relevant views that we will need to read user input from
        textViewPlayer1 = (TextView) findViewById(R.id.match_details_player_1);
        textViewPlayer2 = (TextView) findViewById(R.id.match_details_player_2);
        textViewArray = (TextView) findViewById(R.id.match_details_array);
        textViewDate = (TextView) findViewById(R.id.match_details_date);
        textViewTime = (TextView) findViewById(R.id.match_details_time);
        textViewFinish = (TextView) findViewById(R.id.match_details_finish);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MatchEntry._ID,
                MatchEntry.COLUMN_PLAYER_1_ID,
                MatchEntry.COLUMN_PLAYER_2_ID,
                MatchEntry.COLUMN_PLAYER_1_NAME,
                MatchEntry.COLUMN_PLAYER_2_NAME,
                MatchEntry.COLUMN_PLAYER_1_PICTURE,
                MatchEntry.COLUMN_PLAYER_2_PICTURE,
                MatchEntry.COLUMN_MATCH_ARRAY_LIST,
                MatchEntry.COLUMN_MATCH_TIME,
                MatchEntry.COLUMN_MATCH_DATE,
                MatchEntry.COLUMN_MATCH_FINISH
        };

        return new CursorLoader(this, mCurrentPlayerUri,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            // Find the columns of player attributes that we're interested in
            int player1ColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_PLAYER_1_NAME);
            int player2ColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_PLAYER_2_NAME);
            int arrayListColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_MATCH_ARRAY_LIST);
            int dateColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_MATCH_DATE);
            int timeColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_MATCH_TIME);
            int finishColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_MATCH_FINISH);


            // Extract out the value from the Cursor for the given column index
            String player1 = cursor.getString(player1ColumnIndex);
            String player2 = cursor.getString(player2ColumnIndex);
            String array = cursor.getString(arrayListColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            int finish = cursor.getInt(finishColumnIndex);


            // Update the views on the screen with the values from the database
            textViewPlayer1.setText(player1);
            textViewPlayer2.setText(player2);
            textViewArray.setText(array);
            textViewArray.setVisibility(View.GONE);
            textViewDate.setText(getString(R.string.date) + ": " + date);
            textViewTime.setText(getString(R.string.time_played) + ": " + time);
            if (finish == 1) {
                textViewFinish.setText(R.string.match_finished);
            } else {
                textViewFinish.setText(R.string.match_not_finished);
            }


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        textViewPlayer1.setText("");
        textViewPlayer1.setText("");
        textViewDate.setText("");
        textViewTime.setText("");
        textViewFinish.setText("");
    }

    public void loadArrayList(View v) {
        Gson gson = new Gson();
        String array = textViewArray.getText().toString();
        Type type = new TypeToken<List<UndoRedo>>() {
        }.getType();
        NavigationScoreKeeperActivity.savedState = gson.fromJson(array, type);
        NavigationScoreKeeperActivity.CURRENT_LOAD_MATCH_STATE = true;
    }
}
