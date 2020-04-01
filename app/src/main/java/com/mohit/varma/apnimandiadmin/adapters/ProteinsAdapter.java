package com.mohit.varma.apnimandiadmin.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
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

import static com.mohit.varma.apnimandiadmin.utilities.Constant.ITEMS;

public class ProteinsAdapter extends RecyclerView.Adapter<ProteinsAdapter.ProteinsAdapterViewHolder> {
    public static final String TAG = VegetableItemAdapter.class.getSimpleName();
    private Context context;
    private List<UItem> uItemList;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private DatabaseReference reference;
    private View rootView;
    private IUpdateItemCallBack iUpdateItemCallBack;

    public ProteinsAdapter(Context context, List<UItem> uItemList, DatabaseReference reference, View rootView, IUpdateItemCallBack iUpdateItemCallBack) {
        this.context = context;
        this.uItemList = uItemList;
        this.reference = reference;
        this.rootView = rootView;
        this.iUpdateItemCallBack = iUpdateItemCallBack;
        builder = new AlertDialog.Builder(context);
    }

    @NonNull
    @Override
    public ProteinsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_category_single_item_view, parent, false);
        return new ProteinsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProteinsAdapterViewHolder holder, int position) {

        final UItem uItem = uItemList.get(position);
        holder.ProductCategoryItemIdTextView.setText("" + uItem.getmItemId());
        holder.ProductCategoryItemCutOffPriceTextView.setText("\u20B9" + uItem.getmItemCutOffPrice());
        holder.ProductCategoryItemPriceTextView.setText("\u20B9" + uItem.getmItemPrice());
        holder.ProductCategoryItemNameTextView.setText("" + uItem.getmItemName());
        holder.ProductCategoryItemWeightTextView.setText("" + uItem.getmItemWeight());
        holder.ProductCategoryItemCategoryTextView.setText("" + uItem.getmItemCategory());
        holder.ProductCategoryItemCaloriesTextView.setText(""+uItem.getuItemDescription().getItemCalories());
        holder.ProductCategoryItemFatTextView.setText(""+uItem.getuItemDescription().getItemFat());
        holder.ProductCategoryItemProteinTextView.setText(""+uItem.getuItemDescription().getItemProtein());

        if (uItem.getmItemImage() != null && !uItem.getmItemImage().isEmpty()) {
            setImageToGlide(uItem.getmItemImage(), holder.ProductCategoryItemImageView);
        }

        if (uItemList != null && uItemList.size() > 0) {
            if (uItemList.get(position).isPopular()) {
                holder.ProductCategoryItemCardView.setCardBackgroundColor(Color.parseColor("#90EE90"));
            } else {
                holder.ProductCategoryItemCardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
            }
        }

        holder.ProductCategoryMaterialItemDeleteButton.setOnClickListener(new View.OnClickListener() {
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
                            reference.child(Constant.ITEMS).child(Constant.PROTEIN).orderByChild("mItemId").equalTo(uItem.getmItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            reference.child(Constant.ITEMS).child(Constant.MOST_POPULAR).orderByChild("mItemId").equalTo(uItemList.get(position).getmItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                                            item.getRef().removeValue(new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                    ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.item_removed_from_most_popular_section));
                                                }
                                            });
                                        }
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

        holder.ProductCategoryMaterialIItemUpdateButton.setOnClickListener(new View.OnClickListener() {
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

        holder.ProductCategoryItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsInternetConnectivity.isConnected(context)) {
                    if (!uItemList.get(position).isPopular()) {
                        uItemList.get(position).setPopular(true);
                        holder.ProductCategoryItemCardView.setCardBackgroundColor(Color.parseColor("#90EE90"));
                        if (uItemList.get(position) != null) {
                            setTrueToItemInPopularInFirebase(position);
                            addItemToMostPopularInFirebaseDatabase(position);
                        }
                    } else {
                        uItemList.get(position).setPopular(false);
                        holder.ProductCategoryItemCardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        if (uItemList.get(position) != null) {
                            setFalseToItemInPopularInFirebase(position);
                            removeItemFromMostPopularInFirebaseDatabase(position);
                        }
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

    public class ProteinsAdapterViewHolder extends RecyclerView.ViewHolder {
        private CardView ProductCategoryItemCardView;
        private ImageView ProductCategoryItemImageView;
        private TextView ProductCategoryItemIdTextView, ProductCategoryItemCutOffPriceTextView, ProductCategoryItemPriceTextView
                , ProductCategoryItemNameTextView, ProductCategoryItemWeightTextView, ProductCategoryItemCategoryTextView,
                ProductCategoryItemCaloriesTextView,ProductCategoryItemFatTextView,ProductCategoryItemProteinTextView;
        private MaterialButton ProductCategoryMaterialItemDeleteButton,ProductCategoryMaterialIItemUpdateButton;

        public ProteinsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductCategoryItemCardView = itemView.findViewById(R.id.ProductCategoryItemCardView);
            ProductCategoryItemImageView = itemView.findViewById(R.id.ProductCategoryItemImageView);
            ProductCategoryItemIdTextView = itemView.findViewById(R.id.ProductCategoryItemIdTextView);
            ProductCategoryItemCutOffPriceTextView = itemView.findViewById(R.id.ProductCategoryItemCutOffPriceTextView);
            ProductCategoryItemPriceTextView = itemView.findViewById(R.id.ProductCategoryItemPriceTextView);
            ProductCategoryItemNameTextView = itemView.findViewById(R.id.ProductCategoryItemNameTextView);
            ProductCategoryItemWeightTextView = itemView.findViewById(R.id.ProductCategoryItemWeightTextView);
            ProductCategoryItemCategoryTextView = itemView.findViewById(R.id.ProductCategoryItemCategoryTextView);
            ProductCategoryMaterialItemDeleteButton = itemView.findViewById(R.id.ProductCategoryMaterialItemDeleteButton);
            ProductCategoryMaterialIItemUpdateButton = itemView.findViewById(R.id.ProductCategoryMaterialIItemUpdateButton);
            ProductCategoryItemCaloriesTextView = itemView.findViewById(R.id.ProductCategoryItemCaloriesTextView);
            ProductCategoryItemFatTextView = itemView.findViewById(R.id.ProductCategoryItemFatTextView);
            ProductCategoryItemProteinTextView = itemView.findViewById(R.id.ProductCategoryItemProteinTextView);
        }
    }

    public void setImageToGlide(String image_url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.market)
                .error(R.drawable.market);
        Glide.with(context).load(image_url).apply(options).apply(RequestOptions.centerInsideTransform()).into(imageView);
    }

    public void setTrueToItemInPopularInFirebase(int position) {
        reference.child(Constant.ITEMS).child(Constant.PROTEIN).orderByChild("mItemId").equalTo(uItemList.get(position).getmItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    item.getRef().setValue(uItemList.get(position));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setFalseToItemInPopularInFirebase(int position) {
        reference.child(Constant.ITEMS).child(Constant.PROTEIN).orderByChild("mItemId").equalTo(uItemList.get(position).getmItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    item.getRef().setValue(uItemList.get(position));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addItemToMostPopularInFirebaseDatabase(int position) {
        reference.child(ITEMS).child(Constant.MOST_POPULAR)
                .push()
                .setValue(uItemList.get(position)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    public void removeItemFromMostPopularInFirebaseDatabase(int position) {
        reference.child(Constant.ITEMS).child(Constant.MOST_POPULAR).orderByChild("mItemId").equalTo(uItemList.get(position).getmItemId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    item.getRef().removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            ShowSnackBar.snackBar(context, rootView, context.getResources().getString(R.string.item_removed_from_most_popular_section));
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
}
