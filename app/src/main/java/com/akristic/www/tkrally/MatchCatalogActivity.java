package com.akristic.www.tkrally;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.akristic.www.tkrally.data.PlayerContract;
import com.akristic.www.tkrally.data.PlayerContract.MatchEntry;

public class MatchCatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    MatchCursorAdapter mCursorAdapter;
    private static final int MATCH_LOADER = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_catalog);

        ListView playerListView = (ListView) findViewById(R.id.match_list);

        View emptyView = findViewById(R.id.match_empty_view);
        playerListView.setEmptyView(emptyView);

        mCursorAdapter = new MatchCursorAdapter(this, null);
        playerListView.setAdapter(mCursorAdapter);
        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MatchCatalogActivity.this, PlayerEditorActivity.class);
                Uri currentMatchUri = ContentUris.withAppendedId(PlayerContract.MatchEntry.CONTENT_URI, id);
                intent.setData(currentMatchUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(MATCH_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                MatchEntry._ID,
                MatchEntry.COLUMN_PLAYER_1_ID,
                MatchEntry.COLUMN_PLAYER_2_ID,
                MatchEntry.COLUMN_MATCH_ARRAY_LIST,
                MatchEntry.COLUMN_MATCH_TIME,
                MatchEntry.COLUMN_MATCH_DATE,
                MatchEntry.COLUMN_MATCH_FINISH};

        return new CursorLoader(this, MatchEntry.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_players_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the player.
                deleteMatch();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the player.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the player in the database.
     */
    private void deleteMatch() {
        // Only perform the delete if this is an existing player.

        int deletedMatch = getContentResolver().delete(MatchEntry.CONTENT_URI, null, null);
        if (deletedMatch != 0) {
            Toast.makeText(this, R.string.editor_delete_all_players_successful, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.editor_delete_all_players_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
