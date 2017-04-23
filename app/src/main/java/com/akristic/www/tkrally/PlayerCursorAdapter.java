package com.akristic.www.tkrally;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akristic.www.tkrally.data.PlayerContract;

import static android.R.attr.bitmap;

/**
 * Created by Toni on 17.4.2017..
 */

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
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
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
        TextView playerName = (TextView) view.findViewById(R.id.name);
        TextView playerNationality = (TextView) view.findViewById(R.id.summary);
        ImageView playerImage = (ImageView) view.findViewById(R.id.catalog_player_image);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(PlayerContract.PlayerEntry.COLUMN_PLAYER_NAME));
        String nationality = cursor.getString(cursor.getColumnIndexOrThrow(PlayerContract.PlayerEntry.COLUMN_PLAYER_NATIONALITY));
        byte[] byteImage = cursor.getBlob(cursor.getColumnIndexOrThrow(PlayerContract.PlayerEntry.COLUMN_PLAYER_PICTURE));
        if (byteImage == null) {
            playerImage.setImageBitmap(BitmapFactory.decodeResource(view.getResources(), R.drawable.player_silhouette));
        } else {
            Bitmap bitmapPlayer = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            playerImage.setImageBitmap(bitmapPlayer);
        }
        // Populate fields with extracted properties
        playerName.setText(name);
        if (TextUtils.isEmpty(nationality)) {
            nationality = context.getString(R.string.Unknown_nationality);
        }
        playerNationality.setText(nationality);
    }
}
