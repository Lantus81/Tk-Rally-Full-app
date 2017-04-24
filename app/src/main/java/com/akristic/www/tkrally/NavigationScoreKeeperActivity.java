package com.akristic.www.tkrally;

import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akristic.www.tkrally.data.PlayerContract;

import java.util.ArrayList;
import java.util.Locale;


import static com.akristic.www.tkrally.R.string.tiebreak;

public class NavigationScoreKeeperActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int pointsPlayer1 = 0;
    private int pointsPlayer2 = 0;
    private int gamesPlayer1 = 0;
    private int gamesPlayer2 = 0;
    private int setsPlayer1 = 0;
    private int setsPlayer2 = 0;
    private int numberOfSetsForWin = 2;
    private int numberOfServeInTieBreak = 0;
    private boolean serveOfPlayer = true;
    private boolean serveOfPlayerInTieBreak = true;
    private boolean tieBreak = false;
    private boolean firstFault = true;
    private boolean matchWon = false;
    private boolean tiebreakFinal = true;
    private boolean playAdvantageFinal = true;
    private int NUMBER_OF_GAMES_FOR_WIN = 6;
    private int setsScore[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    /**
     * statistic variables
     */
    static int winnerPlayer1 = 0;
    static int acePlayer1 = 0;
    static int faultPlayer1 = 0;
    static int doubleFaultPlayer1 = 0;
    static int forcedErrorPlayer1 = 0;
    static int unforcedErrorPlayer1 = 0;
    static int winnerPlayer2 = 0;
    static int acePlayer2 = 0;
    static int faultPlayer2 = 0;
    static int doubleFaultPlayer2 = 0;
    static int forcedErrorPlayer2 = 0;
    static int unforcedErrorPlayer2 = 0;

    static String namePlayer1 = "Player 1";
    static String namePlayer2 = "Player 2";
    /**
     * views
     */
    private TextView namePlayer1TextView;
    private TextView namePlayer2TextView;
    private TextView pointsViewPlayer1;
    private TextView pointsViewPlayer2;
    private TextView setsViewPlayer1;
    private TextView setsViewPlayer2;
    private TextView textViewDeuce;
    private TextView textViewServePlayer1;
    private TextView textViewServePlayer2;
    private TextView gamesViewPlayer1;
    private TextView gamesViewPlayer2;
    private Button buttonAcePlayer1;
    private Button buttonAcePlayer2;
    private Button buttonFaultPlayer1;
    private Button buttonFaultPlayer2;
    private ConstraintLayout buttonsLayoutPlayer1;
    private ConstraintLayout buttonsLayoutPlayer2;
    private ImageView mImagePlayer1;
    private ImageView mImagePlayer2;

    private ArrayList<UndoRedo> savedState = new ArrayList<>();
    private int currentUndoIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //* hide buttons at onCreate
        buttonsLayoutPlayer1 = (ConstraintLayout) findViewById(R.id.buttons_layout_holder_player_1);
        buttonsLayoutPlayer2 = (ConstraintLayout) findViewById(R.id.buttons_layout_holder_player_2);
        buttonAcePlayer1 = (Button) findViewById(R.id.button_player1_ace);
        buttonAcePlayer2 = (Button) findViewById(R.id.button_player2_ace);
        buttonFaultPlayer2 = (Button) findViewById(R.id.button_player2_fault);
        buttonFaultPlayer1 = (Button) findViewById(R.id.button_player1_fault);
        pointsViewPlayer1 = (TextView) findViewById(R.id.player_1_points);
        pointsViewPlayer2 = (TextView) findViewById(R.id.player_2_points);
        textViewDeuce = (TextView) findViewById(R.id.deuce);
        textViewServePlayer1 = (TextView) findViewById(R.id.serve_color_Player1);
        textViewServePlayer2 = (TextView) findViewById(R.id.serve_color_Player2);
        gamesViewPlayer1 = (TextView) findViewById(R.id.player_1_games);
        gamesViewPlayer2 = (TextView) findViewById(R.id.player_2_games);
        setsViewPlayer1 = (TextView) findViewById(R.id.player_1_sets);
        setsViewPlayer2 = (TextView) findViewById(R.id.player_2_sets);
        namePlayer1TextView = (TextView) findViewById(R.id.player1_name);
        namePlayer2TextView = (TextView) findViewById(R.id.player2_name);
        mImagePlayer1 = (ImageView) findViewById(R.id.score_image_player1);
        mImagePlayer2 = (ImageView) findViewById(R.id.score_image_player2);


        if (savedInstanceState != null) {
            savedState = savedInstanceState.getParcelableArrayList("UNDO_REDO");
            currentUndoIndex = savedInstanceState.getInt("currentUndoIndex");
            setsScore = savedInstanceState.getIntArray("setsScore");
            pointsPlayer1 = savedInstanceState.getInt("pointsPlayer1");
            pointsPlayer2 = savedInstanceState.getInt("pointsPlayer2");
            gamesPlayer1 = savedInstanceState.getInt("gamesPlayer1");
            gamesPlayer2 = savedInstanceState.getInt("gamesPlayer2");
            setsPlayer1 = savedInstanceState.getInt("setsPlayer1");
            setsPlayer2 = savedInstanceState.getInt("setsPlayer2");
            numberOfSetsForWin = savedInstanceState.getInt("numberOfSetsForWin");
            numberOfServeInTieBreak = savedInstanceState.getInt("numberOfServeInTieBreak");
            serveOfPlayer = savedInstanceState.getBoolean("serveOfPlayer");
            serveOfPlayerInTieBreak = savedInstanceState.getBoolean("serveOfPlayerInTieBreak");
            tieBreak = savedInstanceState.getBoolean("tieBreak");
            firstFault = savedInstanceState.getBoolean("firstFault");
            matchWon = savedInstanceState.getBoolean("matchWon");
            checkIfPlayerHasWinMatch();
            tiebreakFinal = savedInstanceState.getBoolean("tiebreakFinal");
            pointsViewPlayer1.setText(savedInstanceState.getString("pointsTextViewPlayer1"));
            pointsViewPlayer2.setText(savedInstanceState.getString("pointsTextViewPlayer2"));
            gamesViewPlayer1.setText(String.valueOf(gamesPlayer1));
            gamesViewPlayer2.setText(String.valueOf(gamesPlayer2));
            setsViewPlayer1.setText(String.valueOf(setsPlayer1));
            setsViewPlayer2.setText(String.valueOf(setsPlayer2));

        }
        setPreferences();
        changePlayersNames();
        setPlayerPictures();
        showHideSetsView();
        if (!tieBreak) {
            serveChange(serveOfPlayer);
        } else {
            serveChange(serveOfPlayerInTieBreak);
        }
        if (winnerPlayer1 == 0 && winnerPlayer2 == 0 && acePlayer1 == 0 &&
                acePlayer2 == 0 && faultPlayer1 == 0 && faultPlayer2 == 0 &&
                forcedErrorPlayer1 == 0 && forcedErrorPlayer2 == 0 && unforcedErrorPlayer1 == 0 && unforcedErrorPlayer2 == 0) {
            saveUndoState(); //* set zero index state for undo and redo ArrayList
        }

    }

    public void openStatistics(View v) {
        Intent statisticsIntent = new Intent(getApplicationContext(), Statistics.class);
        startActivity(statisticsIntent);
    }

    private void setPreferences() {
        //* manage preferences of app. Tiebreak and number of sets for win
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String setsNumber = sharedPrefs.getString(
                getString(R.string.settings_number_of_sets_key),
                getString(R.string.settings_number_of_sets_default));
        numberOfSetsForWin = Integer.parseInt(setsNumber);
        String tiebreakFinalString = sharedPrefs.getString(
                getString(R.string.settings_tiebreak_key),
                getString(R.string.settings_tiebreak_default));
        if (tiebreakFinalString.equals("No")) {
            tiebreakFinal = false;
        } else {
            tiebreakFinal = true;
        }
        String gamesNumber = sharedPrefs.getString(
                getString(R.string.settings_number_of_games_key),
                getString(R.string.settings_number_of_games_default));
        NUMBER_OF_GAMES_FOR_WIN = Integer.parseInt(gamesNumber);
        String advantageFinalString = sharedPrefs.getString(
                getString(R.string.settings_advantage_key),
                getString(R.string.settings_advantage_default));
        if (advantageFinalString.equals("No")) {
            playAdvantageFinal = false;
        } else {
            playAdvantageFinal = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putIntArray("setsScore", setsScore);
        state.putParcelableArrayList("UNDO_REDO", savedState);
        state.putInt("currentUndoIndex", currentUndoIndex);
        state.putInt("pointsPlayer1", pointsPlayer1);
        state.putInt("pointsPlayer2", pointsPlayer2);
        state.putInt("gamesPlayer1", gamesPlayer1);
        state.putInt("gamesPlayer2", gamesPlayer2);
        state.putInt("setsPlayer1", setsPlayer1);
        state.putInt("setsPlayer2", setsPlayer2);
        state.putInt("numberOfSetsForWin", numberOfSetsForWin);
        state.putInt("numberOfServeInTieBreak", numberOfServeInTieBreak);
        state.putBoolean("serveOfPlayer", serveOfPlayer);
        state.putBoolean("serveOfPlayerInTieBreak", serveOfPlayerInTieBreak);
        state.putBoolean("tieBreak", tieBreak);
        state.putBoolean("firstFault", firstFault);
        state.putBoolean("matchWon", matchWon);
        state.putBoolean("tiebreakFinal", tiebreakFinal);
        state.putString("pointsTextViewPlayer1", pointsViewPlayer1.getText().toString());
        state.putString("pointsTextViewPlayer2", pointsViewPlayer2.getText().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        changePlayersNames();
        setPlayerPictures();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        if (id == R.id.bottombaritem_reset) {
            resetAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_player_manager) {
            Intent playerIntent = new Intent(this, PlayerCatalogActivity.class);
            startActivity(playerIntent);
        } else if (id == R.id.nav_quiz) {
            Intent quizIntent = new Intent(this, QuizActivity.class);
            startActivity(quizIntent);
        } else if (id == R.id.nav_phone) {
            Uri number = Uri.parse("tel:385959034421");
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);
        } else if (id == R.id.nav_email) {
            Intent intentemail = new Intent(Intent.ACTION_SENDTO);
            intentemail.setData(Uri.parse("mailto:info@revoloop.hr"));
            if (intentemail.resolveActivity(getPackageManager()) != null) {
                startActivity(intentemail);
            }
        } else if (id == R.id.nav_map) {
            String uri = String.format(Locale.ENGLISH, "https://www.google.hr/maps/place/Teniski+klub+Rally/@45.8396874,16.0523137,17z/data=!4m5!3m4!1s0x0:0x3f14a0231a6f2e3e!8m2!3d45.8395472!4d16.0550192?hl=en");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, R.string.Error_no_app, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_facebook) {
            Intent intentFace = getOpenFacebookIntent(this);
            try {
                startActivity(intentFace);
            } catch (Exception e) {
                Toast.makeText(this, R.string.Error_no_app, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_web_page) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kvalitetnaskolatenisa.com"));

            try {
                startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(this, R.string.Error_no_app, Toast.LENGTH_SHORT).show();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent getOpenFacebookIntent(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/tkrally/"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tkrally/?fref=ts"));
        }
    }

    /**
     * manage tiebreak service
     */
    public void manageTiebreakService() {
        if (tieBreak) {
            numberOfServeInTieBreak++;
            if (pointsPlayer1 + pointsPlayer2 == 1) {
                serveOfPlayerInTieBreak = !serveOfPlayerInTieBreak;
                numberOfServeInTieBreak = 0;
            }
            if (numberOfServeInTieBreak > 1) {
                serveOfPlayerInTieBreak = !serveOfPlayerInTieBreak;
                numberOfServeInTieBreak = 0;
            }
            serveChange(serveOfPlayerInTieBreak);
        }
    }

    /**
     * Displays the given points for Player 1.
     * manage tiebreak
     */
    public void displayPointsForPlayer1(int points) {
        textViewDeuce.setText("");
        if (points <= 40 || tieBreak) {
            pointsViewPlayer1.setText(String.valueOf(points));
            if (pointsPlayer1 == 40 && pointsPlayer2 == 40 && !tieBreak) {
                textViewDeuce.setText(getString(R.string.deuce));
            }
            //* manage tiebreak service
            manageTiebreakService();
            //* set text if it is a break point
            if (!serveOfPlayer && pointsPlayer1 == 40 && !tieBreak && pointsPlayer2 < 40) {
                textViewDeuce.setText(getString(R.string.break_point_player1));
            }
            //* check if player has Win the tiebreak
            if (pointsPlayer1 >= 6 && pointsPlayer1 > pointsPlayer2 + 1 && tieBreak) {
                gamesPlayer1++;
                displayGamesForPlayer1(gamesPlayer1);
                tieBreak = false;
            }
            return;
        }

        if (points == pointsPlayer2) {
            pointsViewPlayer1.setText(getString(R.string.points_40));
            pointsViewPlayer2.setText(getString(R.string.points_40));
            textViewDeuce.setText(getString(R.string.deuce) + " (" + (pointsPlayer1 - 39) + ")");
            return;
        }
        if (points == pointsPlayer2 + 1) {
            pointsViewPlayer1.setText(getString(R.string.advance));
            pointsViewPlayer2.setText("");
            if (!serveOfPlayer) {
                textViewDeuce.setText(getString(R.string.break_point_player1));
            }
            return;
        }
        if (points == pointsPlayer2 + 2) {
            gamesPlayer1++;
            displayGamesForPlayer1(gamesPlayer1);
        }
    }

    /**
     * Displays the given points for Player 2.
     * manage tiebreak
     */
    public void displayPointsForPlayer2(int points) {

        textViewDeuce.setText("");
        if (points <= 40 || tieBreak) {
            pointsViewPlayer2.setText(String.valueOf(points));
            if (pointsPlayer1 == 40 && pointsPlayer2 == 40 && !tieBreak) {
                textViewDeuce.setText(getString(R.string.deuce));
            }
            //* manage tiebreak service
            manageTiebreakService();
            //* set text if it is a break point
            if (serveOfPlayer && pointsPlayer2 == 40 && !tieBreak && pointsPlayer1 < 40) {
                textViewDeuce.setText(getString(R.string.break_point_player2));
            }
            //* check if player has Win the tiebreak
            if (pointsPlayer2 >= 6 && pointsPlayer2 > pointsPlayer1 + 1 && tieBreak) {
                gamesPlayer2++;
                displayGamesForPlayer2(gamesPlayer2);
                tieBreak = false;
            }
            return;
        }
        if (points == pointsPlayer1) {
            pointsViewPlayer1.setText(getString(R.string.points_40));
            pointsViewPlayer2.setText(getString(R.string.points_40));
            textViewDeuce.setText(getString(R.string.deuce) + " (" + (pointsPlayer1 - 39) + ")");
            return;
        }
        if (points == pointsPlayer1 + 1) {
            pointsViewPlayer1.setText("");
            pointsViewPlayer2.setText(getString(R.string.advance));
            if (serveOfPlayer) {
                textViewDeuce.setText(getString(R.string.break_point_player2));
            }
            return;
        }
        if (points == pointsPlayer1 + 2) {
            gamesPlayer2++;
            displayGamesForPlayer2(gamesPlayer2);
        }
    }


    /**
     * Displays the score of GAMES for Player 1. and reset points
     */
    public void displayGamesForPlayer1(int points) {
        gamesViewPlayer1.setText(String.valueOf(points));
        pointsPlayer1 = 0;
        pointsPlayer2 = 0;
        pointsViewPlayer1.setText("0");
        pointsViewPlayer2.setText("0");
        textViewDeuce.setText(getString(R.string.game_for_player1));
        //* change serve only if not playing tiebreak
        serveOfPlayer = !serveOfPlayer;
        serveChange(serveOfPlayer);
        //* check if player has win the set
        if (gamesPlayer1 >= NUMBER_OF_GAMES_FOR_WIN && gamesPlayer1 > gamesPlayer2 + 1 || gamesPlayer1 == NUMBER_OF_GAMES_FOR_WIN + 1) {
            setsPlayer1++;
            displaySetsForPlayer1(setsPlayer1);
            return;
        }
        checkForTiebreak();
    }

    /**
     * Displays the score of GAMES for Player 2. and reset points
     */
    public void displayGamesForPlayer2(int points) {
        gamesViewPlayer2.setText(String.valueOf(points));
        pointsPlayer1 = 0;
        pointsPlayer2 = 0;
        pointsViewPlayer1.setText("0");
        pointsViewPlayer2.setText("0");
        textViewDeuce.setText(getString(R.string.game_for_player2));
        //* change serve only if not playing tiebreak
        serveOfPlayer = !serveOfPlayer;
        serveChange(serveOfPlayer);
        //* check if player has win the set
        if (gamesPlayer2 >= NUMBER_OF_GAMES_FOR_WIN && gamesPlayer2 > gamesPlayer1 + 1 || gamesPlayer2 == NUMBER_OF_GAMES_FOR_WIN + 1) {
            setsPlayer2++;
            displaySetsForPlayer2(setsPlayer2);
            return;
        }
        checkForTiebreak();
    }

    /**
     * check if tiebreak needs to be played
     */
    private void checkForTiebreak() {

        if (gamesPlayer1 == NUMBER_OF_GAMES_FOR_WIN && gamesPlayer2 == NUMBER_OF_GAMES_FOR_WIN && tiebreakFinal) { //* tiebreakFinal is set in preferences settings
            tieBreak = true;
            textViewDeuce.setText(getString(tiebreak));
            //* in tiebreak first serve has player who is next on serve in order and tiebreak is like one game,
            //* so they change serve in tiebreak but continue with normal serving after.
            serveOfPlayerInTieBreak = serveOfPlayer;
            serveChange(serveOfPlayerInTieBreak);
        }
    }

    /**
     * Displays the score of SETS for Player 1. and reset points for games
     */
    public void displaySetsForPlayer1(int points) {
        //* Save GAMES before resetting to 0
        displaySetsView();
        // * set Games and POINTS to 0 and manage text
        gamesPlayer1 = 0;
        gamesPlayer2 = 0;
        gamesViewPlayer1.setText("0");
        gamesViewPlayer2.setText("0");
        pointsViewPlayer1.setText("0");
        pointsViewPlayer2.setText("0");
        setsViewPlayer1.setText(String.valueOf(points));
        textViewDeuce.setText(getString(R.string.set_for_player1));
        //* check if player has WIN the match
        //* hide buttons
        checkIfPlayerHasWinMatch();
        if (matchWon) {
            textViewDeuce.setText(getString(R.string.game_set_match_player1));
            textViewServePlayer1.setText(getString(R.string.winner_player));
            textViewServePlayer1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            textViewServePlayer2.setBackgroundColor(Color.TRANSPARENT);
            textViewServePlayer2.setText("");
        }

    }

    /**
     * Displays the score of SETS for Player 2. and reset points for games
     */
    public void displaySetsForPlayer2(int points) {
        //* Save GAMES before resetting to 0
        displaySetsView();
        //* set Games and POINTS to 0 and manage text
        gamesPlayer1 = 0;
        gamesPlayer2 = 0;
        gamesViewPlayer1.setText("0");
        gamesViewPlayer2.setText("0");
        pointsViewPlayer1.setText("0");
        pointsViewPlayer2.setText("0");
        setsViewPlayer2.setText(String.valueOf(points));
        textViewDeuce.setText(getString(R.string.set_for_player2));
        //* Check if player has WIN the match
        //* hides buttons
        checkIfPlayerHasWinMatch();
        if (matchWon) {
            textViewDeuce.setText(getString(R.string.game_set_match_player2));
            textViewServePlayer2.setText(getString(R.string.winner_player));
            textViewServePlayer2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            textViewServePlayer1.setBackgroundColor(Color.TRANSPARENT);
            textViewServePlayer1.setText("");

        }
    }

    private void showHideSetsView() {
        if (setsPlayer1 + setsPlayer2 == 0) {
            TextView saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_1);
            TextView saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_1);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_2);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_2);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_3);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_3);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_4);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_4);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_5);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_5);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
        }
        if (setsPlayer1 + setsPlayer2 == 1) {
            TextView saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_1);
            TextView saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_1);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[0]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[1]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_2);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_2);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_3);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_3);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_4);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_4);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_5);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_5);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
        }
        if (setsPlayer1 + setsPlayer2 == 2) {
            TextView saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_1);
            TextView saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_1);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[0]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[1]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_2);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_2);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[2]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[3]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_3);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_3);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_4);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_4);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_5);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_5);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
        }
        if (setsPlayer1 + setsPlayer2 == 3) {
            TextView saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_1);
            TextView saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_1);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[0]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[1]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_2);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_2);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[2]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[3]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_3);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_3);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[4]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[5]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_4);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_4);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_5);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_5);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
        }
        if (setsPlayer1 + setsPlayer2 == 4) {
            TextView saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_1);
            TextView saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_1);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[0]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[1]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_2);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_2);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[2]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[3]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_3);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_3);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[4]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[5]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_4);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_4);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[6]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[7]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_5);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_5);
            saveSetViewPlayer1.setVisibility(View.INVISIBLE);
            saveSetViewPlayer2.setVisibility(View.INVISIBLE);
        }
        if (setsPlayer1 + setsPlayer2 == 5) {
            TextView saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_1);
            TextView saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_1);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[0]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[1]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_2);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_2);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[2]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[3]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_3);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_3);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[4]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[5]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_4);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_4);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[6]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[7]));
            saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_5);
            saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_5);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(setsScore[8]));
            saveSetViewPlayer2.setText(String.valueOf(setsScore[9]));
        }
    }

    private void displaySetsView() {

        if (setsPlayer1 + setsPlayer2 == 1) {
            TextView saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_1);
            TextView saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_1);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(gamesPlayer1));
            saveSetViewPlayer2.setText(String.valueOf(gamesPlayer2));
            setsScore[0] = gamesPlayer1;
            setsScore[1] = gamesPlayer2;

        }
        if (setsPlayer1 + setsPlayer2 == 2) {
            TextView saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_2);
            TextView saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_2);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(gamesPlayer1));
            saveSetViewPlayer2.setText(String.valueOf(gamesPlayer2));
            setsScore[2] = gamesPlayer1;
            setsScore[3] = gamesPlayer2;

        }
        if (setsPlayer1 + setsPlayer2 == 3) {
            TextView saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_3);
            TextView saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_3);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(gamesPlayer1));
            saveSetViewPlayer2.setText(String.valueOf(gamesPlayer2));
            setsScore[4] = gamesPlayer1;
            setsScore[5] = gamesPlayer2;

        }
        if (setsPlayer1 + setsPlayer2 == 4) {
            TextView saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_4);
            TextView saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_4);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(gamesPlayer1));
            saveSetViewPlayer2.setText(String.valueOf(gamesPlayer2));
            setsScore[6] = gamesPlayer1;
            setsScore[7] = gamesPlayer2;

        }
        if (setsPlayer1 + setsPlayer2 == 5) {
            TextView saveSetViewPlayer1 = (TextView) findViewById(R.id.save_sets_player1_set_5);
            TextView saveSetViewPlayer2 = (TextView) findViewById(R.id.save_sets_player2_set_5);
            saveSetViewPlayer1.setVisibility(View.VISIBLE);
            saveSetViewPlayer2.setVisibility(View.VISIBLE);
            saveSetViewPlayer1.setText(String.valueOf(gamesPlayer1));
            saveSetViewPlayer2.setText(String.valueOf(gamesPlayer2));
            setsScore[8] = gamesPlayer1;
            setsScore[9] = gamesPlayer2;
        }

    }

    private void checkIfPlayerHasWinMatch() {
        if (numberOfSetsForWin == setsPlayer2 || numberOfSetsForWin == setsPlayer1) {
            matchWon = true;
            buttonsLayoutPlayer1.setVisibility(View.GONE);
            buttonsLayoutPlayer2.setVisibility(View.GONE);
        } else {
            buttonsLayoutPlayer1.setVisibility(View.VISIBLE);
            buttonsLayoutPlayer2.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ------------------------Manage points for  Player 1.---------------------------------------
     */
    public void managePointsPlayer1() {
        if (!tieBreak) {
            if (pointsPlayer1 == 0 | pointsPlayer1 == 15) {
                pointsPlayer1 = pointsPlayer1 + 15;
                displayPointsForPlayer1(pointsPlayer1);
                return;
            }
            if (pointsPlayer1 == 30) {
                pointsPlayer1 = pointsPlayer1 + 10;
                displayPointsForPlayer1(pointsPlayer1);
                return;
            }
            if (pointsPlayer1 >= 40) {
                if (playAdvantageFinal) {
                    pointsPlayer1++;
                    if (pointsPlayer2 < 40) {
                        gamesPlayer1++;
                        displayGamesForPlayer1(gamesPlayer1);
                        return;
                    }
                    if (pointsPlayer1 >= pointsPlayer2) {
                        displayPointsForPlayer1(pointsPlayer1);
                        return;
                    }
                    if (pointsPlayer1 == pointsPlayer2 + 2) {
                        gamesPlayer1++;
                        displayGamesForPlayer1(gamesPlayer1);
                        return;
                    }
                } else {
                    gamesPlayer1++;
                    displayGamesForPlayer1(gamesPlayer1);
                    return;
                }
            }
        } else {
            pointsPlayer1++;
            displayPointsForPlayer1(pointsPlayer1);
        }
    }


    /**
     * winner points for player 1
     */
    public void addPointForPlayer1(View v) {
        winnerPlayer1++;
        managePointsPlayer1();
        //* set first fault to 0
        resetFaultButton();
        saveUndoState();
    }

    /**
     * ace points for player 1
     */
    public void addPointForPlayer1Ace(View v) {
        acePlayer1++;
        managePointsPlayer1();
        //* set first fault to 0
        resetFaultButton();
        saveUndoState();
    }

    /**
     * forced error button player 1
     */
    public void addPointForPlayer1ForError(View v) {
        forcedErrorPlayer1++;
        managePointsPlayer2();
        //* set first fault to 0
        resetFaultButton();
        saveUndoState();
    }

    /**
     * unforced error button player 1
     */
    public void addPointForPlayer1UnfError(View v) {
        unforcedErrorPlayer1++;
        managePointsPlayer2();
        //* set first fault to 0
        resetFaultButton();
        saveUndoState();
    }

    /**
     * fault and double fault
     */
    public void addPointForPlayer1Fault(View v) {
        if (firstFault) {
            faultPlayer1++;
            buttonFaultPlayer1.setText(getString(R.string.double_fault));
            firstFault = false;
        } else {
            doubleFaultPlayer1++;
            buttonFaultPlayer1.setText(getString(R.string.fault));
            firstFault = true;
            managePointsPlayer2();
        }
        saveUndoState();
    }

    /**
     * -------------------------------Manage points for  Player 2.-------------------------
     */
    public void managePointsPlayer2() {
        if (!tieBreak) {
            if (pointsPlayer2 == 0 | pointsPlayer2 == 15) {
                pointsPlayer2 = pointsPlayer2 + 15;
                displayPointsForPlayer2(pointsPlayer2);
                return;
            }
            if (pointsPlayer2 == 30) {
                pointsPlayer2 = pointsPlayer2 + 10;
                displayPointsForPlayer2(pointsPlayer2);
                return;
            }
            if (pointsPlayer2 >= 40) {
                if (playAdvantageFinal) {
                    pointsPlayer2++;
                    if (pointsPlayer1 < 40) {
                        gamesPlayer2++;
                        displayGamesForPlayer2(gamesPlayer2);
                        return;
                    }
                    if (pointsPlayer2 >= pointsPlayer1) {
                        displayPointsForPlayer2(pointsPlayer2);
                        return;
                    }
                    if (pointsPlayer2 == pointsPlayer1 + 2) {
                        gamesPlayer2++;
                        displayGamesForPlayer2(gamesPlayer2);
                        return;
                    }
                } else {
                    gamesPlayer2++;
                    displayGamesForPlayer2(gamesPlayer2);
                    return;
                }
            }
        } else {
            pointsPlayer2++;
            displayPointsForPlayer2(pointsPlayer2);
        }
    }

    /**
     * winner points for player 2
     */
    public void addPointForPlayer2(View v) {
        winnerPlayer2++;
        managePointsPlayer2();
        resetFaultButton();
        saveUndoState();
    }

    /**
     * ace points for player 2
     */
    public void addPointForPlayer2Ace(View v) {
        acePlayer1++;
        managePointsPlayer2();
        //** set first fault to 0
        resetFaultButton();
        saveUndoState();
    }

    /**
     * forced error for player 2
     */
    public void addPointForPlayer2ForError(View v) {
        forcedErrorPlayer2++;
        managePointsPlayer1();
        // * set first fault to 0
        resetFaultButton();
        saveUndoState();
    }

    public void addPointForPlayer2UnfError(View v) {
        unforcedErrorPlayer2++;
        managePointsPlayer1();
        //* set first fault to 0
        resetFaultButton();
        saveUndoState();
    }

    /**
     * fault and double fault for player 2
     */
    public void addPointForPlayer2Fault(View v) {
        if (firstFault) {
            faultPlayer2++;
            buttonFaultPlayer2.setText(getString(R.string.double_fault));
            firstFault = false;
        } else {
            doubleFaultPlayer2++;
            buttonFaultPlayer2.setText(getString(R.string.fault));
            firstFault = true;
            managePointsPlayer1();
        }
        saveUndoState();
    }

    private void resetFaultButton() {
        if (!firstFault) {
            firstFault = true;
            buttonFaultPlayer1.setText(getString(R.string.fault));
            buttonFaultPlayer2.setText(getString(R.string.fault));
        }
    }

    private void displayRightFaultButtonText() {
        if (!firstFault && serveOfPlayer) {
            buttonFaultPlayer1.setText(getString(R.string.double_fault));
        }
        if (!firstFault && !serveOfPlayer) {
            buttonFaultPlayer2.setText(getString(R.string.double_fault));
        }
        if (firstFault && serveOfPlayer) {
            buttonFaultPlayer1.setText(getString(R.string.fault));
        }
        if (firstFault && !serveOfPlayer) {
            buttonFaultPlayer2.setText(getString(R.string.fault));
        }
    }

    /**
     * change who serve first
     */

    public void changeServeFirstPlayer(View view) {
        if (winnerPlayer1 == 0 && winnerPlayer2 == 0 && acePlayer1 == 0 &&
                acePlayer2 == 0 && faultPlayer1 == 0 && faultPlayer2 == 0 &&
                forcedErrorPlayer1 == 0 && forcedErrorPlayer2 == 0 && unforcedErrorPlayer1 == 0 && unforcedErrorPlayer2 == 0) {
            serveOfPlayer = !serveOfPlayer;
            serveChange(serveOfPlayer);
            saveUndoState();
        } else {
            Toast.makeText(this, getString(R.string.toast_message_serve), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * change service method
     */

    public void serveChange(boolean serve) {
        if (serve) {

            textViewServePlayer1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            textViewServePlayer2.setBackgroundColor(Color.TRANSPARENT);
            textViewServePlayer1.setText(getString(R.string.serve));
            textViewServePlayer2.setText("");

            buttonAcePlayer2.setVisibility(View.GONE);
            buttonFaultPlayer2.setVisibility(View.GONE);

            buttonAcePlayer1.setVisibility(View.VISIBLE);
            buttonFaultPlayer1.setVisibility(View.VISIBLE);
        } else {

            textViewServePlayer1.setBackgroundColor(Color.TRANSPARENT);
            textViewServePlayer2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            textViewServePlayer1.setText("");
            textViewServePlayer2.setText(getString(R.string.serve));

            buttonAcePlayer2.setVisibility(View.VISIBLE);
            buttonFaultPlayer2.setVisibility(View.VISIBLE);

            buttonAcePlayer1.setVisibility(View.GONE);
            buttonFaultPlayer1.setVisibility(View.GONE);
        }
    }

    /**
     * RESET ALL To default
     */
    public void resetAll() {

        new AlertDialog.Builder(this)
                .setMessage(R.string.scorekeeper_reset_question)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(NavigationScoreKeeperActivity.this, R.string.scorekeeper_reset_match, Toast.LENGTH_SHORT).show();
                        //* set all variables to default state
                        savedState.clear();
                        currentUndoIndex = -1;

                        pointsPlayer1 = 0;
                        pointsPlayer2 = 0;
                        gamesPlayer1 = 0;
                        gamesPlayer2 = 0;
                        setsPlayer1 = 0;
                        setsPlayer2 = 0;
                        numberOfSetsForWin = 2;
                        serveOfPlayer = true;
                        serveOfPlayerInTieBreak = true;
                        tieBreak = false;
                        firstFault = true;
                        matchWon = false;

                        winnerPlayer1 = 0;
                        acePlayer1 = 0;
                        faultPlayer1 = 0;
                        doubleFaultPlayer1 = 0;
                        forcedErrorPlayer1 = 0;
                        unforcedErrorPlayer1 = 0;

                        winnerPlayer2 = 0;
                        acePlayer2 = 0;
                        faultPlayer2 = 0;
                        doubleFaultPlayer2 = 0;
                        forcedErrorPlayer2 = 0;
                        unforcedErrorPlayer2 = 0;
                        //* reset POINTS
                        pointsViewPlayer1.setText("0");
                        pointsViewPlayer2.setText("0");
                        textViewDeuce.setText("");
                        //* reset GAMES
                        gamesViewPlayer1.setText("0");
                        gamesViewPlayer2.setText("0");
                        //* reset SETS
                        setsViewPlayer1.setText("0");
                        setsViewPlayer2.setText("0");
                        //* reset saved SETS
                        showHideSetsView();
                        // * return serve to player 1 first and remove winner text
                        TextView textViewWinner1 = (TextView) findViewById(R.id.serve_color_Player1);
                        textViewWinner1.setText("");
                        TextView textViewWinner2 = (TextView) findViewById(R.id.serve_color_Player2);
                        textViewWinner2.setText("");
                        serveChange(serveOfPlayer);
                        //*show buttons
                        buttonsLayoutPlayer1.setVisibility(View.VISIBLE);
                        buttonsLayoutPlayer2.setVisibility(View.VISIBLE);
                        setPreferences();
                        changePlayersNames();
                        saveUndoState();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    /**
     * changing players names
     *
     * @param v imageView silhouette
     */
    public void changePlayersNames(View v) {
        Intent playersIntent = new Intent(getApplicationContext(), PlayerCatalogActivity.class);
        startActivity(playersIntent);
    }

    public void changePlayersNames() {
        if (PlayerEditorActivity.NAME_PLAYER1 != null) {
            namePlayer1 = PlayerEditorActivity.NAME_PLAYER1;
        }
        if (PlayerEditorActivity.NAME_PLAYER2 != null) {
            namePlayer2 = PlayerEditorActivity.NAME_PLAYER2;
        }
        namePlayer1TextView.setText(namePlayer1);
        namePlayer2TextView.setText(namePlayer2);
    }

    private void setPlayerPictures() {
        /**      String[] projection = {
         PlayerContract.PlayerEntry._ID,
         PlayerContract.PlayerEntry.COLUMN_PLAYER_NAME,
         PlayerContract.PlayerEntry.COLUMN_PLAYER_PICTURE};
         Uri uri = Uri.withAppendedPath(PlayerContract.PlayerEntry.CONTENT_URI,"/0");
         CursorLoader cursor = new CursorLoader(this, uri,
         projection, null, null, null);
         */


        if (PlayerEditorActivity.BITMAP_PLAYER1 != null) {
            mImagePlayer1.setImageBitmap(PlayerEditorActivity.BITMAP_PLAYER1);

        }
        if (PlayerEditorActivity.BITMAP_PLAYER2 != null) {
            mImagePlayer2.setImageBitmap(PlayerEditorActivity.BITMAP_PLAYER2);

        }
    }

    /**
     * Undo and redo methods
     */

    private void saveUndoState() {
        String displayPointsPlayer1 = pointsViewPlayer1.getText().toString();
        String displayGamesPlayer1 = gamesViewPlayer1.getText().toString();
        String displaySetsPlayer1 = setsViewPlayer1.getText().toString();
        String displayPointsPlayer2 = pointsViewPlayer2.getText().toString();
        String displayGamesPlayer2 = gamesViewPlayer2.getText().toString();
        String displaySetsPlayer2 = setsViewPlayer2.getText().toString();
        String displayTextMessage = textViewDeuce.getText().toString();
        currentUndoIndex++;
        if (currentUndoIndex >= savedState.size()) {
            savedState.add(new UndoRedo(pointsPlayer1,
                    pointsPlayer2,
                    gamesPlayer1,
                    gamesPlayer2,
                    setsPlayer1,
                    setsPlayer2,
                    numberOfSetsForWin,
                    numberOfServeInTieBreak,
                    serveOfPlayer,
                    serveOfPlayerInTieBreak,
                    tieBreak,
                    firstFault,
                    matchWon,
                    tiebreakFinal,
                    winnerPlayer1,
                    acePlayer1,
                    faultPlayer1,
                    doubleFaultPlayer1,
                    forcedErrorPlayer1,
                    unforcedErrorPlayer1,
                    winnerPlayer2,
                    acePlayer2,
                    faultPlayer2,
                    doubleFaultPlayer2,
                    forcedErrorPlayer2,
                    unforcedErrorPlayer2,
                    displayPointsPlayer1,
                    displayGamesPlayer1,
                    displaySetsPlayer1,
                    displayPointsPlayer2,
                    displayGamesPlayer2,
                    displaySetsPlayer2,
                    displayTextMessage,
                    setsScore));
        } else {
            savedState.set(currentUndoIndex, new UndoRedo(pointsPlayer1,
                    pointsPlayer2,
                    gamesPlayer1,
                    gamesPlayer2,
                    setsPlayer1,
                    setsPlayer2,
                    numberOfSetsForWin,
                    numberOfServeInTieBreak,
                    serveOfPlayer,
                    serveOfPlayerInTieBreak,
                    tieBreak,
                    firstFault,
                    matchWon,
                    tiebreakFinal,
                    winnerPlayer1,
                    acePlayer1,
                    faultPlayer1,
                    doubleFaultPlayer1,
                    forcedErrorPlayer1,
                    unforcedErrorPlayer1,
                    winnerPlayer2,
                    acePlayer2,
                    faultPlayer2,
                    doubleFaultPlayer2,
                    forcedErrorPlayer2,
                    unforcedErrorPlayer2,
                    displayPointsPlayer1,
                    displayGamesPlayer1,
                    displaySetsPlayer1,
                    displayPointsPlayer2,
                    displayGamesPlayer2,
                    displaySetsPlayer2,
                    displayTextMessage,
                    setsScore));
        }
        /**
         * remove all data if after currentUndoIndex
         */
        for (int i = savedState.size() - 1; i > currentUndoIndex; i--) {
            savedState.remove(i);
        }


    }

    public void undo(View v) {

        if (currentUndoIndex - 1 < savedState.size() && currentUndoIndex - 1 >= 0) {
            currentUndoIndex--;
            pointsPlayer1 = savedState.get(currentUndoIndex).getPointsPlayer1();
            pointsPlayer2 = savedState.get(currentUndoIndex).getPointsPlayer2();
            gamesPlayer1 = savedState.get(currentUndoIndex).getGamesPlayer1();
            gamesPlayer2 = savedState.get(currentUndoIndex).getGamesPlayer2();
            setsPlayer1 = savedState.get(currentUndoIndex).getSetsPlayer1();
            setsPlayer2 = savedState.get(currentUndoIndex).getSetsPlayer2();
            numberOfSetsForWin = savedState.get(currentUndoIndex).getNumberOfSetsForWin();
            numberOfServeInTieBreak = savedState.get(currentUndoIndex).getNumberOfServeInTieBreak();
            serveOfPlayer = savedState.get(currentUndoIndex).getServeOfPlayer();
            serveOfPlayerInTieBreak = savedState.get(currentUndoIndex).getServeOfPlayerInTieBreak();
            tieBreak = savedState.get(currentUndoIndex).getTieBreak();
            firstFault = savedState.get(currentUndoIndex).getFirstFault();
            matchWon = savedState.get(currentUndoIndex).getMatchWon();
            tiebreakFinal = savedState.get(currentUndoIndex).getTiebreakFinal();
            winnerPlayer1 = savedState.get(currentUndoIndex).getWinnerPlayer1();
            acePlayer1 = savedState.get(currentUndoIndex).getAcePlayer1();
            faultPlayer1 = savedState.get(currentUndoIndex).getFaultPlayer1();
            doubleFaultPlayer1 = savedState.get(currentUndoIndex).getDoubleFaultPlayer1();
            forcedErrorPlayer1 = savedState.get(currentUndoIndex).getForcedErrorPlayer1();
            unforcedErrorPlayer1 = savedState.get(currentUndoIndex).getUnforcedErrorPlayer1();
            winnerPlayer2 = savedState.get(currentUndoIndex).getWinnerPlayer2();
            acePlayer2 = savedState.get(currentUndoIndex).getAcePlayer2();
            faultPlayer2 = savedState.get(currentUndoIndex).getFaultPlayer2();
            doubleFaultPlayer2 = savedState.get(currentUndoIndex).getDoubleFaultPlayer2();
            forcedErrorPlayer2 = savedState.get(currentUndoIndex).getForcedErrorPlayer2();
            unforcedErrorPlayer2 = savedState.get(currentUndoIndex).getUnforcedErrorPlayer2();
            pointsViewPlayer1.setText(savedState.get(currentUndoIndex).getDisplayPointsPlayer1());
            gamesViewPlayer1.setText(savedState.get(currentUndoIndex).getDisplayGamesPlayer1());
            setsViewPlayer1.setText(savedState.get(currentUndoIndex).getDisplaySetsPlayer1());
            pointsViewPlayer2.setText(savedState.get(currentUndoIndex).getDisplayPointsPlayer2());
            gamesViewPlayer2.setText(savedState.get(currentUndoIndex).getDisplayGamesPlayer2());
            setsViewPlayer2.setText(savedState.get(currentUndoIndex).getDisplaySetsPlayer2());
            textViewDeuce.setText(savedState.get(currentUndoIndex).getDisplayTextMessage());
            setsScore = savedState.get(currentUndoIndex).getSetsScore();
            serveChange(serveOfPlayer);
            displayRightFaultButtonText();
            checkIfPlayerHasWinMatch();
            showHideSetsView();

        } else {
            Toast.makeText(this, "Can't undo", Toast.LENGTH_SHORT).show();
        }

    }

    public void redo(View v) {

        if (currentUndoIndex + 1 < savedState.size() && currentUndoIndex + 1 >= 0) {
            currentUndoIndex++;
            pointsPlayer1 = savedState.get(currentUndoIndex).getPointsPlayer1();
            pointsPlayer2 = savedState.get(currentUndoIndex).getPointsPlayer2();
            gamesPlayer1 = savedState.get(currentUndoIndex).getGamesPlayer1();
            gamesPlayer2 = savedState.get(currentUndoIndex).getGamesPlayer2();
            setsPlayer1 = savedState.get(currentUndoIndex).getSetsPlayer1();
            setsPlayer2 = savedState.get(currentUndoIndex).getSetsPlayer2();
            numberOfSetsForWin = savedState.get(currentUndoIndex).getNumberOfSetsForWin();
            numberOfServeInTieBreak = savedState.get(currentUndoIndex).getNumberOfServeInTieBreak();
            serveOfPlayer = savedState.get(currentUndoIndex).getServeOfPlayer();
            serveOfPlayerInTieBreak = savedState.get(currentUndoIndex).getServeOfPlayerInTieBreak();
            tieBreak = savedState.get(currentUndoIndex).getTieBreak();
            firstFault = savedState.get(currentUndoIndex).getFirstFault();
            matchWon = savedState.get(currentUndoIndex).getMatchWon();
            tiebreakFinal = savedState.get(currentUndoIndex).getTiebreakFinal();
            winnerPlayer1 = savedState.get(currentUndoIndex).getWinnerPlayer1();
            acePlayer1 = savedState.get(currentUndoIndex).getAcePlayer1();
            faultPlayer1 = savedState.get(currentUndoIndex).getFaultPlayer1();
            doubleFaultPlayer1 = savedState.get(currentUndoIndex).getDoubleFaultPlayer1();
            forcedErrorPlayer1 = savedState.get(currentUndoIndex).getForcedErrorPlayer1();
            unforcedErrorPlayer1 = savedState.get(currentUndoIndex).getUnforcedErrorPlayer1();
            winnerPlayer2 = savedState.get(currentUndoIndex).getWinnerPlayer2();
            acePlayer2 = savedState.get(currentUndoIndex).getAcePlayer2();
            faultPlayer2 = savedState.get(currentUndoIndex).getFaultPlayer2();
            doubleFaultPlayer2 = savedState.get(currentUndoIndex).getDoubleFaultPlayer2();
            forcedErrorPlayer2 = savedState.get(currentUndoIndex).getForcedErrorPlayer2();
            unforcedErrorPlayer2 = savedState.get(currentUndoIndex).getUnforcedErrorPlayer2();
            pointsViewPlayer1.setText(savedState.get(currentUndoIndex).getDisplayPointsPlayer1());
            gamesViewPlayer1.setText(savedState.get(currentUndoIndex).getDisplayGamesPlayer1());
            setsViewPlayer1.setText(savedState.get(currentUndoIndex).getDisplaySetsPlayer1());
            pointsViewPlayer2.setText(savedState.get(currentUndoIndex).getDisplayPointsPlayer2());
            gamesViewPlayer2.setText(savedState.get(currentUndoIndex).getDisplayGamesPlayer2());
            setsViewPlayer2.setText(savedState.get(currentUndoIndex).getDisplaySetsPlayer2());
            textViewDeuce.setText(savedState.get(currentUndoIndex).getDisplayTextMessage());
            setsScore = savedState.get(currentUndoIndex).getSetsScore();
            serveChange(serveOfPlayer);
            displayRightFaultButtonText();
            checkIfPlayerHasWinMatch();
            showHideSetsView();

        } else {
            Toast.makeText(this, "Can't redo", Toast.LENGTH_SHORT).show();
        }
    }

}
