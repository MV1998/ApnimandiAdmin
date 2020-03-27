package com.mohit.varma.apnimandiadmin.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mohit.varma.apnimandiadmin.interfaces.IMyDatabaseReference;

import static com.mohit.varma.apnimandiadmin.utilities.WebServices.FIREBASE_ROOT_URL;
import static com.mohit.varma.apnimandiadmin.utilities.WebServices.FIREBASE_STORAGE_ROOT_URL;

public class MyDatabaseReference implements IMyDatabaseReference {
    @Override
    public DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_ROOT_URL);
    }

    @Override
    public StorageReference getStorageReference() {
        return FirebaseStorage.getInstance().getReferenceFromUrl(FIREBASE_STORAGE_ROOT_URL);
    }
}
