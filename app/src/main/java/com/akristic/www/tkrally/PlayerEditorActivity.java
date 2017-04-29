package com.akristic.www.tkrally;

import android.Manifest;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.LoaderManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.akristic.www.tkrally.data.PlayerContract.PlayerEntry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static com.akristic.www.tkrally.PlayersNamesActivity.FILE_NAME;
import static com.akristic.www.tkrally.PlayersNamesActivity.bitmapPlayer1;

/**
 * Created by Toni on 19.4.2017..
 */

public class PlayerEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static Bitmap BITMAP_PLAYER1;
    public static Bitmap BITMAP_PLAYER2;
    public static String NAME_PLAYER1;
    public static String NAME_PLAYER2;

    private Uri mCurrentPlayerUri;
    private static final int EXISTING_PLAYER_LOADER = 0;
    private boolean mPlayerHasChanged = false;
    private static final String TAG = PlayerEditorActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public static final int IMAGE_SIZE = 300;
    private ExifInterface exif = null;
    /**
     * EditText field to enter the player's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the player's breed
     */
    private EditText mNationalityEditText;

    /**
     * EditText field to enter the player's weight
     */
    private EditText mWeightEditText;
    /**
     * EditText field to enter the player's height
     */
    private EditText mHeightEditText;
    /**
     * EditText field to enter the player's year of birth
     */
    private EditText mYearEditText;
    /**
     * ImageView field to enter the player's picture
     */
    private ImageView mPlayerImageView;
    /**
     * EditText field to enter the player's gender
     */
    private Spinner mGenderSpinner;
    /**
     * Gender of the player. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;

    private Bitmap bitmapPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_editor);

        Intent intent = getIntent();
        mCurrentPlayerUri = intent.getData();
        Button addPictureButton = (Button) findViewById(R.id.editor_add_picture_button);

        if (mCurrentPlayerUri == null) {
            setTitle(R.string.editor_activity_title_new_player);
        } else {
            setTitle(R.string.editor_activity_title_edit_player);
            invalidateOptionsMenu();
            getLoaderManager().initLoader(EXISTING_PLAYER_LOADER, null, this);
        }
        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_player_name);
        mNationalityEditText = (EditText) findViewById(R.id.edit_player_nationality);
        mWeightEditText = (EditText) findViewById(R.id.edit_player_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mHeightEditText = (EditText) findViewById(R.id.edit_player_height);
        mYearEditText = (EditText) findViewById(R.id.edit_player_birth);
        mPlayerImageView = (ImageView) findViewById(R.id.editor_player_image);

        setupSpinner();

        mNameEditText.setOnTouchListener(mTouchListener);
        mNationalityEditText.setOnTouchListener(mTouchListener);
        mWeightEditText.setOnTouchListener(mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);
        mHeightEditText.setOnTouchListener(mTouchListener);
        mYearEditText.setOnTouchListener(mTouchListener);

        /**
         * addPicture buttons
         */
        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog();
            }
        });
    }

    /**
     * dialog form to choose gallery or camera
     */
    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerEditorActivity.this);
        builder
                .setMessage(R.string.dialog_select_prompt)
                .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGalleryChooser();
                    }
                })
                .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startCamera();
                    }
                });
        builder.create().show();
    }

    /**
     * if user chose gallery run this code
     */
    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    /**
     * if user chose camera run this code
     */
    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraFile()));
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    /**
     * @return picture file taken from camera
     */
    public File getCameraFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    /**
     * This code is run after startActivityForResult method
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                exif = new ExifInterface(getRealPathFromURI(data.getData()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            showImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            try {
                exif = new ExifInterface(Uri.fromFile(getCameraFile()).getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            showImage(Uri.fromFile(getCameraFile()));
        }
    }

    /**
     * Not sure what exactly is this for but it do something if user did not give permission for camera or to read internal storage
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    /**
     * We want to show image and rotate it to be in portrait mode
     *
     * @param uri path of picture file
     */
    public void showImage(Uri uri) {
        if (uri != null) {
            try {
                Bitmap bitmap = scaleBitmapDown(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), IMAGE_SIZE);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                bitmap = rotateBitmap(bitmap, orientation);
                mPlayerImageView.setImageBitmap(bitmap);
                bitmapPlayer = bitmap;
            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    public String getRealPathFromURI(Uri uri) {
        if (Build.VERSION.SDK_INT >= 19) {
            String id = uri.getLastPathSegment().split(":")[1];
            final String[] imageColumns = {MediaStore.Images.Media.DATA};
            final String imageOrderBy = null;
            Uri tempUri = getUri();
            Cursor imageCursor = getContentResolver().query(tempUri, imageColumns,
                    MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);
            if (imageCursor.moveToFirst()) {
                return imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            } else {
                return null;
            }
        } else {
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else
                return null;
        }
    }

    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    /**
     * scale image because we don't need big image to be shown in 90*90dp box
     *
     * @param bitmap       is picture file
     * @param maxDimension set this in IMAGE_SIZE variable
     * @return Bitmap picture that is smaller in size
     */
    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPlayerHasChanged = true;
            return false;
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new player, hide the "Delete" menu item.
        if (mCurrentPlayerUri == null) {
            MenuItem menuItem1 = menu.findItem(R.id.action_delete);
            menuItem1.setVisible(false);
            MenuItem menuItem2 = menu.findItem(R.id.action_player1);
            menuItem2.setVisible(false);
            MenuItem menuItem3 = menu.findItem(R.id.action_player2);
            menuItem3.setVisible(false);
            MenuItem menuItem4 = menu.findItem(R.id.action_save_new);
            menuItem4.setVisible(false);
        }
        return true;
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the player.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = 1; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = 2; // Female
                    } else {
                        mGender = 0; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    private void savePlayer() {
        //Read from input fields
        //Use trim to eliminate leading or trailing white space
        int weight;
        int height;
        int year;
        String nameString = mNameEditText.getText().toString().trim();
        String nationalityString = mNationalityEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim();
        String heightString = mHeightEditText.getText().toString().trim();
        String yearString = mYearEditText.getText().toString().trim();


        if (TextUtils.isEmpty(weightString)) {
            weight = 0;
        } else {
            weight = Integer.parseInt(weightString);
        }
        if (TextUtils.isEmpty(heightString)) {
            height = 0;
        } else {
            height = Integer.parseInt(heightString);
        }
        if (TextUtils.isEmpty(yearString)) {
            year = 0;
        } else {
            year = Integer.parseInt(yearString);
        }
        // Convert bitmap to byte array so it can be saved in database

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PlayerEntry.COLUMN_PLAYER_NAME, nameString);
        values.put(PlayerEntry.COLUMN_PLAYER_NATIONALITY, nationalityString);
        values.put(PlayerEntry.COLUMN_PLAYER_YEAR_BORN, year);
        values.put(PlayerEntry.COLUMN_PLAYER_GENDER, mGender);
        values.put(PlayerEntry.COLUMN_PLAYER_WEIGHT, weight);
        values.put(PlayerEntry.COLUMN_PLAYER_HEIGHT, height);
        if (bitmapPlayer != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmapPlayer.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] image = bos.toByteArray();
            values.put(PlayerEntry.COLUMN_PLAYER_PICTURE, image);
        }


        // Insert the new row, returning the primary key value of the new row
        if (mCurrentPlayerUri == null) {
            Uri newUri = getContentResolver().insert(PlayerEntry.CONTENT_URI, values);
            showToastMassage(newUri);
        } else {
            int playerIdUpdated = getContentResolver().update(mCurrentPlayerUri, values, null, null);
            showToastMassage(playerIdUpdated);
        }


    }

    private boolean checkEditInput() {
        String nameString = mNameEditText.getText().toString().trim();
        return (TextUtils.isEmpty(nameString));
    }

    private void showToastMassage(Uri newUri) {
        if (newUri == null) {
            Toast.makeText(this, "Error saving Player", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Player Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void showToastMassage(int id) {
        if (id == 0) {
            Toast.makeText(this, "Error saving Player", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Player Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_player_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                if (!checkEditInput()) {
                    // Save player to database
                    savePlayer();
                    //Exit activity
                    finish();
                    return true;
                } else {
                    Toast.makeText(this, "Player name is required", Toast.LENGTH_SHORT).show();
                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_player1:
                setPlayer1();
                return true;
            case R.id.action_player2:
                setPlayer2();
                return true;
            case R.id.action_save_new:
                mCurrentPlayerUri=null;
                savePlayer();
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                if (!mPlayerHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                    // Create a click listener to handle the user confirming that
                    // changes should be discarded.
                    DialogInterface.OnClickListener discardButtonClickListener =
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // User clicked "Discard" button, navigate to parent activity.
                                    NavUtils.navigateUpFromSameTask(PlayerEditorActivity.this);
                                }
                            };

                    // Show a dialog that notifies the user they have unsaved changes
                    showUnsavedChangesDialog(discardButtonClickListener);
                    return true;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPlayer1() {
        // Only perform the setting if this is an existing player.
        if (mCurrentPlayerUri != null) {
            BITMAP_PLAYER1 = bitmapPlayer;
            String name = mNameEditText.getText().toString();
            NAME_PLAYER1 = name;
            Toast.makeText(this, R.string.editor_set_player, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, R.string.editor_set_player_error, Toast.LENGTH_SHORT).show();
        }

    }
    private void setPlayer2() {
        // Only perform the setting if this is an existing player.
        if (mCurrentPlayerUri != null) {
            BITMAP_PLAYER2 = bitmapPlayer;
            String name = mNameEditText.getText().toString();
            NAME_PLAYER2 = name;
            Toast.makeText(this, R.string.editor_set_player, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, R.string.editor_set_player_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
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
        if (mCurrentPlayerUri != null) {
            // Call the ContentResolver to delete the player at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPlayerUri
            // content URI already identifies the player that we want.
            int deletedPlayer = getContentResolver().delete(mCurrentPlayerUri, null, null);
            if (deletedPlayer != 0) {
                Toast.makeText(this, R.string.editor_delete_player_successful, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.editor_delete_player_failed, Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PlayerEntry._ID,
                PlayerEntry.COLUMN_PLAYER_NAME,
                PlayerEntry.COLUMN_PLAYER_NATIONALITY,
                PlayerEntry.COLUMN_PLAYER_YEAR_BORN,
                PlayerEntry.COLUMN_PLAYER_GENDER,
                PlayerEntry.COLUMN_PLAYER_WEIGHT,
                PlayerEntry.COLUMN_PLAYER_HEIGHT,
                PlayerEntry.COLUMN_PLAYER_PICTURE
        };

        return new CursorLoader(this, mCurrentPlayerUri,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            // Find the columns of player attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_NAME);
            int nationalityColumnIndex = cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_NATIONALITY);
            int genderColumnIndex = cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_WEIGHT);
            int heightColumnIndex = cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_HEIGHT);
            int yearColumnIndex = cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_YEAR_BORN);
            int imageColumnIndex = cursor.getColumnIndex(PlayerEntry.COLUMN_PLAYER_PICTURE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String nationality = cursor.getString(nationalityColumnIndex);
            int year = cursor.getInt(yearColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int weight = cursor.getInt(weightColumnIndex);
            int height = cursor.getInt(heightColumnIndex);
            byte[] imgByte = null;
            if (cursor.getBlob(imageColumnIndex) != null) {
                imgByte = cursor.getBlob(imageColumnIndex);
            }

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mNationalityEditText.setText(nationality);
            mWeightEditText.setText(Integer.toString(weight));
            mHeightEditText.setText(Integer.toString(height));
            mYearEditText.setText(Integer.toString(year));
            if (imgByte != null) {
                bitmapPlayer = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                mPlayerImageView.setImageBitmap(bitmapPlayer);
            } else {
                mPlayerImageView.setImageBitmap(null);
            }
            // Gender is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Unknown, 1 is Male, 2 is Female).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (gender) {
                case PlayerEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case PlayerEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mNationalityEditText.setText("");
        mYearEditText.setText("");
        mGenderSpinner.setSelection(PlayerEntry.GENDER_UNKNOWN);
        mWeightEditText.setText("");
        mHeightEditText.setText("");
        mPlayerImageView.setImageBitmap(null);

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
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

    @Override
    public void onBackPressed() {
        // If the player hasn't changed, continue with handling back button press
        if (!mPlayerHasChanged) {
            super.onBackPressed();
            return;
        } else {
            // Otherwise if there are unsaved changes, setup a dialog to warn the user.
            // Create a click listener to handle the user confirming that changes should be discarded.
            DialogInterface.OnClickListener discardButtonClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // User clicked "Discard" button, close the current activity.
                            finish();
                        }
                    };

            // Show dialog that there are unsaved changes
            showUnsavedChangesDialog(discardButtonClickListener);
        }
    }
}
