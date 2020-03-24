package com.mohit.varma.apnimandiadmin.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.interfaces.IUpdateItemCallBack;
import com.mohit.varma.apnimandiadmin.model.UItem;
import com.mohit.varma.apnimandiadmin.utilities.Constant;
import com.mohit.varma.apnimandiadmin.utilities.IsInternetConnectivity;
import com.mohit.varma.apnimandiadmin.utilities.ShowSnackBar;

import java.util.List;

public class VegetableItemAdapter extends RecyclerView.Adapter<VegetableItemAdapter.VegetableItemAdapterViewHolder> {
    public static final String TAG = VegetableItemAdapter.class.getSimpleName();
    private Context context;
    private List<UItem> uItemList;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private DatabaseReference reference;
    private View rootView;
    private IUpdateItemCallBack iUpdateItemCallBack;

    public VegetableItemAdapter(Context context, List<UItem> uItemList, DatabaseReference reference, View rootView, IUpdateItemCallBack iUpdateItemCallBack) {
        this.context = context;
        this.uItemList = uItemList;
        this.reference = reference;
        this.rootView = rootView;
        this.iUpdateItemCallBack = iUpdateItemCallBack;
        builder = new AlertDialog.Builder(context);
    }

    @NonNull
    @Override
    public VegetableItemAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_category_single_item_view, parent, false);
        return new VegetableItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VegetableItemAdapterViewHolder holder, int position) {

        final UItem uItem = uItemList.get(position);
        holder.ProductCategoryItemIdTextView.setText("Item Id : " + uItem.getmItemId());
        holder.ProductCategoryItemCutOffPriceTextView.setText("Item Cut Off Price : " + uItem.getmItemCutOffPrice());
        holder.ProductCategoryItemPriceTextView.setText("Item Price : " + uItem.getmItemPrice());
        holder.ProductCategoryItemNameTextView.setText("Item Name : " + uItem.getmItemName());
        holder.ProductCategoryItemWeightTextView.setText("Item weight : " + uItem.getmItemWeight());
        holder.ProductCategoryItemCategoryTextView.setText("Item Category : " + uItem.getmItemCategory());

        if (uItem.getmItemImage() != null && !uItem.getmItemImage().isEmpty()) {
            setImageToGlide(uItem.getmItemImage(), holder.ProductCategoryItemImageView);
        }

        holder.ProductCategoryItemDeleteButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsInternetConnectivity.isConnected(context)) {
                    builder.setMessage("Are you to delete?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (alertDialog != null && alertDialog.isShowing()) {
                                alertDialog.dismiss();
                            }
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(uItem.getmItemImage());
                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                            reference.child(Constant.ITEMS).child(Constant.VEGETABLE).orderByChild("mItemId").equalTo(uItem.getmItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                                        item.getRef().removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.item_deleted_successfully));
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.item_id_not_found));
                                }
                            });
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (alertDialog != null && alertDialog.isShowing()) {
                                alertDialog.dismiss();
                            }
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });

        holder.ProductCategoryItemUpdateButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsInternetConnectivity.isConnected(context)) {
                    if (iUpdateItemCallBack != null) {
                        iUpdateItemCallBack.updateItem(uItem);
                    }
                } else {
                    ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return uItemList.size();
    }

    public class VegetableItemAdapterViewHolder extends RecyclerView.ViewHolder {
        private CardView ProductCategoryItemCardView;
        private ImageView ProductCategoryItemImageView;
        private TextView ProductCategoryItemIdTextView, ProductCategoryItemCutOffPriceTextView, ProductCategoryItemPriceTextView, ProductCategoryItemNameTextView, ProductCategoryItemWeightTextView, ProductCategoryItemCategoryTextView;
        private Button ProductCategoryItemDeleteButtonView, ProductCategoryItemUpdateButtonView;

        public VegetableItemAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductCategoryItemCardView = (CardView) itemView.findViewById(R.id.ProductCategoryItemCardView);
            ProductCategoryItemImageView = (ImageView) itemView.findViewById(R.id.ProductCategoryItemImageView);
            ProductCategoryItemIdTextView = (TextView) itemView.findViewById(R.id.ProductCategoryItemIdTextView);
            ProductCategoryItemCutOffPriceTextView = (TextView) itemView.findViewById(R.id.ProductCategoryItemCutOffPriceTextView);
            ProductCategoryItemPriceTextView = (TextView) itemView.findViewById(R.id.ProductCategoryItemPriceTextView);
            ProductCategoryItemNameTextView = (TextView) itemView.findViewById(R.id.ProductCategoryItemNameTextView);
            ProductCategoryItemWeightTextView = (TextView) itemView.findViewById(R.id.ProductCategoryItemWeightTextView);
            ProductCategoryItemCategoryTextView = (TextView) itemView.findViewById(R.id.ProductCategoryItemCategoryTextView);
            ProductCategoryItemDeleteButtonView = (Button) itemView.findViewById(R.id.ProductCategoryItemDeleteButtonView);
            ProductCategoryItemUpdateButtonView = (Button) itemView.findViewById(R.id.ProductCategoryItemUpdateButtonView);
        }
    }

    public void setImageToGlide(String image_url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.market)
                .error(R.drawable.market);
        Glide.with(context).load(image_url).apply(options).apply(RequestOptions.centerInsideTransform()).into(imageView);
    }
}
