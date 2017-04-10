package com.example.mayankchauhan.nanochat;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageActivity extends AppCompatActivity {

    private StorageReference mStorage;
    private Button selectImage;
    private static final int GALLERY_INTENT =2;

    public static final String ALLOW_KEY = "ALLOWED";
    public static final String GALLERY_PREF = "gallery_pref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        selectImage = (Button) findViewById(R.id.select_image);
        mStorage = FireApp.storageReference;

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(ImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (getFromPref(ImageActivity.this, ALLOW_KEY)) {
                        showSettingsAlert();
                    } else if (ContextCompat.checkSelfPermission(ImageActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)

                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ImageActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(ImageActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    GALLERY_INTENT);
                        }
                    }
                } else {
                    openGallery();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();

            StorageReference childref = mStorage.child("Photos").child(uri.getLastPathSegment());

            childref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(ImageActivity.this, "UPLOAD DONE", Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    public static void saveToPreferences(ImageActivity context, String key, Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences(GALLERY_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.commit();
    }
    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(GALLERY_PREF,
                Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }
    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(ImageActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Gallery.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(ImageActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                GALLERY_INTENT);
                    }
                });
        alertDialog.show();
    }
    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(ImageActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Gallery.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(ImageActivity.this);
                    }
                });

        alertDialog.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GALLERY_INTENT: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];

                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean
                                showRationale =
                                ActivityCompat.shouldShowRequestPermissionRationale(
                                        this, permission);

                        if (showRationale) {
                            showAlert();
                        } else if (!showRationale) {
                            // user denied flagging NEVER ASK AGAIN
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            saveToPreferences(ImageActivity.this, ALLOW_KEY, true);
                        }
                    }
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }

        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);

    }

}
