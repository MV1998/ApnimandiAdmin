package com.mohit.varma.apnimandiadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.mohit.varma.apnimandiadmin.activities.NewOrderItemViewDetailsActivity;
import com.mohit.varma.apnimandiadmin.interfaces.IPhoneCallBackPermission;
import com.mohit.varma.apnimandiadmin.model.OrderStatus;
import com.mohit.varma.apnimandiadmin.model.Orders;
import com.mohit.varma.apnimandiadmin.model.UCart;

import java.io.Serializable;
import java.util.List;

public class OngoingOrderAdapter extends RecyclerView.Adapter<OngoingOrderAdapter.OngoingOrderAdapterViewHolder> {
    private static final String TAG = "MyOrderAdapter";
    private Context context;
    private List<Orders> ordersList;
    private Orders orders;
    private DatabaseReference databaseReference;
    private IPhoneCallBackPermission iPhoneCallBackPermission;

    public OngoingOrderAdapter(Context context, List<Orders> ordersList, DatabaseReference databaseReference, IPhoneCallBackPermission iPhoneCallBackPermission) {
        this.context = context;
        this.ordersList = ordersList;
        this.databaseReference = databaseReference;
        this.iPhoneCallBackPermission = iPhoneCallBackPermission;
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
        holder.MyOrderSingleItemGrandTotal.setText("\u20B9" + orders.getGrandTotal());
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
        holder.MyOrderSingleItemCallCustomerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iPhoneCallBackPermission != null) {
                    iPhoneCallBackPermission.checkPermissionAndProvideOrders(ordersList.get(position));
                }
            }
        });
        holder.MyOrderSingleItemCustomerDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewOrderItemViewDetailsActivity.class);
                intent.putExtra("newOrder",(Serializable) ordersList.get(position));
                context.startActivity(intent);
            }
        });
        holder.MyOrderSingleItemCancelledOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders = ordersList.get(position);
                orders.setOrderStatus(OrderStatus.CANCELLED);
                Log.d(TAG, "onClick: " + new Gson().toJson(orders));
                databaseReference.child("Orders").orderByChild("orderId").equalTo(orders.getOrderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                item.getRef().setValue(orders).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
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
        });
        holder.MyOrderSingleItemAcceptOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders = ordersList.get(position);
                orders.setOrderStatus(OrderStatus.PROCESSING);
                Log.d(TAG, "onClick: " + new Gson().toJson(orders));
                databaseReference.child("Orders").orderByChild("orderId").equalTo(orders.getOrderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                item.getRef().setValue(orders).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

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
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class OngoingOrderAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView MyOrderSingleItemOrderNumber, MyOrderSingleItemGrandTotal, MyOrderSingleItemOrderUserName,
                SummarySingleItemName, SummarySingleItemQuantity, SummarySingleItemPrice, MyOrderSingleItemCallCustomerView, MyOrderSingleItemCustomerDetailView, MyOrderSingleItemCancelledOrderView, MyOrderSingleItemAcceptOrderView;
        private RecyclerView MyOrderSingleItemRecyclerView;
        private ConstraintLayout ConstraintLayoutRootView;

        public OngoingOrderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            MyOrderSingleItemOrderNumber = itemView.findViewById(R.id.MyOrderSingleItemOrderNumber);
            MyOrderSingleItemGrandTotal = itemView.findViewById(R.id.MyOrderSingleItemGrandTotal);
            MyOrderSingleItemRecyclerView = itemView.findViewById(R.id.MyOrderSingleItemRecyclerView);
            MyOrderSingleItemOrderUserName = itemView.findViewById(R.id.MyOrderSingleItemOrderUserName);
            MyOrderSingleItemCallCustomerView = itemView.findViewById(R.id.MyOrderSingleItemCallCustomerView);
            MyOrderSingleItemCustomerDetailView = itemView.findViewById(R.id.MyOrderSingleItemCustomerDetailView);
            MyOrderSingleItemCancelledOrderView = itemView.findViewById(R.id.MyOrderSingleItemCancelledOrderView);
            MyOrderSingleItemAcceptOrderView = itemView.findViewById(R.id.MyOrderSingleItemAcceptOrderView);
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
}