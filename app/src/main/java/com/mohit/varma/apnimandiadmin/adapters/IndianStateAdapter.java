package com.mohit.varma.apnimandiadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.activities.DistrictsActivity;
import com.mohit.varma.apnimandiadmin.interfaces.IAddDistrictCallBack;
import com.mohit.varma.apnimandiadmin.model.Districts;
import com.mohit.varma.apnimandiadmin.model.IndianState;
import com.mohit.varma.apnimandiadmin.utilities.Constant;
import com.mohit.varma.apnimandiadmin.utilities.ShowSnackBar;

import java.util.List;

public class IndianStateAdapter extends RecyclerView.Adapter<IndianStateAdapter.IndiaStateViewHolder> {
    private static final String TAG = "IndianStateAdapter";
    private Context context;
    private List<IndianState> indianStates;
    private List<Districts> districts;
    private IAddDistrictCallBack iAddDistrictCallBack;
    private DatabaseReference databaseReference;
    private String flag = "States";

    public IndianStateAdapter(Context context, List<IndianState> indianStates, IAddDistrictCallBack iAddDistrictCallBack, DatabaseReference databaseReference) {
        this.context = context;
        this.indianStates = indianStates;
        this.iAddDistrictCallBack = iAddDistrictCallBack;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public IndiaStateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.indian_state_single_view_layout,parent,false);
        return new IndiaStateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IndiaStateViewHolder holder, int position) {
        IndianState indianState = indianStates.get(position);
        holder.IndianStateSingleViewStateName.setText(String.format("State Name: %s",indianState.getStateName()));
        holder.IndianStateSingleViewStateCode.setText(String.format("State Code: %d",indianState.getStateCode()));
        holder.IndianStateSingleViewStateAbbr.setText(String.format("State Abbr. : %s",indianState.getStateAbbr()));
        districts = indianState.getDistrictsList();
        if (districts != null && districts.size()>0){
            int numberOfDistricts = districts.size();
            indianState.setNumberOfDistricts(numberOfDistricts);
            holder.IndianStateSingleViewNumberOfDistricts.setText(String.format("Total Districts: %d",numberOfDistricts));
        }else {
            holder.IndianStateSingleViewNumberOfDistricts.setText(String.format("Total Districts: %d",0));
        }

        holder.addDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iAddDistrictCallBack != null){
                    iAddDistrictCallBack.callBack(indianState);
                }
            }
        });

        holder.IndianStateSingleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + new Gson().toJson(indianState));
                Intent intent = new Intent(context, DistrictsActivity.class);
                intent.putExtra("state_date",indianState);
                context.startActivity(intent);
            }
        });

        holder.deleteState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(flag).orderByChild("stateCode").equalTo(indianState.getStateCode()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                item.getRef().removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        notifyDataSetChanged();
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
        return indianStates.size();
    }

    class IndiaStateViewHolder extends RecyclerView.ViewHolder{
        TextView IndianStateSingleViewStateName,IndianStateSingleViewStateCode,IndianStateSingleViewStateAbbr,IndianStateSingleViewNumberOfDistricts,
                deleteState,addDistrict;
        CardView IndianStateSingleView;
        public IndiaStateViewHolder(@NonNull View itemView) {
            super(itemView);
            IndianStateSingleViewStateName = itemView.findViewById(R.id.IndianStateSingleViewStateName);
            IndianStateSingleViewStateCode = itemView.findViewById(R.id.IndianStateSingleViewStateCode);
            IndianStateSingleViewStateAbbr = itemView.findViewById(R.id.IndianStateSingleViewStateAbbr);
            IndianStateSingleView = itemView.findViewById(R.id.IndianStateSingleView);
            IndianStateSingleViewNumberOfDistricts = itemView.findViewById(R.id.IndianStateSingleViewNumberOfDistricts);
            addDistrict = itemView.findViewById(R.id.addDistrict);
            deleteState = itemView.findViewById(R.id.deleteState);
        }
    }
}
