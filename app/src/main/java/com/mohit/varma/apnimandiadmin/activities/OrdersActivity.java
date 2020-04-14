package com.mohit.varma.apnimandiadmin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.fragments.NewOrderFragment;
import com.mohit.varma.apnimandiadmin.fragments.OngoingOrderFragment;
import com.mohit.varma.apnimandiadmin.fragments.PastOrderFragment;
import com.mohit.varma.apnimandiadmin.model.OrderStatus;
import com.mohit.varma.apnimandiadmin.model.Orders;
import com.mohit.varma.apnimandiadmin.utilities.IsInternetConnectivity;
import com.mohit.varma.apnimandiadmin.utilities.ShowSnackBar;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private static final String TAG = "OrdersActivity";
    private TextView OrdersActivityNewOrdersTextView,OrdersActivityOngoingOrdersTextView,OrdersActivityPastOrdersTextView;
    private SwipeRefreshLayout OrdersActivitySwipeFreshLayout;
    private Toolbar OrdersActivityToolBar;
    private DatabaseReference databaseReference;
    private List<Orders> ordersList = new LinkedList<>();
    private List<Orders> cancelledOrders;
    private List<Orders> processingOrders;
    private List<Orders> newOrders;
    private ProgressDialog progressDialog;
    private View MyOrdersActivityRootView;
    private Context context;
    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;
    private Bundle bundle;

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
                        newOrders = new LinkedList<>();
                        processingOrders = new LinkedList<>();
                        cancelledOrders = new LinkedList<>();
                        for(int i=0;i<ordersList.size();i++){
                            if(ordersList.get(i).getOrderStatus() == OrderStatus.ORDER_PLACED){
                                newOrders.add(ordersList.get(i));
                            }else if(ordersList.get(i).getOrderStatus() == OrderStatus.PROCESSING ||
                                    ordersList.get(i).getOrderStatus() == OrderStatus.SHIPPED){
                                processingOrders.add(ordersList.get(i));
                            }else {
                                cancelledOrders.add(ordersList.get(i));
                            }
                        }
                        Log.d(TAG, "newOrders: " + new Gson().toJson(newOrders));
                        dismissProgressDialog();
                        initializeOnGoingFragment();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setListener();
    }

    public void initViews() {
        OrdersActivityToolBar = findViewById(R.id.OrdersActivityToolBar);
        OrdersActivityNewOrdersTextView = findViewById(R.id.OrdersActivityNewOrdersTextView);
        OrdersActivityOngoingOrdersTextView = findViewById(R.id.OrdersActivityOngoingOrdersTextView);
        OrdersActivityPastOrdersTextView = findViewById(R.id.OrdersActivityPastOrdersTextView);
        OrdersActivitySwipeFreshLayout = findViewById(R.id.OrdersActivitySwipeFreshLayout);
        MyOrdersActivityRootView = findViewById(R.id.MyOrdersActivityRootView);
        this.context = this;
        this.bundle = new Bundle();
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

    public void initializeOnGoingFragment() {
        try {
            OrdersActivityNewOrdersTextView.setTextColor(Color.parseColor("#ee6002"));
            OrdersActivityOngoingOrdersTextView.setTextColor(Color.parseColor("#000000"));
            OrdersActivityPastOrdersTextView.setTextColor(Color.parseColor("#000000"));
        } catch (Exception e) {

        }
        fragment = null;
        fragment = new NewOrderFragment();
        bundle.putSerializable("newOrders", (Serializable) newOrders);
        fragment.setArguments(bundle);
        if (fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter,
                    R.anim.exit);
            fragmentTransaction.replace(R.id.OrdersContainer, fragment, "newOrders");
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    void setListener(){
        OrdersActivityNewOrdersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsInternetConnectivity.isConnected(context)){
                    try {
                        OrdersActivityNewOrdersTextView.setTextColor(Color.parseColor("#ee6002"));
                        OrdersActivityOngoingOrdersTextView.setTextColor(Color.parseColor("#000000"));
                        OrdersActivityPastOrdersTextView.setTextColor(Color.parseColor("#000000"));
                    } catch (Exception e) {

                    }
                    fragment = null;
                    fragment = new NewOrderFragment();
                    bundle.putSerializable("newOrders", (Serializable) newOrders);
                    fragment.setArguments(bundle);
                    if (fragment != null) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter,
                                R.anim.exit);
                        fragmentTransaction.replace(R.id.OrdersContainer, fragment, "newOrders");
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                }else {
                    ShowSnackBar.snackBar(context, MyOrdersActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });
        OrdersActivityOngoingOrdersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsInternetConnectivity.isConnected(context)){
                    try {
                        OrdersActivityNewOrdersTextView.setTextColor(Color.parseColor("#000000"));
                        OrdersActivityOngoingOrdersTextView.setTextColor(Color.parseColor("#ee6002"));
                        OrdersActivityPastOrdersTextView.setTextColor(Color.parseColor("#000000"));
                    } catch (Exception e) {

                    }
                    fragment = null;
                    fragment = new OngoingOrderFragment();
                    bundle.putSerializable("onGoingOrder", (Serializable) processingOrders);
                    fragment.setArguments(bundle);
                    if (fragment != null) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter,
                                R.anim.exit);
                        fragmentTransaction.replace(R.id.OrdersContainer, fragment, "onGoingOrder");
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                }else {
                    ShowSnackBar.snackBar(context, MyOrdersActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });
        OrdersActivityPastOrdersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsInternetConnectivity.isConnected(context)){
                    try {
                        OrdersActivityNewOrdersTextView.setTextColor(Color.parseColor("#000000"));
                        OrdersActivityOngoingOrdersTextView.setTextColor(Color.parseColor("#000000"));
                        OrdersActivityPastOrdersTextView.setTextColor(Color.parseColor("#ee6002"));
                    } catch (Exception e) {

                    }
                    fragment = null;
                    fragment = new PastOrderFragment();
                    bundle.putSerializable("pastOrder", (Serializable) cancelledOrders);
                    fragment.setArguments(bundle);
                    if (fragment != null) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter,
                                R.anim.exit);
                        fragmentTransaction.replace(R.id.OrdersContainer, fragment, "pastOrder");
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                }else {
                    ShowSnackBar.snackBar(context, MyOrdersActivityRootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });
    }
}
