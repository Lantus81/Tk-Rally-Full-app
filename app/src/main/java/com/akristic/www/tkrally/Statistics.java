package com.akristic.www.tkrally;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Statistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        printStatistic();
    }

    public void printStatistic() {
        /**
         * show statistic and hide buttons
         */

        /**
         * Player 1 statistic
         */
        TextView textViewPlayer1Winners = (TextView) findViewById(R.id.textview_player1_winners);
        TextView textViewPlayer1Aces = (TextView) findViewById(R.id.textview_player1_aces);
        TextView textViewPlayer1Fault = (TextView) findViewById(R.id.textview_player1_faults);
        TextView textViewPlayer1DoubleFault = (TextView) findViewById(R.id.textview_player1_double_faults);
        TextView textViewPlayer1ForcedErrors = (TextView) findViewById(R.id.textview_player1_forced_errors);
        TextView textViewPlayer1UnforcedErrors = (TextView) findViewById(R.id.textview_player1_unforced_errors);
        textViewPlayer1Winners.setText(NavigationScoreKeeperActivity.winnerPlayer1 + getString(R.string.winners) + NavigationScoreKeeperActivity.winnerPlayer2);
        textViewPlayer1Aces.setText(NavigationScoreKeeperActivity.acePlayer1 + getString(R.string.aces) + NavigationScoreKeeperActivity.acePlayer2);
        textViewPlayer1Fault.setText(NavigationScoreKeeperActivity.faultPlayer1 + getString(R.string.faults) + NavigationScoreKeeperActivity.faultPlayer2);
        textViewPlayer1DoubleFault.setText(NavigationScoreKeeperActivity.doubleFaultPlayer1 + getString(R.string.double_faults) + NavigationScoreKeeperActivity.doubleFaultPlayer2);
        textViewPlayer1ForcedErrors.setText(NavigationScoreKeeperActivity.forcedErrorPlayer1 + getString(R.string.forced_errors) + NavigationScoreKeeperActivity.forcedErrorPlayer2);
        textViewPlayer1UnforcedErrors.setText(NavigationScoreKeeperActivity.unforcedErrorPlayer1 + getString(R.string.unforced_errors) + NavigationScoreKeeperActivity.unforcedErrorPlayer2);

    }
}
