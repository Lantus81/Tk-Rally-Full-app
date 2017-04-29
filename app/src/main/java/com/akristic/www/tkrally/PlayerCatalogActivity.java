package com.akristic.www.tkrally;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.akristic.www.tkrally.data.PlayerContract.PlayerEntry;

import java.io.ByteArrayOutputStream;

import static android.R.attr.id;


public class PlayerCatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    PlayerCursorAdapter mCursorAdapter;
    private static final int PLAYER_LOADER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_catalog);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayerCatalogActivity.this, PlayerEditorActivity.class);
                startActivity(intent);
            }
        });
        ListView playerListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        playerListView.setEmptyView(emptyView);

        mCursorAdapter = new PlayerCursorAdapter(this, null);
        playerListView.setAdapter(mCursorAdapter);
        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlayerCatalogActivity.this, PlayerEditorActivity.class);
                Uri currentPlayerUri = ContentUris.withAppendedId(PlayerEntry.CONTENT_URI, id);
                intent.setData(currentPlayerUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PLAYER_LOADER, null, this);
    }

    private void insertPlayer() {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PlayerEntry.COLUMN_PLAYER_NAME, "Player");
        values.put(PlayerEntry.COLUMN_PLAYER_NATIONALITY, "Croatian");
        values.put(PlayerEntry.COLUMN_PLAYER_YEAR_BORN, 1981);
        values.put(PlayerEntry.COLUMN_PLAYER_GENDER, PlayerEntry.GENDER_MALE);
        values.put(PlayerEntry.COLUMN_PLAYER_WEIGHT, 79);
        values.put(PlayerEntry.COLUMN_PLAYER_HEIGHT, 181);


        // Insert the new row, returning the primary key value of the new row
        Uri newUri = getContentResolver().insert(PlayerEntry.CONTENT_URI, values);
        if (newUri != null) {
            Toast.makeText(this, "Default player created", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error inserting default player", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_player_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPlayer();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                PlayerEntry._ID,
                PlayerEntry.COLUMN_PLAYER_NAME,
                PlayerEntry.COLUMN_PLAYER_NATIONALITY,
                PlayerEntry.COLUMN_PLAYER_PICTURE};

        return new CursorLoader(this, PlayerEntry.CONTENT_URI,
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
                deletePlayer();
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
    private void deletePlayer() {
        // Only perform the delete if this is an existing player.

        int deletedPlayer = getContentResolver().delete(PlayerEntry.CONTENT_URI, null, null);
        if (deletedPlayer != 0) {
            Toast.makeText(this, R.string.editor_delete_all_players_successful, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.editor_delete_all_players_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
