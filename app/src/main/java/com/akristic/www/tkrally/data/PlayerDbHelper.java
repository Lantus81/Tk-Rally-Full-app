package com.akristic.www.tkrally.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.akristic.www.tkrally.data.PlayerContract.PlayerEntry;
import com.akristic.www.tkrally.data.PlayerContract.MatchEntry;

/**
 * Created by Toni on 17.4.2017..
 */

public class PlayerDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tkrally.db";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PlayerEntry.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_MATCH =
            "DROP TABLE IF EXISTS " + MatchEntry.TABLE_NAME;

    public PlayerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + PlayerEntry.TABLE_NAME + " (" +
                        PlayerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PlayerEntry.COLUMN_PLAYER_NAME + " TEXT NOT NULL, " +
                        PlayerEntry.COLUMN_PLAYER_NATIONALITY + " TEXT, " +
                        PlayerEntry.COLUMN_PLAYER_YEAR_BORN + " INTEGER NOT NULL DEFAULT 0, " +
                        PlayerEntry.COLUMN_PLAYER_GENDER + " INTEGER NOT NULL, " +
                        PlayerEntry.COLUMN_PLAYER_WEIGHT + " INTEGER NOT NULL DEFAULT 0, " +
                        PlayerEntry.COLUMN_PLAYER_HEIGHT + " INTEGER NOT NULL DEFAULT 0, " +
                        PlayerEntry.COLUMN_PLAYER_PICTURE + " BLOB);";
        String SQL_CREATE_ENTRIES_MATCH =
                "CREATE TABLE " + MatchEntry.TABLE_NAME + " (" +
                        MatchEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MatchEntry.COLUMN_PLAYER_1_ID + " INTEGER, " +
                        MatchEntry.COLUMN_PLAYER_2_ID + " INTEGER, " +
                        MatchEntry.COLUMN_MATCH_ARRAY_LIST + " TEXT, " +
                        MatchEntry.COLUMN_MATCH_TIME + " TEXT, " +
                        MatchEntry.COLUMN_MATCH_DATE + " TEXT, " +
                        MatchEntry.COLUMN_MATCH_FINISH + " INTEGER);";

        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES_MATCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES_MATCH);
        onCreate(db);
    }
}