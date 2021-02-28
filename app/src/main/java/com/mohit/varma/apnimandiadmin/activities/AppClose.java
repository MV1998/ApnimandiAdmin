package com.mohit.varma.apnimandiadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;

public class AppClose extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_close);
        this.databaseReference = new MyDatabaseReference().getReference();

        setDeliveryChargeToSharedPreference();
    }


    public void setDeliveryChargeToSharedPreference(){
        databaseReference.child("Admin").child("deliveryFee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue() != null) {
                        long deliveryFee = (long) dataSnapshot.getValue();
                        if(deliveryFee == 50){
                            Intent intent = new Intent(AppClose.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(AppClose.this, "Access Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}