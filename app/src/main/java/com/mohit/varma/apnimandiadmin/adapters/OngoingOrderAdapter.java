package com.mohit.varma.apnimandiadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.activities.OngoingItemViewDetailsActivity;
import com.mohit.varma.apnimandiadmin.model.OrderStatus;
import com.mohit.varma.apnimandiadmin.model.Orders;
import com.mohit.varma.apnimandiadmin.model.UCart;
import com.mohit.varma.apnimandiadmin.utilities.Constant;

import java.io.Serializable;
import java.util.List;

public class OngoingOrderAdapter extends RecyclerView.Adapter<OngoingOrderAdapter.OngoingOrderAdapterViewHolder> {
    private static final String TAG = "OngoingOrderAdapter";
    private Context context;
    private List<Orders> ordersList;
    private Orders orders;
    private DatabaseReference databaseReference;
    private View rootView;
    private ArrayAdapter<String> orderStatusArrayAdapter;
    private String[] orderStatuses = {"Order Status", "Processing", "Delivered", "Shipped", "Cancelled"};

    public OngoingOrderAdapter(Context context, List<Orders> ordersList, DatabaseReference databaseReference, View rootView) {
        this.context = context;
        this.ordersList = ordersList;
        this.databaseReference = databaseReference;
        this.rootView = rootView;
        orderStatusArrayAdapter = new ArrayAdapter<String>(context,
                R.layout.spinner_layout, orderStatuses);
        // Drop down layout style - list view with radio button
        orderStatusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public OngoingOrderAdapter.OngoingOrderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.on_going_single_item_view, parent, false);
        return new OngoingOrderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingOrderAdapterViewHolder holder, int position) {
        orders = ordersList.get(position);
        holder.MyOrderSingleItemOrderNumber.setText("Order No - " + orders.getOrderId());
        holder.MyOrderSingleItemGrandTotal.setText("Total: \u20B9" + orders.getGrandTotal());
        holder.MyOrderSingleItemOrderPlaceDate.setText("Date: " + orders.getOrderDate());
        setOrderStatus(orders.getOrderStatus(), holder.MyOrderSingleItemCurrentOrderStatus);
        if (orders.getUserAddress() != null) {
            holder.MyOrderSingleItemOrderUserName.setText(orders.getUserAddress().getUserName());
        }
        if (ordersList.get(position).getuCartList() != null && ordersList.get(position).getuCartList().size() > 0) {
            holder.ConstraintLayoutRootView.setVisibility(View.GONE);
            holder.MyOrderSingleItemRecyclerView.setVisibility(View.VISIBLE);
            setAdapter(ordersList.get(position).getuCartList(), holder.MyOrderSingleItemRecyclerView);
        } else {
            holder.ConstraintLayoutRootView.setVisibility(View.VISIBLE);
            holder.MyOrderSingleItemRecyclerView.setVisibility(View.GONE);
            holder.SummarySingleItemName.setText(ordersList.get(position).getuItem().getmItemName());
            holder.SummarySingleItemQuantity.setText("Qty: " + ordersList.get(position).getuItem().getmItemWeight());
            holder.SummarySingleItemPrice.setText("\u20B9" + ordersList.get(position).getuItem().getmItemPrice());
        }
        holder.MyOrderSingleItemSpinnerView.setAdapter(orderStatusArrayAdapter);
        holder.MyOrderSingleItemCustomerDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OngoingItemViewDetailsActivity.class);
                intent.putExtra("OngoingOrder", (Serializable) ordersList.get(position));
                context.startActivity(intent);
            }
        });
        holder.MyOrderSingleItemSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                String item = parent.getItemAtPosition(pos).toString();
                switch (item) {
                    case "Processing":
                        updateOrderStatus(position, OrderStatus.PROCESSING);
                        break;
                    case "Delivered":
                        updateOrderStatus(position, OrderStatus.DELIVERED);
                        break;
                    case "Shipped":
                        updateOrderStatus(position, OrderStatus.SHIPPED);
                        break;
                    case "Cancelled":
                        updateOrderStatus(position, OrderStatus.CANCELLED);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class OngoingOrderAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView MyOrderSingleItemOrderNumber, MyOrderSingleItemGrandTotal, MyOrderSingleItemOrderUserName,
                SummarySingleItemName, SummarySingleItemQuantity, SummarySingleItemPrice, MyOrderSingleItemCustomerDetailView, MyOrderSingleItemOrderPlaceDate, MyOrderSingleItemCurrentOrderStatus;
        private RecyclerView MyOrderSingleItemRecyclerView;
        private ConstraintLayout ConstraintLayoutRootView;
        private Spinner MyOrderSingleItemSpinnerView;

        public OngoingOrderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            MyOrderSingleItemOrderNumber = itemView.findViewById(R.id.MyOrderSingleItemOrderNumber);
            MyOrderSingleItemGrandTotal = itemView.findViewById(R.id.MyOrderSingleItemGrandTotal);
            MyOrderSingleItemRecyclerView = itemView.findViewById(R.id.MyOrderSingleItemRecyclerView);
            MyOrderSingleItemOrderUserName = itemView.findViewById(R.id.MyOrderSingleItemOrderUserName);
            MyOrderSingleItemCustomerDetailView = itemView.findViewById(R.id.MyOrderSingleItemCustomerDetailView);
            MyOrderSingleItemOrderPlaceDate = itemView.findViewById(R.id.MyOrderSingleItemOrderPlaceDate);
            MyOrderSingleItemSpinnerView = itemView.findViewById(R.id.MyOrderSingleItemSpinnerView);
            MyOrderSingleItemCurrentOrderStatus = itemView.findViewById(R.id.MyOrderSingleItemCurrentOrderStatus);
            SummarySingleItemName = itemView.findViewById(R.id.SummarySingleItemName);
            SummarySingleItemQuantity = itemView.findViewById(R.id.SummarySingleItemQuantity);
            SummarySingleItemPrice = itemView.findViewById(R.id.SummarySingleItemPrice);
            ConstraintLayoutRootView = itemView.findViewById(R.id.ConstraintLayoutRootView);

        }
    }

    public void setAdapter(List<UCart> uCartList, RecyclerView recyclerView) {
        if (uCartList != null && uCartList.size() > 0) {
            MyOrderSummaryAdapter myOrderSummaryAdapter = new MyOrderSummaryAdapter(context, uCartList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(myOrderSummaryAdapter);
        }
    }

    public void setOrderStatus(OrderStatus orderStatus, TextView MyOrderSingleItemOrderStatus) {
        switch (orderStatus) {
            case ORDER_PLACED:
                MyOrderSingleItemOrderStatus.setText("Current Status : " + Constant.ORDER_PLACED);
                break;
            case SHIPPED:
                MyOrderSingleItemOrderStatus.setText("Current Status : " + Constant.SHIPPED);
                break;
            case CANCELLED:
                MyOrderSingleItemOrderStatus.setText("Current Status : " + Constant.CANCELLED);
                break;
            case DELIVERED:
                MyOrderSingleItemOrderStatus.setText("Current Status : " + Constant.DELIVERED);
                break;
            case PROCESSING:
                MyOrderSingleItemOrderStatus.setText("Current Status : " + Constant.PROCESSING);
                break;
        }
    }

    public void updateOrderStatus(int position, OrderStatus orderStatus) {
        orders = ordersList.get(position);
        orders.setOrderStatus(orderStatus);
        Log.d(TAG, "onClick: " + new Gson().toJson(orders));
        databaseReference.child("Orders").orderByChild("orderId").equalTo(orders.getOrderId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        item.getRef().setValue(orders).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, context.getResources().getString(R.string.order_status_updated), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}