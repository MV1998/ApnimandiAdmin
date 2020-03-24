package com.mohit.varma.apnimandiadmin.interfaces;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public interface IMyDatabaseReference {
    DatabaseReference getReference();
    StorageReference  getStorageReference();
}
