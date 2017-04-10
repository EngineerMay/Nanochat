package com.example.mayankchauhan.nanochat;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by mayankchauhan on 27/02/17.
 */

public class FireApp extends Application {

    public static DatabaseReference databaseReference;
    public static StorageReference storageReference;
    @Override
    public void onCreate() {
        super.onCreate();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        storageReference = FirebaseStorage.getInstance().getReference();

    }
}
