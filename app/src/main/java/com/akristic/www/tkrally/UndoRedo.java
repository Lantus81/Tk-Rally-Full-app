package com.akristic.www.tkrally;

import android.os.Parcel;
import android.os.Parcelable;

import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.acePlayer1;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.acePlayer2;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.doubleFaultPlayer1;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.doubleFaultPlayer2;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.faultPlayer1;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.faultPlayer2;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.forcedErrorPlayer1;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.forcedErrorPlayer2;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.unforcedErrorPlayer1;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.unforcedErrorPlayer2;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.winnerPlayer1;
import static com.akristic.www.tkrally.NavigationScoreKeeperActivity.winnerPlayer2;

/**
 * Created by Toni on 7.3.2017..
 */

public class UndoRedo implements Parcelable {
    private int mPointsPlayer1 = 0;
    private int mPointsPlayer2 = 0;
    private int mGamesPlayer1 = 0;
    private int mGamesPlayer2 = 0;
    private int mSetsPlayer1 = 0;
    private int mSetsPlayer2 = 0;

    private int mSetsScore []={0};

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
                    String displayTextMessage,
                    int setsScore []) {

        mPointsPlayer1 = pointsPlayer1;
        mPointsPlayer2 = pointsPlayer2;
        mGamesPlayer1 = gamesPlayer1;
        mGamesPlayer2 = gamesPlayer2;
        mSetsPlayer1 = setsPlayer1;
        mSetsPlayer2 = setsPlayer2;

        mSetsScore = setsScore;

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
        mDisplayPointsPlayer1 = displayPointsPlayer1;
        mDisplayGamesPlayer1 = displayGamesPlayer1;
        mDisplaySetsPlayer1 = displaySetsPlayer1;
        mDisplayPointsPlayer2 = displayPointsPlayer2;
        mDisplayGamesPlayer2 = displayGamesPlayer2;
        mDisplaySetsPlayer2 = displaySetsPlayer2;
        mDisplayTextMessage = displayTextMessage;
    }

    protected UndoRedo(Parcel in) {
        mPointsPlayer1 = in.readInt();
        mPointsPlayer2 = in.readInt();
        mGamesPlayer1 = in.readInt();
        mGamesPlayer2 = in.readInt();
        mSetsPlayer1 = in.readInt();
        mSetsPlayer2 = in.readInt();

        mSetsScore = in.createIntArray();

        mNumberOfSetsForWin = in.readInt();
        mNumberOfServeInTieBreak = in.readInt();

        mServeOfPlayer = in.readByte() != 0;
        mServeOfPlayerInTieBreak = in.readByte() != 0;
        mTieBreak = in.readByte() != 0;
        mFirstFault = in.readByte() != 0;
        mMatchWon = in.readByte() != 0;
        mTiebreakFinal = in.readByte() != 0;
        /**
         * statistic variables
         */
        mWinnerPlayer1 = in.readInt();
        mAcePlayer1 = in.readInt();
        mFaultPlayer1 = in.readInt();
        mDoubleFaultPlayer1 = in.readInt();
        mForcedErrorPlayer1 = in.readInt();
        mUnforcedErrorPlayer1 = in.readInt();
        mWinnerPlayer2 = in.readInt();
        mAcePlayer2 = in.readInt();
        mFaultPlayer2 = in.readInt();
        mDoubleFaultPlayer2 = in.readInt();
        mForcedErrorPlayer2 = in.readInt();
        mUnforcedErrorPlayer2 = in.readInt();
        /**
         * text display
         */
        mDisplayPointsPlayer1 = in.readString();
        mDisplayGamesPlayer1 = in.readString();
        mDisplaySetsPlayer1 = in.readString();
        mDisplayPointsPlayer2 = in.readString();
        mDisplayGamesPlayer2 = in.readString();
        mDisplaySetsPlayer2 = in.readString();
        mDisplayTextMessage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeInt(mPointsPlayer1);
        dest.writeInt(mPointsPlayer2);
        dest.writeInt(mGamesPlayer1);
        dest.writeInt(mGamesPlayer2);
        dest.writeInt(mSetsPlayer1);
        dest.writeInt(mSetsPlayer2);
        dest.writeInt(mNumberOfSetsForWin);
        dest.writeInt(mNumberOfServeInTieBreak);

        dest.writeIntArray(mSetsScore);

        dest.writeByte((byte) (mServeOfPlayer ? 1 : 0));
        dest.writeByte((byte) (mServeOfPlayerInTieBreak ? 1 : 0));
        dest.writeByte((byte) (mTieBreak ? 1 : 0));
        dest.writeByte((byte) (mFirstFault ? 1 : 0));
        dest.writeByte((byte) (mMatchWon ? 1 : 0));
        dest.writeByte((byte) (mTiebreakFinal ? 1 : 0));


        dest.writeInt(mWinnerPlayer1);
        dest.writeInt(mAcePlayer1);
        dest.writeInt(mFaultPlayer1);
        dest.writeInt(mDoubleFaultPlayer1);
        dest.writeInt(mForcedErrorPlayer1);
        dest.writeInt(mUnforcedErrorPlayer1);
        dest.writeInt(mWinnerPlayer2);
        dest.writeInt(mAcePlayer2);
        dest.writeInt(mFaultPlayer2);
        dest.writeInt(mDoubleFaultPlayer2);
        dest.writeInt(mForcedErrorPlayer2);
        dest.writeInt(mUnforcedErrorPlayer2);
        dest.writeString(mDisplayPointsPlayer1);
        dest.writeString(mDisplayGamesPlayer1);
        dest.writeString(mDisplaySetsPlayer1);
        dest.writeString(mDisplayGamesPlayer2);
        dest.writeString(mDisplaySetsPlayer2);
        dest.writeString(mDisplayTextMessage);

    }

    @SuppressWarnings("unused")
    public static final Creator<UndoRedo> CREATOR = new Creator<UndoRedo>() {
        @Override
        public UndoRedo createFromParcel(Parcel in) {
            return new UndoRedo(in);
        }

        @Override
        public UndoRedo[] newArray(int size) {
            return new UndoRedo[size];
        }
    };
    public int[] getSetsScore() {
        return mSetsScore;
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

    public String getDisplayPointsPlayer1() {
        return mDisplayPointsPlayer1;
    }

    public String getDisplayPointsPlayer2() {
        return mDisplayPointsPlayer2;
    }

    public String getDisplayGamesPlayer1() {
        return mDisplayGamesPlayer1;
    }

    public String getDisplayGamesPlayer2() {
        return mDisplayGamesPlayer2;
    }

    public String getDisplaySetsPlayer1() {
        return mDisplaySetsPlayer1;
    }

    public String getDisplaySetsPlayer2() {
        return mDisplaySetsPlayer2;
    }

    public String getDisplayTextMessage() {
        return mDisplayTextMessage;
    }
}
