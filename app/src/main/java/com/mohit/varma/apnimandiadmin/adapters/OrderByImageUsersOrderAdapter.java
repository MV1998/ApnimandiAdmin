package com.mohit.varma.apnimandiadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.model.OrderByImage;

import java.util.ArrayList;

public class OrderByImageUsersOrderAdapter extends RecyclerView.Adapter<OrderByImageUsersOrderAdapter.OrderByImageUsersOrderAdapterViewHolder> {

    private Context context;
    private ArrayList<OrderByImage> arrayList;

    public OrderByImageUsersOrderAdapter(Context context, ArrayList<OrderByImage> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public OrderByImageUsersOrderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_view_for_order_by_image_user_order, parent, false);
        return new OrderByImageUsersOrderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderByImageUsersOrderAdapterViewHolder holder, int position) {

        OrderByImage orderByImage = arrayList.get(position);


        setImageToGlide(orderByImage.getImageURL(), holder.orderImageView);
        holder.phoneNumber.setText(orderByImage.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class OrderByImageUsersOrderAdapterViewHolder extends RecyclerView.ViewHolder{

        private ImageView orderImageView;
        private TextView phoneNumber;

        public OrderByImageUsersOrderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            orderImageView = (ImageView) itemView.findViewById(R.id.ordered_image);
            phoneNumber = (TextView) itemView.findViewById(R.id.user_phone_number_text_view);
        }
    }

    public void setImageToGlide(String image_url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.market)
                .error(R.drawable.market);
        Glide.with(context).load(image_url).apply(options).apply(RequestOptions.centerInsideTransform()).into(imageView);
    }
}
