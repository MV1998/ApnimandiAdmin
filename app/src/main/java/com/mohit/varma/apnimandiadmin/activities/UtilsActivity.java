package com.mohit.varma.apnimandiadmin.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.utilities.IsInternetConnectivity;
import com.mohit.varma.apnimandiadmin.utilities.ShowSnackBar;

public class UtilsActivity extends AppCompatActivity {
    private MaterialButton UtilsActivityButton;
    private TextInputEditText UtilsActivityEditText;
    private TextView UtilsActivityDeliveryFeeTexView;
    private Toolbar UtilsActivityToolbar;
    private View UtilsActivityRootView;
    private MyDatabaseReference myDatabaseReference;
    private Context activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils);

        initViews();

        UtilsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsInternetConnectivity.isConnected(activity)) {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(UtilsActivityRootView.getWindowToken(), 0);
                    }
                    if (UtilsActivityEditText.getText() != null) {
                        String deliveryFee = UtilsActivityEditText.getText().toString();
                        if (deliveryFee != null && !deliveryFee.isEmpty()) {
                            if (myDatabaseReference.getReference() != null) {
                                myDatabaseReference.getReference().child("Admin").child("deliveryFee").setValue(Integer.parseInt(deliveryFee)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ShowSnackBar.snackBar(activity, UtilsActivityRootView, activity.getResources().getString(R.string.delivery_fee_added));
                                        UtilsActivityEditText.setText("");
                                    }
                                });
                            }
                        } else {
                            ShowSnackBar.snackBar(activity, UtilsActivityRootView, activity.getResources().getString(R.string.all_fields_are_mandetory));
                        }
                    }
                } else {
                    ShowSnackBar.snackBar(activity, UtilsActivityRootView, activity.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });


        myDatabaseReference.getReference().child("Admin").child("deliveryFee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UtilsActivityDeliveryFeeTexView.setText("\u20B9"+dataSnapshot.getValue()+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        UtilsActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void initViews() {
        UtilsActivityButton = (MaterialButton) findViewById(R.id.UtilsActivityButton);
        UtilsActivityEditText = (TextInputEditText) findViewById(R.id.UtilsActivityEditText);
        UtilsActivityRootView = (View) findViewById(R.id.UtilsActivityRootView);
        UtilsActivityDeliveryFeeTexView = (TextView) findViewById(R.id.UtilsActivityDeliveryFeeTexView);
        UtilsActivityToolbar = (Toolbar) findViewById(R.id.UtilsActivityToolbar);
        this.activity = this;
        myDatabaseReference = new MyDatabaseReference();
    }
}
