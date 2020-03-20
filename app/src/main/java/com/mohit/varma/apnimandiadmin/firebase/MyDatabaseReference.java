package com.mohit.varma.apnimandiadmin.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohit.varma.apnimandiadmin.interfaces.IMyDatabaseReference;

public class MyDatabaseReference implements IMyDatabaseReference {
    @Override
    public DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReferenceFromUrl("https://apnimandi-c7058.firebaseio.com/");
    }
}
