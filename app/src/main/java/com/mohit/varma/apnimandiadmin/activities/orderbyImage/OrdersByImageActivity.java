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
import com.mohit.varma.apnimandiadmin.adapters.OrderByImageUserAdapter;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;

import java.util.ArrayList;

public class OrdersByImageActivity extends AppCompatActivity {

    private static final String TAG = OrdersByImageActivity.class.getSimpleName();
    private DatabaseReference databaseReference;
    private ArrayList<String> userPhoneNumber = new ArrayList<>();
    private Context activity;
    private ProgressDialog progressDialog;
    private Toolbar OrderByImageActivityToolbar;
    private RecyclerView orders_by_image_recycler_view;
    private OrderByImageUserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_by_image);

        initViews();

        setSupportActionBar(OrderByImageActivityToolbar);
        showProgressDialog();

        setSupportActionBar(OrderByImageActivityToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        databaseReference.child("OrderByImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    userPhoneNumber.clear();
                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                        for (DataSnapshot Items : dataSnapshot.getChildren()) {
                           String phoneNumber = Items.getKey();
                            userPhoneNumber.add(phoneNumber);
                        }
                        Log.d(TAG, "onDataChange: " + userPhoneNumber.size());
                        if (userPhoneNumber != null && userPhoneNumber.size() > 0) {
                            if (userAdapter != null) {
                                dismissProgressDialog();
                                Log.d(TAG, "testing1");
                                orders_by_image_recycler_view.setVisibility(View.VISIBLE);
//                                AttaActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                userAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "testing2");
                                orders_by_image_recycler_view.setVisibility(View.VISIBLE);
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


    public void initViews(){
        this.activity = this;
        databaseReference = new MyDatabaseReference().getReference();
        progressDialog = new ProgressDialog(activity);
        orders_by_image_recycler_view = findViewById(R.id.orders_by_image_recycler_view);
        OrderByImageActivityToolbar = findViewById(R.id.OrderByImageActivityToolbar);
    }

    public void setAdapter() {
        if (userPhoneNumber != null && userPhoneNumber.size() > 0) {
            orders_by_image_recycler_view.setLayoutManager(new LinearLayoutManager(activity));
            orders_by_image_recycler_view.setHasFixedSize(true);
            userAdapter = new OrderByImageUserAdapter(activity, userPhoneNumber);
            orders_by_image_recycler_view.setAdapter(userAdapter);
        }
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


}