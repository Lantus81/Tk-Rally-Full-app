package com.akristic.www.tkrally;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akristic.www.tkrally.data.PlayerContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Blob;
import java.util.List;

/**
 * Created by Toni on 24.4.2017..
 */

public class MatchCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link MatchCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public MatchCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_match_item, parent, false);
    }

    /**
     * This method binds the player data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current player can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView matchPlayer1Id = (TextView) view.findViewById(R.id.match_player_1);
        TextView matchPlayer2Id = (TextView) view.findViewById(R.id.match_player_2);
        TextView matchDate = (TextView) view.findViewById(R.id.match_date);
        ImageView imagePlayer1 = (ImageView) view.findViewById(R.id.catalog_player1_image);
        ImageView imagePlayer2 = (ImageView) view.findViewById(R.id.catalog_player2_image);
        ImageView menu = (ImageView) view.findViewById(R.id.match_catalog_item_menu_more);

        // Extract properties from cursor
        final int matchID = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry._ID));
        final String player1 = cursor.getString(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry.COLUMN_PLAYER_1_NAME));
        final String player2 = cursor.getString(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry.COLUMN_PLAYER_2_NAME));
        final String array = cursor.getString(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry.COLUMN_MATCH_ARRAY_LIST));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry.COLUMN_MATCH_DATE));
        final byte[] byteImagePlayer1 = cursor.getBlob(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry.COLUMN_PLAYER_1_PICTURE));
        final byte[] byteImagePlayer2 = cursor.getBlob(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry.COLUMN_PLAYER_2_PICTURE));
        if (byteImagePlayer1 != null) {
            Bitmap bitmapPlayer1 = BitmapFactory.decodeByteArray(byteImagePlayer1, 0, byteImagePlayer1.length);
            imagePlayer1.setImageBitmap(bitmapPlayer1);
        } else {
            imagePlayer1.setImageBitmap(null);
        }
        if (byteImagePlayer2 != null) {
            Bitmap bitmapPlayer2 = BitmapFactory.decodeByteArray(byteImagePlayer2, 0, byteImagePlayer2.length);
            imagePlayer2.setImageBitmap(bitmapPlayer2);
        } else {
            imagePlayer2.setImageBitmap(null);
        }

        // Populate fields with extracted properties
        matchPlayer1Id.setText(player1);
        matchPlayer2Id.setText(player2);
        matchDate.setText(date);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                switch (view.getId()) {
                    case R.id.match_catalog_item_menu_more:

                        PopupMenu popup = new PopupMenu(view.getContext(), view);
                        popup.getMenuInflater().inflate(R.menu.menu_matches_load,
                                popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.action_match_load:
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<List<UndoRedo>>() {
                                        }.getType();
                                        NavigationScoreKeeperActivity.savedState = gson.fromJson(array, type);
                                        NavigationScoreKeeperActivity.CURRENT_LOAD_MATCH_STATE = true;
                                        PlayerEditorActivity.NAME_PLAYER1 = player1;
                                        PlayerEditorActivity.NAME_PLAYER2 = player2;
                                        if (byteImagePlayer1!=null){
                                             PlayerEditorActivity.BITMAP_PLAYER1 = BitmapFactory.decodeByteArray(byteImagePlayer1, 0, byteImagePlayer1.length);
                                        }else{
                                            PlayerEditorActivity.BITMAP_PLAYER1 = null;
                                        }
                                        if (byteImagePlayer2 !=null){
                                            PlayerEditorActivity.BITMAP_PLAYER2 = BitmapFactory.decodeByteArray(byteImagePlayer2, 0, byteImagePlayer2.length);
                                        }else {
                                            PlayerEditorActivity.BITMAP_PLAYER2 = null;
                                        }

                                        NavigationScoreKeeperActivity.mCurrentMatchUri = ContentUris.withAppendedId(PlayerContract.MatchEntry.CONTENT_URI, matchID);
                                        ((Activity)context).finish();
                                        break;

                                    case R.id.action_match_delete:
                                        Uri currentMatchUri = ContentUris.withAppendedId(PlayerContract.MatchEntry.CONTENT_URI, matchID);
                                        // Only perform the delete if this is an existing match.
                                        if (currentMatchUri != null) {
                                            // Call the ContentResolver to delete the match at the given content URI.
                                            int deletedMatch = context.getContentResolver().delete(currentMatchUri, null, null);
                                            if (deletedMatch != 0) {
                                                Toast.makeText(context, R.string.editor_delete_match_successful, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, R.string.editor_delete_match_failed, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }

                                return true;
                            }
                        });

                        break;

                    default:
                        break;
                }
            }
        });

    }
}
