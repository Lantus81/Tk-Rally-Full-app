package com.akristic.www.tkrally;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

public class PlayersNamesActivity extends AppCompatActivity {
    private EditText player1Name;
    private EditText player2Name;
    public static final String FILE_NAME = "temp.jpg";

    private static final String TAG = PlayersNamesActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public static final int IMAGE_SIZE = 1000;

    private ImageView mImagePlayer1;
    private ImageView mImagePlayer2;
    private String whatPlayer = "";
    public static Bitmap bitmapPlayer1;
    public static Bitmap bitmapPlayer2;

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
            mImagePlayer1.setRotation(270); //** rotate picture because by default it is in landscape position
        }
        if (bitmapPlayer2 != null) {
            mImagePlayer2.setImageBitmap(bitmapPlayer2);
            mImagePlayer2.setRotation(270);
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

    /**This code is run after startActivityForResult method
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            showImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            showImage(Uri.fromFile(getCameraFile()));
        }
    }

    /**
     * Not sure what exactly is this for but it do something if user did not give permission for camera or to read internal storage
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
     * @param uri path of picture file
     */
    public void showImage(Uri uri) {
        if (uri != null) {
            try {

                Bitmap bitmap = scaleBitmapDown(MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                        IMAGE_SIZE);

                if (whatPlayer.equals("Player1")) {
                    mImagePlayer1.setImageBitmap(bitmap);
                    mImagePlayer1.setRotation(270);
                    bitmapPlayer1 = bitmap; //*save picture in static variable so other activity can use this
                }
                if (whatPlayer.equals("Player2")) {
                    mImagePlayer2.setImageBitmap(bitmap);
                    mImagePlayer2.setRotation(270);
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

    /**
     * scale image because we don't need big image to be shown in 90*90dp box
     *
     * @param bitmap is picture file
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


}
