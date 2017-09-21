package com.akristic.www.tkrally;

import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akristic.www.tkrally.data.PlayerContract;

import java.sql.Blob;

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
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView matchPlayer1Id = (TextView) view.findViewById(R.id.match_player_1);
        TextView matchPlayer2Id = (TextView) view.findViewById(R.id.match_player_2);
        TextView matchDate = (TextView) view.findViewById(R.id.match_date);
        ImageView imagePlayer1 = (ImageView) view.findViewById(R.id.catalog_player1_image);
        ImageView imagePlayer2 = (ImageView) view.findViewById(R.id.catalog_player2_image);

        // Extract properties from cursor
        String player1 = cursor.getString(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry.COLUMN_PLAYER_1_NAME));
        String player2 = cursor.getString(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry.COLUMN_PLAYER_2_NAME));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry.COLUMN_MATCH_DATE));
        byte[] byteImagePlayer1 = cursor.getBlob(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry.COLUMN_PLAYER_1_PICTURE));
        byte[] byteImagePlayer2 = cursor.getBlob(cursor.getColumnIndexOrThrow(PlayerContract.MatchEntry.COLUMN_PLAYER_2_PICTURE));
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

    }
}
