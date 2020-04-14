package com.mohit.varma.apnimandiadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.activities.OngoingItemViewDetailsActivity;
import com.mohit.varma.apnimandiadmin.activities.PastOrderItemViewActivity;
import com.mohit.varma.apnimandiadmin.model.OrderStatus;
import com.mohit.varma.apnimandiadmin.model.Orders;
import com.mohit.varma.apnimandiadmin.model.UCart;
import com.mohit.varma.apnimandiadmin.utilities.Constant;

import java.io.Serializable;
import java.util.List;

public class PastOrderAdapter extends RecyclerView.Adapter<PastOrderAdapter.PastOrderAdapterViewHolder> {
    private static final String TAG = "PastOrderAdapter";
    private Context context;
    private List<Orders> ordersList;
    private Orders orders;
    private DatabaseReference databaseReference;
    private View rootView;

    public PastOrderAdapter(Context context, List<Orders> ordersList, DatabaseReference databaseReference, View rootView) {
        this.context = context;
        this.ordersList = ordersList;
        this.databaseReference = databaseReference;
        this.rootView = rootView;
    }

    @NonNull
    @Override
    public PastOrderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.past_order_single_item_view, parent, false);
        return new PastOrderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastOrderAdapterViewHolder holder, int position) {
        orders = ordersList.get(position);
        holder.MyOrderSingleItemOrderNumber.setText("Order No - " + orders.getOrderId());
        holder.MyOrderSingleItemGrandTotal.setText("Total: \u20B9" + orders.getGrandTotal());
        holder.MyOrderSingleItemOrderPlaceDate.setText("Date: " + orders.getOrderDate());
        setOrderStatus(orders.getOrderStatus(),holder.MyOrderSingleItemCurrentOrderStatus);
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
        holder.MyOrderSingleItemCustomerDetailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PastOrderItemViewActivity.class);
                intent.putExtra("pastOrder", (Serializable) ordersList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class PastOrderAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView MyOrderSingleItemOrderNumber, MyOrderSingleItemGrandTotal, MyOrderSingleItemOrderUserName,
                SummarySingleItemName, SummarySingleItemQuantity, SummarySingleItemPrice, MyOrderSingleItemCustomerDetailView, MyOrderSingleItemOrderPlaceDate, MyOrderSingleItemCurrentOrderStatus;
        private RecyclerView MyOrderSingleItemRecyclerView;
        private ConstraintLayout ConstraintLayoutRootView;
        private Spinner MyOrderSingleItemSpinnerView;

        public PastOrderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            MyOrderSingleItemOrderNumber = itemView.findViewById(R.id.MyOrderSingleItemOrderNumber);
            MyOrderSingleItemGrandTotal = itemView.findViewById(R.id.MyOrderSingleItemGrandTotal);
            MyOrderSingleItemRecyclerView = itemView.findViewById(R.id.MyOrderSingleItemRecyclerView);
            MyOrderSingleItemOrderUserName = itemView.findViewById(R.id.MyOrderSingleItemOrderUserName);
            MyOrderSingleItemCustomerDetailView = itemView.findViewById(R.id.MyOrderSingleItemCustomerDetailView);
            MyOrderSingleItemOrderPlaceDate = itemView.findViewById(R.id.MyOrderSingleItemOrderPlaceDate);
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