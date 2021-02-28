package com.mohit.varma.apnimandiadmin.activities.orderbyImage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.adapters.OrderByImageUsersOrderAdapter;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.model.OrderByImage;

import java.util.ArrayList;

public class OrderByImageUserOrdersActivity extends AppCompatActivity {

    private static final String TAG = OrderByImageUserOrdersActivity.class.getSimpleName();
    private DatabaseReference databaseReference;
    private Context activity;
    String userNumber = "";
    private ProgressDialog progressDialog;
    private Toolbar users_order_image_by_ordered_toolbar;
    private RecyclerView users_orders_recycler_view;
    private OrderByImageUsersOrderAdapter userAdapter;
    private ArrayList<OrderByImage> orderByImagesList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_by_image_user_orders);

        initViews();

        userNumber = getIntent().getExtras().getString("userNumber");

        Log.d("User Order", "onCreate: " + userNumber);


        databaseReference.child("OrderByImage").child(userNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    orderByImagesList.clear();
                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                        for (DataSnapshot Items : dataSnapshot.getChildren()) {
                            OrderByImage item = Items.getValue(OrderByImage.class);
                            orderByImagesList.add(item);
                        }
                        Log.d(TAG, "onDataChange: " + orderByImagesList.size());
                        if (orderByImagesList != null && orderByImagesList.size() > 0) {
                            if (userAdapter != null) {
                                dismissProgressDialog();
                                Log.d(TAG, "testing1");
                                users_orders_recycler_view.setVisibility(View.VISIBLE);
//                                AttaActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                userAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "testing2");
                                users_orders_recycler_view.setVisibility(View.VISIBLE);
                                // AttaActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                setAdapter();
                                dismissProgressDialog();
                            }
                        }
                    } else {
                        dismissProgressDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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


    public void initViews(){
        this.activity = this;
        databaseReference = new MyDatabaseReference().getReference();
        progressDialog = new ProgressDialog(activity);
        users_orders_recycler_view = findViewById(R.id.users_orders_recycler_view);
        users_order_image_by_ordered_toolbar = findViewById(R.id.users_order_image_by_ordered_toolbar);
    }

    public void setAdapter() {
        if (orderByImagesList != null && orderByImagesList.size() > 0) {
            users_orders_recycler_view.setLayoutManager(new LinearLayoutManager(activity));
            users_orders_recycler_view.setHasFixedSize(true);
            userAdapter = new OrderByImageUsersOrderAdapter(activity, orderByImagesList);
            users_orders_recycler_view.setAdapter(userAdapter);
        }
    }

}