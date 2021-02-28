package com.mohit.varma.apnimandiadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.activities.orderbyImage.OrderByImageUserOrdersActivity;

import java.util.ArrayList;

public class OrderByImageUserAdapter extends RecyclerView.Adapter<OrderByImageUserAdapter.OrderByImageUserAdapterViewHolder> {

    private Context context;
    private ArrayList<String> userPhoneNumber;

    public OrderByImageUserAdapter(Context context, ArrayList<String> userPhoneNumber) {
        this.context = context;
        this.userPhoneNumber = userPhoneNumber;
    }

    @NonNull
    @Override
    public OrderByImageUserAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_view_for_user_order_by_image, parent, false);
        return new OrderByImageUserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderByImageUserAdapterViewHolder holder, int position) {

        String userNumber = userPhoneNumber.get(position);

        holder.userPhoneNumberTextView.setText(userNumber);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderByImageUserOrdersActivity.class);
                intent.putExtra("userNumber", userPhoneNumber.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userPhoneNumber.size();
    }

    class OrderByImageUserAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView userPhoneNumberTextView;
        private View rootView;

        public OrderByImageUserAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            userPhoneNumberTextView = (TextView) itemView.findViewById(R.id.userPhoneNumberTextView);
            rootView = (View) itemView.findViewById(R.id.rootView);
        }
    }
}
