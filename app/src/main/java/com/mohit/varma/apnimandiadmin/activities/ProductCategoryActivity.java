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
import java.util.function.Consumer;

import static com.mohit.varma.apnimandiadmin.utilities.Constant.FRUIT;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.ITEM_KEY;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.VEGETABLE;

public class ProductCategoryActivity extends AppCompatActivity {

    private Toolbar productCategoryActivityToolbar;
    private Context activity;
    private CardView ProductCategoryActivityFruitsCardView, ProductCategoryActivityVegetablesCardView;
    private Intent intent;
    private View ProductCategoryActivityRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        initViews();

        //set consumers
        toolbarConsumer.accept(productCategoryActivityToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ProductCategoryFruitsConsumer.accept(ProductCategoryActivityFruitsCardView);
        ProductCategoryVegetablesConsumer.accept(ProductCategoryActivityVegetablesCardView);

        productCategoryActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void initViews() {
        productCategoryActivityToolbar = (Toolbar) findViewById(R.id.productCategoryActivityToolbar);
        ProductCategoryActivityFruitsCardView = (CardView) findViewById(R.id.ProductCategoryActivityFruitsCardView);
        ProductCategoryActivityVegetablesCardView = (CardView) findViewById(R.id.ProductCategoryActivityVegetablesCardView);
        ProductCategoryActivityRootView = (View) findViewById(R.id.ProductCategoryActivityRootView);
        this.activity = this;
    }

    Consumer<Toolbar> toolbarConsumer = productCategoryActivityToolbar -> {
        if (productCategoryActivityToolbar != null && activity != null) {
            setSupportActionBar(productCategoryActivityToolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            productCategoryActivityToolbar.setTitle(activity.getResources().getString(R.string.product_category));
        }
    };


    Consumer<CardView> ProductCategoryVegetablesConsumer = ProductCategoryVegetables -> {
        ProductCategoryVegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddVegetablesActivityConsumer.accept(activity);
            }
        });
    };

    Consumer<CardView> ProductCategoryFruitsConsumer = ProductCategoryFruits -> {
        ProductCategoryFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddFruitsActivityConsumer.accept(activity);
            }
        });
    };

    Consumer<Context> startAddFruitsActivityConsumer = context -> {
        if (IsInternetConnectivity.isConnected(activity)) {
            intent = new Intent(activity, FruitsActivity.class);
            intent.putExtra(ITEM_KEY,FRUIT);
            context.startActivity(intent);
        } else {
            ShowSnackBar.snackBar(activity, ProductCategoryActivityRootView,activity.getResources().getString(R.string.please_check_internet_connectivity));
        }
    };

    Consumer<Context> startAddVegetablesActivityConsumer = context -> {
        if (IsInternetConnectivity.isConnected(activity)) {
            intent = new Intent(activity, VegetablesActivity.class);
            intent.putExtra(ITEM_KEY,VEGETABLE);
            context.startActivity(intent);
        } else {
            ShowSnackBar.snackBar(activity, ProductCategoryActivityRootView,activity.getResources().getString(R.string.please_check_internet_connectivity));
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
