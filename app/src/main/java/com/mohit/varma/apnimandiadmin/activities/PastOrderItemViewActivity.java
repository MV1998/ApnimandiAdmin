package com.mohit.varma.apnimandiadmin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.adapters.MyOrderSummaryAdapter;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.model.OrderStatus;
import com.mohit.varma.apnimandiadmin.model.Orders;
import com.mohit.varma.apnimandiadmin.model.UCart;
import com.mohit.varma.apnimandiadmin.utilities.Constant;
import com.mohit.varma.apnimandiadmin.utilities.Session;

import java.util.List;

public class PastOrderItemViewActivity extends AppCompatActivity {
    private static final String TAG = "PastOrderItemViewActivity";
    private Toolbar NewOrderItemViewDetailsActivityToolbar;
    private TextView MyOrderSingleItemOrderNumber, MyOrderSingleItemPlacedDate, MyOrderSingleItemOrderUserName,
            MyOrderSingleCustomerPhoneNumber, MyOrderSingleItemCustomerCallView, MyOrderSingleCustomerEmail, MyOrderSingleItemCustomerEmailView, MyOrderSingleCustomerNavigation, MyOrderSingleItemCustomerNavigationView,
            SummarySingleItemName, SummarySingleItemQuantity, SummarySingleItemPrice,MyOrderSingleItemCurrentOrderStatus,
            MyOrderSingleItemGrandTotalView, MyOrderSingleItemSubtotalView, MyOrderSingleItemDeliveryChargeView, MyOrderSingleItemServiceTaxView,
            MyOrderSingleItemDiscountView;
    private RecyclerView MyOrderSingleItemRecyclerView;
    private ConstraintLayout ConstraintLayoutRootView;
    private Orders orders;
    private Context context;
    private Session session;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_order_item_view);
        initViews();
        if (getIntent().getSerializableExtra("pastOrder") != null) {
            orders = (Orders) getIntent().getSerializableExtra("pastOrder");
        }
        if (orders != null) {
            calculation(orders.getGrandTotal());
            MyOrderSingleItemOrderNumber.setText("Order No - " + orders.getOrderId());
            MyOrderSingleItemPlacedDate.setText("Date: " + orders.getOrderDate());
            MyOrderSingleItemOrderUserName.setText(orders.getUserAddress().getUserName());
            MyOrderSingleCustomerPhoneNumber.setText(orders.getUserAddress().getPhoneNumber());
            MyOrderSingleCustomerEmail.setText("mv501049@gmail.com");
            setOrderStatus(orders.getOrderStatus(),MyOrderSingleItemCurrentOrderStatus);
            MyOrderSingleCustomerNavigation.setText(orders.getUserAddress().getAddressLine1() + " " + orders.getUserAddress().getAddressLine2() + " " + orders.getUserAddress().getCityCode());
            if (orders.getuCartList() != null && orders.getuCartList().size() > 0) {
                ConstraintLayoutRootView.setVisibility(View.GONE);
                MyOrderSingleItemRecyclerView.setVisibility(View.VISIBLE);
                setAdapter(orders.getuCartList(), MyOrderSingleItemRecyclerView);
            } else {
                ConstraintLayoutRootView.setVisibility(View.VISIBLE);
                MyOrderSingleItemRecyclerView.setVisibility(View.GONE);
                SummarySingleItemName.setText(orders.getuItem().getmItemName());
                SummarySingleItemQuantity.setText("Qty: " + orders.getuItem().getmItemWeight());
                SummarySingleItemPrice.setText("\u20B9" + orders.getuItem().getmItemPrice());
            }
        }

        NewOrderItemViewDetailsActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void initViews() {
        NewOrderItemViewDetailsActivityToolbar = findViewById(R.id.NewOrderItemViewDetailsActivityToolbar);
        MyOrderSingleItemOrderNumber = findViewById(R.id.MyOrderSingleItemOrderNumber);
        MyOrderSingleItemPlacedDate = findViewById(R.id.MyOrderSingleItemPlacedDate);
        MyOrderSingleItemOrderUserName = findViewById(R.id.MyOrderSingleItemOrderUserName);
        MyOrderSingleCustomerPhoneNumber = findViewById(R.id.MyOrderSingleCustomerPhoneNumber);
        MyOrderSingleItemCustomerCallView = findViewById(R.id.MyOrderSingleItemCustomerCallView);
        MyOrderSingleCustomerEmail = findViewById(R.id.MyOrderSingleCustomerEmail);
        MyOrderSingleItemCustomerEmailView = findViewById(R.id.MyOrderSingleItemCustomerEmailView);
        MyOrderSingleCustomerNavigation = findViewById(R.id.MyOrderSingleCustomerNavigation);
        MyOrderSingleItemCustomerNavigationView = findViewById(R.id.MyOrderSingleItemCustomerNavigationView);
        MyOrderSingleItemRecyclerView = findViewById(R.id.MyOrderSingleItemRecyclerView);
        MyOrderSingleItemCurrentOrderStatus = findViewById(R.id.MyOrderSingleItemCurrentOrderStatus);
        MyOrderSingleItemGrandTotalView = findViewById(R.id.MyOrderSingleItemGrandTotalView);
        MyOrderSingleItemSubtotalView = findViewById(R.id.MyOrderSingleItemSubtotalView);
        MyOrderSingleItemServiceTaxView = findViewById(R.id.MyOrderSingleItemServiceTaxView);
        MyOrderSingleItemDeliveryChargeView = findViewById(R.id.MyOrderSingleItemDeliveryChargeView);
        MyOrderSingleItemDiscountView = findViewById(R.id.MyOrderSingleItemDiscountView);
        ConstraintLayoutRootView = findViewById(R.id.ConstraintLayoutRootView);
        SummarySingleItemName = findViewById(R.id.SummarySingleItemName);
        SummarySingleItemQuantity = findViewById(R.id.SummarySingleItemQuantity);
        SummarySingleItemPrice = findViewById(R.id.SummarySingleItemPrice);
        this.context = this;
        this.session = new Session(context);
        this.databaseReference = new MyDatabaseReference().getReference();
    }
    public void setAdapter(List<UCart> uCartList, RecyclerView recyclerView) {
        if (uCartList != null && uCartList.size() > 0) {
            MyOrderSummaryAdapter myOrderSummaryAdapter = new MyOrderSummaryAdapter(context, uCartList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(myOrderSummaryAdapter);
        }
    }

    public void calculation(long grandTotal) {
        long deliveryCharge = session.getDeliveryCharge();
        long subTotal = grandTotal - deliveryCharge;
        MyOrderSingleItemDeliveryChargeView.setText("Delivery Charge : \u20B9" + deliveryCharge);
        MyOrderSingleItemSubtotalView.setText("Subtotal : \u20B9" + subTotal);
        MyOrderSingleItemGrandTotalView.setText("Grand Total : \u20B9" + grandTotal);
        try {
            if(orders.getuCartList()!=null && orders.getuCartList().size()>0){
                long discount = (((orders.getuCartList().get(0).getmItemCutOffPrice()) * grandTotal))/100;
                MyOrderSingleItemDiscountView.setText("-Discount "+"("+orders.getuCartList().get(0).getmItemCutOffPrice()+"%) : \u20B9"+discount);
            }else {
                long discount = (((orders.getuItem().getmItemCutOffPrice()) * grandTotal))/100;
                MyOrderSingleItemDiscountView.setText("-Discount "+"("+orders.getuItem().getmItemCutOffPrice()+"%) : \u20B9"+discount);
            }
        } catch (Exception e) {

        }
        MyOrderSingleItemServiceTaxView.setText("+Service Tax (20%) : \u20B9" + 0);
    }
    public void setOrderStatus(OrderStatus orderStatus, TextView MyOrderSingleItemOrderStatus) {
        switch (orderStatus) {
            case CANCELLED:
                MyOrderSingleItemOrderStatus.setTextColor(Color.parseColor("#fe363b"));
                MyOrderSingleItemOrderStatus.setText("Order Status : " + Constant.CANCELLED);
                break;
            case DELIVERED:
                MyOrderSingleItemOrderStatus.setTextColor(Color.parseColor("#45ae6a"));
                MyOrderSingleItemOrderStatus.setText("Order Status : " + Constant.DELIVERED);
                break;
        }
    }
}
