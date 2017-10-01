package com.akristic.www.tkrally;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akristic.www.tkrally.data.PlayerContract.MatchEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import static android.R.attr.id;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.savedState;



/**
 * Created by Toni on 2.6.2017..
 */

public class MatchDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private Uri mCurrentPlayerUri;
    private static final int EXISTING_MATCH_LOADER = 0;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private TextView textViewDate;
    private TextView textViewTime;
    private TextView textViewFinish;
    private TextView textViewArray;
    private ImageView imageViewPicturePlayer1;
    private ImageView imageViewPicturePlayer2;

    private String player1;
    private String player2;
    private Bitmap picturePlayer1;
    private Bitmap picturePlayer2;

    private int player1Winners;
    private int player1Aces;
    private int player1Faults;
    private int player1DoubleFaults;
    private int player1ForcedErrors;
    private int player1UnforcedErrors;
    private int player2Winners;
    private int player2Aces;
    private int player2Faults;
    private int player2DoubleFaults;
    private int player2ForcedErrors;
    private int player2UnforcedErrors;

    private static ArrayList<UndoRedo> savedStateMatchDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        Intent intent = getIntent();
        mCurrentPlayerUri = intent.getData();

        getLoaderManager().initLoader(EXISTING_MATCH_LOADER, null, this);

        // Find all relevant views that we will need to read user input from
        textViewPlayer1 = (TextView) findViewById(R.id.match_details_player_1);
        textViewPlayer2 = (TextView) findViewById(R.id.match_details_player_2);
        textViewArray = (TextView) findViewById(R.id.match_details_array);
        textViewDate = (TextView) findViewById(R.id.match_details_date);
        textViewTime = (TextView) findViewById(R.id.match_details_time);
        textViewFinish = (TextView) findViewById(R.id.match_details_finish);
        imageViewPicturePlayer1 = (ImageView) findViewById(R.id.match_details_image_player_1);
        imageViewPicturePlayer2 = (ImageView) findViewById(R.id.match_details_image_player_2);

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
            int picturePlayer1ColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_PLAYER_1_PICTURE);
            int picturePlayer2ColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_PLAYER_2_PICTURE);
            int arrayListColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_MATCH_ARRAY_LIST);
            int dateColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_MATCH_DATE);
            int timeColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_MATCH_TIME);
            int finishColumnIndex = cursor.getColumnIndex(MatchEntry.COLUMN_MATCH_FINISH);


            // Extract out the value from the Cursor for the given column index
            player1 = cursor.getString(player1ColumnIndex);
            player2 = cursor.getString(player2ColumnIndex);
            String array = cursor.getString(arrayListColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            int finish = cursor.getInt(finishColumnIndex);
            byte[] imgBytePlayer1 = null;
            if (cursor.getBlob(picturePlayer1ColumnIndex) != null) {
                imgBytePlayer1 = cursor.getBlob(picturePlayer1ColumnIndex);
            }
            byte[] imgBytePlayer2 = null;
            if (cursor.getBlob(picturePlayer2ColumnIndex) != null) {
                imgBytePlayer2 = cursor.getBlob(picturePlayer2ColumnIndex);
            }
            if (imgBytePlayer1 != null) {
                picturePlayer1 = BitmapFactory.decodeByteArray(imgBytePlayer1, 0, imgBytePlayer1.length);
                imageViewPicturePlayer1.setImageBitmap(picturePlayer1);
            }
            if (imgBytePlayer2 != null) {
                picturePlayer2 = BitmapFactory.decodeByteArray(imgBytePlayer2, 0, imgBytePlayer2.length);
                imageViewPicturePlayer2.setImageBitmap(picturePlayer2);
            }


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
            /**
             * get back array list from json
             */
            Gson gson = new Gson();
            String arrayTest = textViewArray.getText().toString();
            Type type = new TypeToken<List<UndoRedo>>() {
            }.getType();
            savedStateMatchDetails = gson.fromJson(arrayTest, type);

            /**
             * get data from array list used for undo and redo function from the last position
             *
             */
            int index = savedStateMatchDetails.size() - 1;
            int setsScore[];
            int setsPlayer1 = savedStateMatchDetails.get(index).getSetsPlayer1();
            int setsPlayer2 = savedStateMatchDetails.get(index).getSetsPlayer2();
            setsScore = savedStateMatchDetails.get(index).getSetsScore();
            player1Winners = savedStateMatchDetails.get(index).getWinnerPlayer1();
            player1Aces = savedStateMatchDetails.get(index).getAcePlayer1();
            player1Faults = savedStateMatchDetails.get(index).getFaultPlayer1();
            player1DoubleFaults =savedStateMatchDetails.get(index).getDoubleFaultPlayer1();
            player1ForcedErrors = savedStateMatchDetails.get(index).getForcedErrorPlayer1();
            player1UnforcedErrors =savedStateMatchDetails.get(index).getUnforcedErrorPlayer1();
            player2Winners = savedStateMatchDetails.get(index).getWinnerPlayer2();
            player2Aces = savedStateMatchDetails.get(index).getAcePlayer2();
            player2Faults = savedStateMatchDetails.get(index).getFaultPlayer2();
            player2DoubleFaults =savedStateMatchDetails.get(index).getDoubleFaultPlayer2();
            player2ForcedErrors = savedStateMatchDetails.get(index).getForcedErrorPlayer2();
            player2UnforcedErrors =savedStateMatchDetails.get(index).getUnforcedErrorPlayer2();

            TextView textViewSets = (TextView) findViewById(R.id.match_details_players_sets);
            TextView textViewSetScore = (TextView) findViewById(R.id.match_details_players_set_score);
            String setText = "";
            if (setsScore[0] > 0 || setsScore[1] > 0) {
                setText = setsScore[0] + " : " + setsScore[1];
            }
            if (setsScore[2] > 0 || setsScore[3] > 0) {
                setText = setText + "\n" + setsScore[2]+" : " + setsScore[3];
            }
            if (setsScore[4] > 0 || setsScore[5] > 0) {
                setText = setText + "\n" + setsScore[4]+" : " + setsScore[5];
            }
            if (setsScore[6] > 0 || setsScore[7] > 0) {
                setText = setText + "\n" + setsScore[6]+" : " + setsScore[7];
            }
            if (setsScore[8] > 0 || setsScore[9] > 0) {
                setText = setText + "\n" + setsScore[8]+" : " + setsScore[9];
            }
            textViewSetScore.setText(setText);
            textViewSets.setText(setsPlayer1 + " : " + setsPlayer2);
            setProgressBars();
            printStatistic();

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
        savedState = gson.fromJson(array, type);
        NavigationScoreKeeperActivity.CURRENT_LOAD_MATCH_STATE = true;
        PlayerEditorActivity.NAME_PLAYER1 = player1;
        PlayerEditorActivity.NAME_PLAYER2 = player2;
        PlayerEditorActivity.BITMAP_PLAYER1 = picturePlayer1;
        PlayerEditorActivity.BITMAP_PLAYER2 = picturePlayer2;
        NavigationScoreKeeperActivity.mCurrentMatchUri = mCurrentPlayerUri;
        finish();
    }
    private void setProgressBars() {
        int progress;
        ProgressBar progressBarWinners = (ProgressBar) findViewById(R.id.statistics_bar_winners);
        ProgressBar progressBarAces = (ProgressBar) findViewById(R.id.statistics_bar_aces);
        ProgressBar progressBarFaults = (ProgressBar) findViewById(R.id.statistics_bar_faults);
        ProgressBar progressBarDoubleFaults = (ProgressBar) findViewById(R.id.statistics_bar_double_faults);
        ProgressBar progressBarForcedErrors = (ProgressBar) findViewById(R.id.statistics_bar_forced_errors);
        ProgressBar progressBarUnforcedErrors = (ProgressBar) findViewById(R.id.statistics_bar_unforced_errors);
        if (player1Winners == 0 && player2Winners == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) player1Winners / (((double) player1Winners + (double) player2Winners) / 100));
        }
        progressBarWinners.setProgress(progress);

        if (player1Aces == 0 && player2Aces == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) player1Aces / (((double) player1Aces + (double) player2Aces) / 100));
        }
        progressBarAces.setProgress(progress);

        if (player1Faults == 0 && player2Faults == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) player1Faults / (((double) player1Faults + (double) player2Faults) / 100));
        }
        progressBarFaults.setProgress(progress);

        if (player1DoubleFaults == 0 && player2DoubleFaults == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) player1DoubleFaults / (((double) player1DoubleFaults + (double) player2DoubleFaults) / 100));
        }
        progressBarDoubleFaults.setProgress(progress);
        if (player1ForcedErrors == 0 && player2ForcedErrors == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) player1ForcedErrors / (((double) player1ForcedErrors + (double) player2ForcedErrors) / 100));
        }
        progressBarForcedErrors.setProgress(progress);
        if (player1UnforcedErrors == 0 && player2UnforcedErrors == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) player1UnforcedErrors / (((double) player1UnforcedErrors + (double) player2UnforcedErrors) / 100));
        }
        progressBarUnforcedErrors.setProgress(progress);
    }

    private void printStatistic() {

        /**
         * Player 1 statistic
         */
        TextView textViewPlayer1Winners = (TextView) findViewById(R.id.statistics_winners_player_1);
        TextView textViewPlayer1Aces = (TextView) findViewById(R.id.statistics_aces_player_1);
        TextView textViewPlayer1Fault = (TextView) findViewById(R.id.statistics_faults_player_1);
        TextView textViewPlayer1DoubleFault = (TextView) findViewById(R.id.statistics_double_faults_player_1);
        TextView textViewPlayer1ForcedErrors = (TextView) findViewById(R.id.statistics_forced_errors_player_1);
        TextView textViewPlayer1UnforcedErrors = (TextView) findViewById(R.id.statistics_unforced_errors_player_1);
        textViewPlayer1Winners.setText(String.valueOf(player1Winners));
        textViewPlayer1Aces.setText(String.valueOf(player1Aces));
        textViewPlayer1Fault.setText(String.valueOf(player1Faults));
        textViewPlayer1DoubleFault.setText(String.valueOf(player1DoubleFaults));
        textViewPlayer1ForcedErrors.setText(String.valueOf(player1ForcedErrors));
        textViewPlayer1UnforcedErrors.setText(String.valueOf(player1UnforcedErrors));

        /**
         * Player 2 statistic
         */
        TextView textViewPlayer2Winners = (TextView) findViewById(R.id.statistics_winners_player_2);
        TextView textViewPlayer2Aces = (TextView) findViewById(R.id.statistics_aces_player_2);
        TextView textViewPlayer2Fault = (TextView) findViewById(R.id.statistics_faults_player_2);
        TextView textViewPlayer2DoubleFault = (TextView) findViewById(R.id.statistics_double_faults_player_2);
        TextView textViewPlayer2ForcedErrors = (TextView) findViewById(R.id.statistics_forced_errors_player_2);
        TextView textViewPlayer2UnforcedErrors = (TextView) findViewById(R.id.statistics_unforced_errors_player_2);
        textViewPlayer2Winners.setText(String.valueOf(player2Winners));
        textViewPlayer2Aces.setText(String.valueOf(player2Aces));
        textViewPlayer2Fault.setText(String.valueOf(player2Faults));
        textViewPlayer2DoubleFault.setText(String.valueOf(player2DoubleFaults));
        textViewPlayer2ForcedErrors.setText(String.valueOf(player2ForcedErrors));
        textViewPlayer2UnforcedErrors.setText(String.valueOf(player2UnforcedErrors));


    }
}
