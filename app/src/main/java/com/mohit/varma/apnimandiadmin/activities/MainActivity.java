package com.mohit.varma.apnimandiadmin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.util.Consumer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.activities.orderbyImage.OrdersByImageActivity;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.utilities.IsInternetConnectivity;
import com.mohit.varma.apnimandiadmin.utilities.Session;
import com.mohit.varma.apnimandiadmin.utilities.ShowSnackBar;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CardView MainActivityUserCardView, MainActivityProductCategoryCardView, MainActivityOrderCardView,MainActivityUtilsCardView,
            MainActivityOrderByImageCardView;
    private Toolbar mainActivityToolbar;
    private View MainActivityRootView;
    private DatabaseReference databaseReference;
    private Session session;
    private Context activity;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setClickableAndFocusableToCardViews();
        setDeliveryChargeToSharedPreference();

        //set consumers
        toolbarConsumer.accept(mainActivityToolbar);

        //set onclick listeners at textViews
        MainActivityUserCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUsersActivity();
            }
        });

        MainActivityProductCategoryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProductCategoryActivity();
            }
        });

        MainActivityOrderCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOrdersActivity();
            }
        });

        MainActivityUtilsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUtilsActivity();
            }
        });

        MainActivityOrderByImageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOrderByImageActivity();
            }
        });

    }


    public void initViews() {
        mainActivityToolbar = findViewById(R.id.mainActivityToolbar);
        MainActivityUserCardView = findViewById(R.id.MainActivityUserCardView);
        MainActivityProductCategoryCardView = findViewById(R.id.MainActivityProductCategoryCardView);
        MainActivityOrderCardView = findViewById(R.id.MainActivityOrderCardView);
        MainActivityUtilsCardView = (CardView) findViewById(R.id.MainActivityUtilsCardView);
        MainActivityRootView = findViewById(R.id.MainActivityRootView);
        MainActivityOrderByImageCardView = findViewById(R.id.MainActivityOrderByImageCardView);
        activity = this;
        this.session = new Session(activity);
        this.databaseReference = new MyDatabaseReference().getReference();
    }

    public void startUsersActivity() {
        if (activity != null) {
            if(IsInternetConnectivity.isConnected(activity)) {
                intent = new Intent(activity, UsersActivity.class);
                startActivity(intent);
            }else {
                ShowSnackBar.snackBar(activity,MainActivityRootView,activity.getResources().getString(R.string.please_check_internet_connectivity));
            }
        }
    }

    public void startProductCategoryActivity() {
        if (activity != null) {
            if(IsInternetConnectivity.isConnected(activity)) {
                intent = new Intent(activity, ProductCategoryActivity.class);
                startActivity(intent);
            }else {
                ShowSnackBar.snackBar(activity,MainActivityRootView,activity.getResources().getString(R.string.please_check_internet_connectivity));
            }
        }
    }

    public void startOrdersActivity() {
        if (activity != null) {
            if(IsInternetConnectivity.isConnected(activity)) {
                intent = new Intent(activity, OrdersActivity.class);
                startActivity(intent);
            }else {
                ShowSnackBar.snackBar(activity,MainActivityRootView,activity.getResources().getString(R.string.please_check_internet_connectivity));
            }
        }
    }

    public void startUtilsActivity(){
        if (activity != null) {
            if(IsInternetConnectivity.isConnected(activity)) {
                intent = new Intent(activity, UtilsActivity.class);
                startActivity(intent);
            }else {
                ShowSnackBar.snackBar(activity,MainActivityRootView,activity.getResources().getString(R.string.please_check_internet_connectivity));
            }
        }
    }

    Consumer<Toolbar> toolbarConsumer = mainActivityToolbar ->
    {
        if (activity != null) {
            setSupportActionBar(mainActivityToolbar);
            mainActivityToolbar.setTitle(activity.getResources().getString(R.string.admin));
        }
    };

    public void startOrderByImageActivity(){
        if(activity != null){
            if(IsInternetConnectivity.isConnected(activity)){
                if(IsInternetConnectivity.isConnected(activity)) {
                    intent = new Intent(activity, OrdersByImageActivity.class);
                    startActivity(intent);
                }else {
                    ShowSnackBar.snackBar(activity,MainActivityRootView,activity.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        }
    }

    public void setClickableAndFocusableToCardViews(){
        MainActivityUserCardView.setClickable(true);
        MainActivityUserCardView.setFocusable(true);
        MainActivityProductCategoryCardView.setClickable(true);
        MainActivityProductCategoryCardView.setFocusable(true);
        MainActivityOrderCardView.setClickable(true);
        MainActivityOrderCardView.setFocusable(true);
        MainActivityOrderByImageCardView.setClickable(true);
        MainActivityOrderByImageCardView.setFocusable(true);
    }

    public void setDeliveryChargeToSharedPreference(){
        databaseReference.child("Admin").child("deliveryFee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue() != null) {
                        long deliveryFee = (long) dataSnapshot.getValue();
//                        if(deliveryFee == 50){
//                            Toast.makeText(activity, "Access Allowed", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Intent intent = new Intent(MainActivity.this, AppClose.class);
//                            startActivity(intent);
//                            finish();
//                        }
                        session.setDeliveryCharge(deliveryFee);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
