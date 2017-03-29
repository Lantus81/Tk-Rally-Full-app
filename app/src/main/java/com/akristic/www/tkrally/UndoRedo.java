package com.akristic.www.tkrally;

/**
 * Created by Toni on 7.3.2017..
 */

public class UndoRedo {
    private int mPointsPlayer1 = 0;
    private int mPointsPlayer2 = 0;
    private int mGamesPlayer1 = 0;
    private int mGamesPlayer2 = 0;
    private int mSetsPlayer1 = 0;
    private int mSetsPlayer2 = 0;

    private int mNumberOfSetsForWin = 2;
    private int mNumberOfServeInTieBreak = 0;
    private boolean mServeOfPlayer = true;
    private boolean mServeOfPlayerInTieBreak = true;
    private boolean mTieBreak = false;
    private boolean mFirstFault = true;
    private boolean mMatchWon = false;
    private boolean mTiebreakFinal = true;
    /**
     * statistic variables
     */
    private int mWinnerPlayer1 = 0;
    private int mAcePlayer1 = 0;
    private int mFaultPlayer1 = 0;
    private int mDoubleFaultPlayer1 = 0;
    private int mForcedErrorPlayer1 = 0;
    private int mUnforcedErrorPlayer1 = 0;
    private int mWinnerPlayer2 = 0;
    private int mAcePlayer2 = 0;
    private int mFaultPlayer2 = 0;
    private int mDoubleFaultPlayer2 = 0;
    private int mForcedErrorPlayer2 = 0;
    private int mUnforcedErrorPlayer2 = 0;

    private String mDisplayPointsPlayer1;
    private String mDisplayGamesPlayer1;
    private String mDisplaySetsPlayer1;
    private String mDisplayPointsPlayer2;
    private String mDisplayGamesPlayer2;
    private String mDisplaySetsPlayer2;
    private String mDisplayTextMessage;

    public UndoRedo(int pointsPlayer1,
                    int pointsPlayer2,
                    int gamesPlayer1,
                    int gamesPlayer2,
                    int setsPlayer1,
                    int setsPlayer2,
                    int numberOfSetsForWin,
                    int numberOfServeInTieBreak,
                    boolean serveOfPlayer,
                    boolean serveOfPlayerInTieBreak,
                    boolean tieBreak,
                    boolean firstFault,
                    boolean matchWon,
                    boolean tiebreakFinal,
                    int winnerPlayer1,
                    int acePlayer1,
                    int faultPlayer1,
                    int doubleFaultPlayer1,
                    int forcedErrorPlayer1,
                    int unforcedErrorPlayer1,
                    int winnerPlayer2,
                    int acePlayer2,
                    int faultPlayer2,
                    int doubleFaultPlayer2,
                    int forcedErrorPlayer2,
                    int unforcedErrorPlayer2,
                    String displayPointsPlayer1,
                    String displayGamesPlayer1,
                    String displaySetsPlayer1,
                    String displayPointsPlayer2,
                    String displayGamesPlayer2,
                    String displaySetsPlayer2,
                    String displayTextMessage) {

        mPointsPlayer1 = pointsPlayer1;
        mPointsPlayer2 = pointsPlayer2;
        mGamesPlayer1 = gamesPlayer1;
        mGamesPlayer2 = gamesPlayer2;
        mSetsPlayer1 = setsPlayer1;
        mSetsPlayer2 = setsPlayer2;

        mNumberOfSetsForWin = numberOfSetsForWin;
        mNumberOfServeInTieBreak = numberOfServeInTieBreak;
        mServeOfPlayer = serveOfPlayer;
        mServeOfPlayerInTieBreak = serveOfPlayerInTieBreak;
        mTieBreak = tieBreak;
        mFirstFault = firstFault;
        mMatchWon = matchWon;
        mTiebreakFinal = tiebreakFinal;
        /**
         * statistic variables
         */
        mWinnerPlayer1 = winnerPlayer1;
        mAcePlayer1 = acePlayer1;
        mFaultPlayer1 = faultPlayer1;
        mDoubleFaultPlayer1 = doubleFaultPlayer1;
        mForcedErrorPlayer1 = forcedErrorPlayer1;
        mUnforcedErrorPlayer1 = unforcedErrorPlayer1;
        mWinnerPlayer2 = winnerPlayer2;
        mAcePlayer2 = acePlayer2;
        mFaultPlayer2 = faultPlayer2;
        mDoubleFaultPlayer2 = doubleFaultPlayer2;
        mForcedErrorPlayer2 = forcedErrorPlayer2;
        mUnforcedErrorPlayer2 = unforcedErrorPlayer2;
        /**
         * text display
         */
        mDisplayPointsPlayer1=displayPointsPlayer1;
        mDisplayGamesPlayer1=displayGamesPlayer1;
        mDisplaySetsPlayer1=displaySetsPlayer1;
        mDisplayPointsPlayer2=displayPointsPlayer2;
        mDisplayGamesPlayer2=displayGamesPlayer2;
        mDisplaySetsPlayer2=displaySetsPlayer2;
        mDisplayTextMessage=displayTextMessage;
    }

    public int getPointsPlayer1() {
        return mPointsPlayer1;
    }

    public int getPointsPlayer2() {
        return mPointsPlayer2;
    }

    public int getGamesPlayer1() {
        return mGamesPlayer1;
    }

    public int getGamesPlayer2() {
        return mGamesPlayer2;
    }

    public int getSetsPlayer1() {
        return mSetsPlayer1;
    }

    public int getSetsPlayer2() {
        return mSetsPlayer2;
    }

    public int getNumberOfSetsForWin() {
        return mNumberOfSetsForWin;
    }

    public int getNumberOfServeInTieBreak() {
        return mNumberOfServeInTieBreak;
    }

    public boolean getServeOfPlayer() {
        return mServeOfPlayer;
    }

    public boolean getServeOfPlayerInTieBreak() {
        return mServeOfPlayerInTieBreak;
    }

    public boolean getTieBreak() {
        return mTieBreak;
    }

    public boolean getFirstFault() {
        return mFirstFault;
    }

    public boolean getMatchWon() {
        return mMatchWon;
    }

    public boolean getTiebreakFinal() {
        return mTiebreakFinal;
    }

    public int getWinnerPlayer1() {
        return mWinnerPlayer1;
    }

    public int getAcePlayer1() {
        return mAcePlayer1;
    }

    public int getFaultPlayer1() {
        return mFaultPlayer1;
    }

    public int getDoubleFaultPlayer1() {
        return mDoubleFaultPlayer1;
    }

    public int getForcedErrorPlayer1() {
        return mForcedErrorPlayer1;
    }

    public int getUnforcedErrorPlayer1() {
        return mUnforcedErrorPlayer1;
    }

    public int getWinnerPlayer2() {
        return mWinnerPlayer2;
    }

    public int getAcePlayer2() {
        return mAcePlayer2;
    }

    public int getFaultPlayer2() {
        return mFaultPlayer2;
    }

    public int getDoubleFaultPlayer2() {
        return mDoubleFaultPlayer2;
    }

    public int getForcedErrorPlayer2() {
        return mForcedErrorPlayer2;
    }

    public int getUnforcedErrorPlayer2() {
        return mUnforcedErrorPlayer2;
    }
    public String getDisplayPointsPlayer1(){
        return mDisplayPointsPlayer1;
    }
    public String getDisplayPointsPlayer2(){
        return mDisplayPointsPlayer2;
    }
    public String getDisplayGamesPlayer1(){
        return mDisplayGamesPlayer1;
    }
    public String getDisplayGamesPlayer2(){
        return mDisplayGamesPlayer2;
    }
    public String getDisplaySetsPlayer1(){
        return mDisplaySetsPlayer1;
    }
    public String getDisplaySetsPlayer2(){
        return mDisplaySetsPlayer2;
    }
    public String getDisplayTextMessage(){
        return mDisplayTextMessage;
    }
}
