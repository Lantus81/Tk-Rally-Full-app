package com.akristic.www.tkrally.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.akristic.www.tkrally.data.PlayerContract.PlayerEntry;
/**
 * Created by Toni on 17.4.2017..
 */

public class PlayerProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = PlayerProvider.class.getSimpleName();

    private PlayerDbHelper mDbHelper;

    /**
     * URI matcher code for the content URI for the players table
     */
    private static final int PLAYERS = 100;

    /**
     * URI matcher code for the content URI for a single player in the players table
     */
    private static final int PLAYER_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(PlayerContract.CONTENT_AUTHORITY, PlayerContract.PATH_PLEYERS, PLAYERS);
        sUriMatcher.addURI(PlayerContract.CONTENT_AUTHORITY, PlayerContract.PATH_PLEYERS + "/#", PLAYER_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new PlayerDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PLAYERS:
                // For the PLAYERS code, query the players table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the players table.
                cursor = database.query(PlayerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PLAYER_ID:
                // For the PLAYER_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.players/players/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PlayerEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the players table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(PlayerEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLAYERS:
                return PlayerEntry.CONTENT_LIST_TYPE;
            case PLAYER_ID:
                return PlayerEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLAYERS:
                return insertPlayer(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a player into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPlayer(Uri uri, ContentValues values) {
        String name = values.getAsString(PlayerEntry.COLUMN_PLAYER_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Player requires a name");
        }
        Integer gender = values.getAsInteger(PlayerEntry.COLUMN_PLAYER_GENDER);
        if (gender == null) {
            throw new IllegalArgumentException("Player requires valid gender");
        }
        Integer weight = values.getAsInteger(PlayerEntry.COLUMN_PLAYER_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Player requires valid weight");
        }
        Integer height = values.getAsInteger(PlayerEntry.COLUMN_PLAYER_HEIGHT);
        if (height != null && height < 0) {
            throw new IllegalArgumentException("Player requires valid height");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(PlayerEntry.TABLE_NAME, null, values);
        // Once we know the ID of the new row in the table,
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // return the new URI with the ID appended to the end of it
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Track the number of rows that were deleted
        int rowsDeleted;
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLAYERS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(PlayerEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted !=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case PLAYER_ID:
                // Delete a single row given by the ID in the URI
                selection = PlayerEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(PlayerEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted !=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PLAYERS:
                return updatePlayer(uri, values, selection, selectionArgs);
            case PLAYER_ID:
                // For the PLAYER_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PlayerEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePlayer(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update players in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more players).
     * Return the number of rows that were successfully updated.
     */
    private int updatePlayer(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }
        if (values.containsKey(PlayerEntry.COLUMN_PLAYER_NAME)) {
            String name = values.getAsString(PlayerEntry.COLUMN_PLAYER_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Player requires a name");
            }
        }
        if (values.containsKey(PlayerEntry.COLUMN_PLAYER_GENDER)) {
            Integer gender = values.getAsInteger(PlayerEntry.COLUMN_PLAYER_GENDER);
            if (gender == null) {
                throw new IllegalArgumentException("Player requires valid gender");
            }
        }
        if (values.containsKey(PlayerEntry.COLUMN_PLAYER_WEIGHT)) {
            Integer weight = values.getAsInteger(PlayerEntry.COLUMN_PLAYER_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Player requires valid weight");
            }
        }
        if (values.containsKey(PlayerEntry.COLUMN_PLAYER_HEIGHT)) {
            Integer height = values.getAsInteger(PlayerEntry.COLUMN_PLAYER_HEIGHT);
            if (height != null && height < 0) {
                throw new IllegalArgumentException("Player requires valid weight");
            }
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(PlayerEntry.TABLE_NAME, values, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
