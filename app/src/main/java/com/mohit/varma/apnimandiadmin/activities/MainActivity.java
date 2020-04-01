package com.mohit.varma.apnimandiadmin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.util.Consumer;

import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.utilities.IsInternetConnectivity;
import com.mohit.varma.apnimandiadmin.utilities.ShowSnackBar;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CardView MainActivityUserCardView, MainActivityProductCategoryCardView, MainActivityOrderCardView,MainActivityUtilsCardView;
    private Toolbar mainActivityToolbar;
    private View MainActivityRootView;
    private Context activity;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setClickableAndFocusableToCardViews();

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

    }


    public void initViews() {
        mainActivityToolbar = findViewById(R.id.mainActivityToolbar);
        MainActivityUserCardView = findViewById(R.id.MainActivityUserCardView);
        MainActivityProductCategoryCardView = findViewById(R.id.MainActivityProductCategoryCardView);
        MainActivityOrderCardView = findViewById(R.id.MainActivityOrderCardView);
        MainActivityUtilsCardView = (CardView) findViewById(R.id.MainActivityUtilsCardView);
        MainActivityRootView = findViewById(R.id.MainActivityRootView);
        activity = this;
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

    public void setClickableAndFocusableToCardViews(){
        MainActivityUserCardView.setClickable(true);
        MainActivityUserCardView.setFocusable(true);
        MainActivityProductCategoryCardView.setClickable(true);
        MainActivityProductCategoryCardView.setFocusable(true);
        MainActivityOrderCardView.setClickable(true);
        MainActivityOrderCardView.setFocusable(true);
    }
}
