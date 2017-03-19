package com.akristic.www.tkrally;

/**
 * Created by Toni on 7.3.2017..
 */

public class UndoRedo {
    int pointsPlayer1 = 0;
    int pointsPlayer2 = 0;
    int gamesPlayer1 = 0;
    int gamesPlayer2 = 0;
    int setsPlayer1 = 0;
    int setsPlayer2 = 0;

    int numberOfSetsForWin = 2;
    int numberOfServeInTieBreak = 0;
    boolean serveOfPlayer = true;
    boolean serveOfPlayerInTieBreak = true;
    boolean tieBreak = false;
    boolean firstFault = true;
    boolean showStatistic = true;
    boolean matchWon = false;
    boolean tiebreakFinal = true;
    /**
     * statistic variables
     */
    int winnerPlayer1 = 0;
    int acePlayer1 = 0;
    int faultPlayer1 = 0;
    int doubleFaultPlayer1 = 0;
    int forcedErrorPlayer1 = 0;
    int unforcedErrorPlayer1 = 0;
    int winnerPlayer2 = 0;
    int acePlayer2 = 0;
    int faultPlayer2 = 0;
    int doubleFaultPlayer2 = 0;
    int forcedErrorPlayer2 = 0;
    int unforcedErrorPlayer2 = 0;
    public UndoRedo(int pointsPlayer1, int pointsPlayer2){

    }
}
