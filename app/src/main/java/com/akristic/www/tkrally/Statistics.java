package com.akristic.www.tkrally;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.cast.CastRemoteDisplay;


public class Statistics extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        printStatistic();
        TextView textViewPlayer1Name = (TextView) findViewById(R.id.statistic_player1_name);
        TextView textViewPlayer2Name = (TextView) findViewById(R.id.statistic_player2_name);
        if (NavigationScoreKeeperActivity.namePlayer1 != null) {
            textViewPlayer1Name.setText(NavigationScoreKeeperActivity.namePlayer1);
        }
        if (NavigationScoreKeeperActivity.namePlayer2 != null) {
            textViewPlayer2Name.setText(NavigationScoreKeeperActivity.namePlayer2);
        }
        ImageView imageViewPlayer1Image = (ImageView) findViewById(R.id.statistics_image_player_1);
        ImageView imageViewPlayer2Image = (ImageView) findViewById(R.id.statistics_image_player_2);
        if (PlayerEditorActivity.BITMAP_PLAYER1 != null) {
            imageViewPlayer1Image.setImageBitmap(PlayerEditorActivity.BITMAP_PLAYER1);
        }
        if (PlayerEditorActivity.BITMAP_PLAYER2 != null) {
            imageViewPlayer2Image.setImageBitmap(PlayerEditorActivity.BITMAP_PLAYER2);
        }
        setProgressBars();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            AdView mAdView = (AdView)findViewById(R.id.statistic_banner);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
    }

    private void setProgressBars() {
        int progress;
        ProgressBar progressBarWinners = (ProgressBar) findViewById(R.id.statistics_bar_winners);
        ProgressBar progressBarAces = (ProgressBar) findViewById(R.id.statistics_bar_aces);
        ProgressBar progressBarFaults = (ProgressBar) findViewById(R.id.statistics_bar_faults);
        ProgressBar progressBarDoubleFaults = (ProgressBar) findViewById(R.id.statistics_bar_double_faults);
        ProgressBar progressBarForcedErrors = (ProgressBar) findViewById(R.id.statistics_bar_forced_errors);
        ProgressBar progressBarUnforcedErrors = (ProgressBar) findViewById(R.id.statistics_bar_unforced_errors);
        if (NavigationScoreKeeperActivity.winnerPlayer1 == 0 && NavigationScoreKeeperActivity.winnerPlayer2 == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) NavigationScoreKeeperActivity.winnerPlayer1 / (((double) NavigationScoreKeeperActivity.winnerPlayer1 + (double) NavigationScoreKeeperActivity.winnerPlayer2) / 100));
        }
            progressBarWinners.setProgress(progress);

        if (NavigationScoreKeeperActivity.acePlayer1 == 0 && NavigationScoreKeeperActivity.acePlayer2 == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) NavigationScoreKeeperActivity.acePlayer1 / (((double) NavigationScoreKeeperActivity.acePlayer1 + (double) NavigationScoreKeeperActivity.acePlayer2) / 100));
        }
        progressBarAces.setProgress(progress);

        if (NavigationScoreKeeperActivity.faultPlayer1 == 0 && NavigationScoreKeeperActivity.faultPlayer2 == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) NavigationScoreKeeperActivity.faultPlayer1 / (((double) NavigationScoreKeeperActivity.faultPlayer1 + (double) NavigationScoreKeeperActivity.faultPlayer2) / 100));
        }
        progressBarFaults.setProgress(progress);

        if (NavigationScoreKeeperActivity.doubleFaultPlayer1 == 0 && NavigationScoreKeeperActivity.doubleFaultPlayer2 == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) NavigationScoreKeeperActivity.doubleFaultPlayer1 / (((double) NavigationScoreKeeperActivity.doubleFaultPlayer1 + (double) NavigationScoreKeeperActivity.doubleFaultPlayer2) / 100));
        }
        progressBarDoubleFaults.setProgress(progress);
        if (NavigationScoreKeeperActivity.forcedErrorPlayer1 == 0 && NavigationScoreKeeperActivity.forcedErrorPlayer2 == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) NavigationScoreKeeperActivity.forcedErrorPlayer1 / (((double) NavigationScoreKeeperActivity.forcedErrorPlayer1 + (double) NavigationScoreKeeperActivity.forcedErrorPlayer2) / 100));
        }
        progressBarForcedErrors.setProgress(progress);
        if (NavigationScoreKeeperActivity.unforcedErrorPlayer1 == 0 && NavigationScoreKeeperActivity.unforcedErrorPlayer2 == 0) {
            progress = 50;
        } else {
            progress = (int) ((double) NavigationScoreKeeperActivity.unforcedErrorPlayer1 / (((double) NavigationScoreKeeperActivity.unforcedErrorPlayer1 + (double) NavigationScoreKeeperActivity.unforcedErrorPlayer2) / 100));
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
        textViewPlayer1Winners.setText(String.valueOf(NavigationScoreKeeperActivity.winnerPlayer1));
        textViewPlayer1Aces.setText(String.valueOf(NavigationScoreKeeperActivity.acePlayer1));
        textViewPlayer1Fault.setText(String.valueOf(NavigationScoreKeeperActivity.faultPlayer1));
        textViewPlayer1DoubleFault.setText(String.valueOf(NavigationScoreKeeperActivity.doubleFaultPlayer1));
        textViewPlayer1ForcedErrors.setText(String.valueOf(NavigationScoreKeeperActivity.forcedErrorPlayer1));
        textViewPlayer1UnforcedErrors.setText(String.valueOf(NavigationScoreKeeperActivity.unforcedErrorPlayer1));

        /**
         * Player 2 statistic
         */
        TextView textViewPlayer2Winners = (TextView) findViewById(R.id.statistics_winners_player_2);
        TextView textViewPlayer2Aces = (TextView) findViewById(R.id.statistics_aces_player_2);
        TextView textViewPlayer2Fault = (TextView) findViewById(R.id.statistics_faults_player_2);
        TextView textViewPlayer2DoubleFault = (TextView) findViewById(R.id.statistics_double_faults_player_2);
        TextView textViewPlayer2ForcedErrors = (TextView) findViewById(R.id.statistics_forced_errors_player_2);
        TextView textViewPlayer2UnforcedErrors = (TextView) findViewById(R.id.statistics_unforced_errors_player_2);
        textViewPlayer2Winners.setText(String.valueOf(NavigationScoreKeeperActivity.winnerPlayer2));
        textViewPlayer2Aces.setText(String.valueOf(NavigationScoreKeeperActivity.acePlayer2));
        textViewPlayer2Fault.setText(String.valueOf(NavigationScoreKeeperActivity.faultPlayer2));
        textViewPlayer2DoubleFault.setText(String.valueOf(NavigationScoreKeeperActivity.doubleFaultPlayer2));
        textViewPlayer2ForcedErrors.setText(String.valueOf(NavigationScoreKeeperActivity.forcedErrorPlayer2));
        textViewPlayer2UnforcedErrors.setText(String.valueOf(NavigationScoreKeeperActivity.unforcedErrorPlayer2));


    }
}
