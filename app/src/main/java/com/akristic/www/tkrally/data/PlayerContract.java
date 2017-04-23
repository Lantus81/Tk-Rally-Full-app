package com.akristic.www.tkrally.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Toni on 17.4.2017..
 */

public class PlayerContract {

    private PlayerContract(){}

    public static final String CONTENT_AUTHORITY = "com.akristic.www.tkrally";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PLEYERS = "players";

    public static final class PlayerEntry implements BaseColumns {
        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLEYERS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLEYERS;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLEYERS;

        /** Name of database table for pets */
        public final static String TABLE_NAME = "players";

        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the player.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PLAYER_NAME ="name";

        /**
         * Age of the player.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PLAYER_YEAR_BORN = "year";

        /**
         * Nationality of the player.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PLAYER_NATIONALITY= "nationality";

        /**
         * Gender of the player.
         *
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PLAYER_GENDER = "gender";

        /**
         * Weight of the player.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PLAYER_WEIGHT = "weight";

        /**
         * Height of the player.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PLAYER_HEIGHT = "height";

        /**
         * Height of the player.
         *
         * Type: BLOB
         */
        public final static String COLUMN_PLAYER_PICTURE = "picture";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

    }
}
