package com.akristic.www.tkrally;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.R.attr.data;
import static android.R.attr.orientation;
import static android.R.attr.rotation;


public class PlayersNamesActivity extends AppCompatActivity {
    private EditText player1Name;
    private EditText player2Name;
    public static final String FILE_NAME = "temp.jpg";

    private static final String TAG = PlayersNamesActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public static final int IMAGE_SIZE = 400;

    private ImageView mImagePlayer1;
    private ImageView mImagePlayer2;
    private String whatPlayer = "";
    public static Bitmap bitmapPlayer1;
    public static Bitmap bitmapPlayer2;

    private ExifInterface exif = null;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_names);
        /**
         * initialize variables
         */
        player1Name = (EditText) findViewById(R.id.player1_change_name);
        player2Name = (EditText) findViewById(R.id.player2_change_name);
        player1Name.setText(NavigationScoreKeeperActivity.namePlayer1);
        player2Name.setText(NavigationScoreKeeperActivity.namePlayer2);
        Button addPicturePlayer1 = (Button) findViewById(R.id.add_picture_player1);
        Button addPicturePlayer2 = (Button) findViewById(R.id.add_picture_player2);
        mImagePlayer1 = (ImageView) findViewById(R.id.image_player1);
        mImagePlayer2 = (ImageView) findViewById(R.id.image_player2);

        /**
         * if we already added pictures then show them
         */
        if (bitmapPlayer1 != null) {
            mImagePlayer1.setImageBitmap(bitmapPlayer1);

        }
        if (bitmapPlayer2 != null) {
            mImagePlayer2.setImageBitmap(bitmapPlayer2);

        }
        /**
         * addPicture buttons
         */
        addPicturePlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatPlayer = "Player1"; //** set this so that method showImage knows what button is clicked
                alertDialog();
            }
        });
        addPicturePlayer2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                whatPlayer = "Player2";
                alertDialog();
            }
        });

    }

    /**
     * saving static variables namePlayer1 and namePlayer2 when we press back arrow
     */
    @Override
    protected void onPause() {
        super.onPause();
        NavigationScoreKeeperActivity.namePlayer1 = player1Name.getText().toString();
        NavigationScoreKeeperActivity.namePlayer2 = player2Name.getText().toString();
    }

    /**
     * dialog form to choose gallery or camera
     */
    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayersNamesActivity.this);
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



                if (whatPlayer.equals("Player1")) {
                    mImagePlayer1.setImageBitmap(bitmap);

                    bitmapPlayer1 = bitmap; //*save picture in static variable so other activity can use this
                }
                if (whatPlayer.equals("Player2")) {
                    mImagePlayer2.setImageBitmap(bitmap);

                    bitmapPlayer2 = bitmap;
                }

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
        if(Build.VERSION.SDK_INT >= 19){
            String id = uri.getLastPathSegment().split(":")[1];
            final String[] imageColumns = {MediaStore.Images.Media.DATA };
            final String imageOrderBy = null;
            Uri tempUri = getUri();
            Cursor imageCursor = getContentResolver().query(tempUri, imageColumns,
                    MediaStore.Images.Media._ID + "="+id, null, imageOrderBy);
            if (imageCursor.moveToFirst()) {
                return imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }else{
                return null;
            }
        }else{
            String[] projection = { MediaStore.MediaColumns.DATA };
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
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
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
}
