package com.mohit.varma.apnimandiadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.mohit.varma.apnimandiadmin.R;

import static com.mohit.varma.apnimandiadmin.utilities.Constant.ITEM_KEY;

public class VegetablesActivity extends AppCompatActivity {
    public static final String TAG = VegetablesActivity.class.getSimpleName();
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vegetabls);


        if( getIntent().getStringExtra(ITEM_KEY) != null) {
            if(!getIntent().getStringExtra(ITEM_KEY).isEmpty()){
                category = getIntent().getStringExtra(ITEM_KEY);
                Log.d(TAG, "onCreate: " + category);
            }
        }

    }
}
