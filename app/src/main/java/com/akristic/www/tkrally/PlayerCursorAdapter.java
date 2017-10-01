package com.akristic.www.tkrally;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import static android.R.attr.id;


public class PlayerCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link PlayerCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public PlayerCursorAdapter(Context context, Cursor c) {
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
        return LayoutInflater.from(context).inflate(R.layout.list_player_item, parent, false);
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
        TextView playerName = (TextView) view.findViewById(R.id.name);
        TextView playerNationality = (TextView) view.findViewById(R.id.summary);
        ImageView playerImage = (ImageView) view.findViewById(R.id.catalog_player_image);
        ImageView menu = (ImageView) view.findViewById(R.id.player_catalog_item_menu_more);

        // Extract properties from cursor
        final String name = cursor.getString(cursor.getColumnIndexOrThrow(PlayerContract.PlayerEntry.COLUMN_PLAYER_NAME));
        String nationality = cursor.getString(cursor.getColumnIndexOrThrow(PlayerContract.PlayerEntry.COLUMN_PLAYER_NATIONALITY));
        final int playerID = cursor.getInt(cursor.getColumnIndexOrThrow(PlayerContract.PlayerEntry._ID));

        final byte[] byteImage = cursor.getBlob(cursor.getColumnIndexOrThrow(PlayerContract.PlayerEntry.COLUMN_PLAYER_PICTURE));


        // Populate fields with extracted properties
        playerName.setText(name);
        if (TextUtils.isEmpty(nationality)) {
            nationality = context.getString(R.string.Unknown_nationality);
        }
        playerNationality.setText(nationality);
        if (byteImage != null) {
            Bitmap bitmapPlayer = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            playerImage.setImageBitmap(bitmapPlayer);
        } else {
            playerImage.setImageBitmap(null);
        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                switch (view.getId()) {
                    case R.id.player_catalog_item_menu_more:

                        PopupMenu popup = new PopupMenu(view.getContext(), view);
                        popup.getMenuInflater().inflate(R.menu.menu_player_set,
                                popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.action_player1_set:
                                        if (byteImage != null) {
                                            PlayerEditorActivity.BITMAP_PLAYER1 = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
                                        } else {
                                            PlayerEditorActivity.BITMAP_PLAYER1 = null;
                                        }
                                        PlayerEditorActivity.ID_PLAYER1 = playerID;
                                        PlayerEditorActivity.NAME_PLAYER1 = name;
                                        Toast.makeText(view.getContext(), name + " " + view.getContext().getString(R.string.is_player_1), Toast.LENGTH_LONG).show();

                                        break;

                                    case R.id.action_player2_set:
                                        if (byteImage != null) {
                                            PlayerEditorActivity.BITMAP_PLAYER2 = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
                                        } else {
                                            PlayerEditorActivity.BITMAP_PLAYER2 = null;
                                        }
                                        PlayerEditorActivity.ID_PLAYER2 = playerID;
                                        PlayerEditorActivity.NAME_PLAYER2 = name;
                                        Toast.makeText(view.getContext(), name + " " + view.getContext().getString(R.string.is_player_2), Toast.LENGTH_LONG).show();

                                        break;
                                    case R.id.action_player_delete:
                                        Uri currentPlayerUri = ContentUris.withAppendedId(PlayerContract.PlayerEntry.CONTENT_URI, playerID);
                                        // Only perform the delete if this is an existing player.
                                        if (currentPlayerUri != null) {
                                            // Call the ContentResolver to delete the player at the given content URI.
                                            int deletedPlayer = context.getContentResolver().delete(currentPlayerUri, null, null);
                                            if (deletedPlayer != 0) {
                                                Toast.makeText(context, R.string.editor_delete_player_successful, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, R.string.editor_delete_player_failed, Toast.LENGTH_SHORT).show();
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
