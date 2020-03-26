package com.mohit.varma.apnimandiadmin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.utilities.IsInternetConnectivity;
import com.mohit.varma.apnimandiadmin.utilities.ShowSnackBar;

import java.util.Objects;

import static com.mohit.varma.apnimandiadmin.utilities.Constant.BACKING;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.FRUIT;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.ITEM_KEY;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.PROTEIN;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.SNACKS;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.VEGETABLE;

public class ProductCategoryActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = ProductCategoryActivity.class.getSimpleName();
    private Toolbar productCategoryActivityToolbar;
    private Context activity;
    private CardView ProductCategoryActivityFruitsCardView, ProductCategoryActivityVegetablesCardView, ProductCategoryActivitySnacksCardView, ProductCategoryActivityProteinCardView, ProductCategoryActivityBackingCardView;
    private Intent intent;
    private View ProductCategoryActivityRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        initViews();
        setToolbar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setListener();
        productCategoryActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * initialize activity views
     */
    public void initViews() {
        productCategoryActivityToolbar = findViewById(R.id.productCategoryActivityToolbar);
        ProductCategoryActivityFruitsCardView = findViewById(R.id.ProductCategoryActivityFruitsCardView);
        ProductCategoryActivityVegetablesCardView = findViewById(R.id.ProductCategoryActivityVegetablesCardView);
        ProductCategoryActivitySnacksCardView = findViewById(R.id.ProductCategoryActivitySnacksCardView);
        ProductCategoryActivityProteinCardView = findViewById(R.id.ProductCategoryActivityProteinCardView);
        ProductCategoryActivityBackingCardView = findViewById(R.id.ProductCategoryActivityBackingCardView);
        ProductCategoryActivityRootView = findViewById(R.id.ProductCategoryActivityRootView);
        this.activity = this;
    }

    /**
     * set onclick listener to CardView
     */
    public void setListener() {
        ProductCategoryActivityFruitsCardView.setOnClickListener(this);
        ProductCategoryActivityVegetablesCardView.setOnClickListener(this);
        ProductCategoryActivitySnacksCardView.setOnClickListener(this);
        ProductCategoryActivityProteinCardView.setOnClickListener(this);
        ProductCategoryActivityBackingCardView.setOnClickListener(this);
    }

    /**
     * set toolbar
     */
    public void setToolbar() {
        if (productCategoryActivityToolbar != null && activity != null) {
            setSupportActionBar(productCategoryActivityToolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            productCategoryActivityToolbar.setTitle(activity.getResources().getString(R.string.product_category));
        }
    }

    /**
     * @param activity
     */
    public void startAddFruitsActivity(Context activity) {
        if (IsInternetConnectivity.isConnected(activity)) {
            intent = new Intent(activity, FruitsActivity.class);
            intent.putExtra(ITEM_KEY, FRUIT);
            activity.startActivity(intent);
        } else {
            ShowSnackBar.snackBar(activity, ProductCategoryActivityRootView, activity.getResources().getString(R.string.please_check_internet_connectivity));
        }
    }

    /**
     * @param activity
     */
    public void startAddVegetablesActivity(Context activity) {
        if (IsInternetConnectivity.isConnected(activity)) {
            intent = new Intent(activity, VegetablesActivity.class);
            intent.putExtra(ITEM_KEY, VEGETABLE);
            activity.startActivity(intent);
        } else {
            ShowSnackBar.snackBar(activity, ProductCategoryActivityRootView, activity.getResources().getString(R.string.please_check_internet_connectivity));
        }
    }

    /**
     * @param activity
     */
    public void startAddSnacksActivity(Context activity) {
        if (IsInternetConnectivity.isConnected(activity)) {
            intent = new Intent(activity, SnacksActivity.class);
            intent.putExtra(ITEM_KEY, SNACKS);
            activity.startActivity(intent);
        } else {
            ShowSnackBar.snackBar(activity, ProductCategoryActivityRootView, activity.getResources().getString(R.string.please_check_internet_connectivity));
        }
    }

    /**
     * @param activity
     */
    public void startAddProteinActivity(Context activity) {
        if (IsInternetConnectivity.isConnected(activity)) {
            intent = new Intent(activity, ProteinsActivity.class);
            intent.putExtra(ITEM_KEY, PROTEIN);
            activity.startActivity(intent);
        } else {
            ShowSnackBar.snackBar(activity, ProductCategoryActivityRootView, activity.getResources().getString(R.string.please_check_internet_connectivity));
        }
    }

    /**
     * @param activity
     */
    public void startAddBackingActivity(Context activity) {
        if (IsInternetConnectivity.isConnected(activity)) {
            intent = new Intent(activity, BackingsActivity.class);
            intent.putExtra(ITEM_KEY, BACKING);
            activity.startActivity(intent);
        } else {
            ShowSnackBar.snackBar(activity, ProductCategoryActivityRootView, activity.getResources().getString(R.string.please_check_internet_connectivity));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ProductCategoryActivityFruitsCardView:
                startAddFruitsActivity(activity);
                break;
            case R.id.ProductCategoryActivityVegetablesCardView:
                startAddVegetablesActivity(activity);
                break;
            case R.id.ProductCategoryActivitySnacksCardView:
                startAddSnacksActivity(activity);
                break;
            case R.id.ProductCategoryActivityProteinCardView:
                startAddProteinActivity(activity);
                break;
            case R.id.ProductCategoryActivityBackingCardView:
                startAddBackingActivity(activity);
                break;
        }
    }
}
