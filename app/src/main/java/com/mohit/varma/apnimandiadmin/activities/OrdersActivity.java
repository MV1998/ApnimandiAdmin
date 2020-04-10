package com.mohit.varma.apnimandiadmin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.model.Orders;

import java.util.LinkedList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private static final String TAG = "OrdersActivity";
    private TextView OrdersActivityNewOrdersTextView,OrdersActivityOngoingOrdersTextView,OrdersActivityPastOrdersTextView;
    private Toolbar OrdersActivityToolBar;
    private DatabaseReference databaseReference;
    private List<Orders> ordersList = new LinkedList<>();
    private List<Orders> cancelledOrders;
    private List<Orders> processingOrders;
    private List<Orders> newOrders;
    private ProgressDialog progressDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initViews();
        showProgressDialog();

        OrdersActivityToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ordersList.clear();
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    for (DataSnapshot Items : dataSnapshot.getChildren()) {
                        Orders orders = Items.getValue(Orders.class);
                        ordersList.add(orders);
                    }
                    if(ordersList != null && ordersList.size()>0){
                        Log.d(TAG, "onDataChange: " + new Gson().toJson(ordersList));
                        dismissProgressDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void initViews() {
        OrdersActivityToolBar = findViewById(R.id.OrdersActivityToolBar);
        OrdersActivityNewOrdersTextView = findViewById(R.id.OrdersActivityNewOrdersTextView);
        OrdersActivityOngoingOrdersTextView = findViewById(R.id.OrdersActivityOngoingOrdersTextView);
        OrdersActivityPastOrdersTextView = findViewById(R.id.OrdersActivityPastOrdersTextView);
        this.context = this;
        this.databaseReference = new MyDatabaseReference().getReference();
        this.progressDialog = new ProgressDialog(context);
    }

    public void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }
}
