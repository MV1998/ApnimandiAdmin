package com.mohit.varma.apnimandiadmin.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.model.Districts;

import java.util.List;
import java.util.Map;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder> {
    private Context context;
    private List<Districts> districts;
    private DatabaseReference databaseReference;
    private Map<Integer,String> wards;

    public DistrictAdapter(Context context, List<Districts> districts, DatabaseReference databaseReference) {
        this.context = context;
        this.districts = districts;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public DistrictViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.distirct_single_view_layout,parent,false);
        return new DistrictViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictViewHolder holder, int position) {
        Districts district = districts.get(position);
        holder.DistrictsActivityDistrictName.setText(String.format("District Name: %s",district.getDistrictName()));
        holder.DistrictsActivityDistrictCode.setText(String.format("District Code: %d",district.getDistrictPinCode()));
        holder.DistrictsActivityDistrictAbbr.setText(String.format("District Abbr. : %s",district.getDistrictAbbr()));
        wards = district.getWardMap();
        if (wards != null && wards.size()>0){
            int numberOfWards = wards.size();
            holder.DistrictsActivityNumberOfWard.setText(String.format("Total Ward: %d",numberOfWards));
        }else {
            holder.DistrictsActivityNumberOfWard.setText(String.format("Total Ward: %d",0));
        }

        holder.deleteDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = -1;
                for (int i = 0; i < districts.size(); i++){
                    if (districts.get(i).getDistrictPinCode() == district.getDistrictPinCode()){
                        index = i;
                    }
                }
                Log.d("Index", "onClick: " + index);
                if (index != -1){
                    districts.remove(index);
                }
            }
        });

//        holder.addDistrict.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (iAddDistrictCallBack != null){
//                    iAddDistrictCallBack.callBack(indianState);
//                }
//            }
//        });
//
//        holder.IndianStateSingleView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, DistrictsActivity.class);
//                intent.putExtra("state_date",indianState);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return districts.size();
    }

    class DistrictViewHolder extends RecyclerView.ViewHolder {
        TextView DistrictsActivityDistrictName,DistrictsActivityDistrictCode,DistrictsActivityDistrictAbbr,DistrictsActivityNumberOfWard,
                deleteDistrict,addWard;
        CardView DistrictsActivityCardView;
        public DistrictViewHolder(@NonNull View itemView) {
            super(itemView);
            DistrictsActivityDistrictName = itemView.findViewById(R.id.DistrictsActivityDistrictName);
            DistrictsActivityDistrictCode = itemView.findViewById(R.id.DistrictsActivityDistrictCode);
            DistrictsActivityDistrictAbbr = itemView.findViewById(R.id.DistrictsActivityDistrictAbbr);
            DistrictsActivityCardView = itemView.findViewById(R.id.DistrictsActivityCardView);
            DistrictsActivityNumberOfWard = itemView.findViewById(R.id.DistrictsActivityNumberOfWard);
            addWard = itemView.findViewById(R.id.addWard);
            deleteDistrict = itemView.findViewById(R.id.deleteDistrict);
        }
    }
}
