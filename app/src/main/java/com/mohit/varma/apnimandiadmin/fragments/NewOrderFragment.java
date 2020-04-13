package com.mohit.varma.apnimandiadmin.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.adapters.NewOrderAdapter;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.interfaces.IPhoneCallBackPermission;
import com.mohit.varma.apnimandiadmin.model.Orders;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrderFragment extends Fragment {
    private static final String TAG = "NewOrderFragment";
    public static final int PHONE_CALL_PERMISSION_REQUEST_CODE = 100;
    private RecyclerView MyOrdersActivityRecyclerView;
    private DatabaseReference databaseReference;
    private Context context;
    private List<Orders> ordersList = new ArrayList<>();
    private NewOrderAdapter newOrderAdapter;

    public NewOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_order, container, false);
        if (ordersList != null) {
            ordersList.clear();
            ordersList = (List<Orders>) getArguments().getSerializable("newOrders");
            Log.d(TAG, "NewOrderFragment--->: " + new Gson().toJson(ordersList));
        }
        initViews(view);
        if (ordersList != null && ordersList.size() > 0) {
            if (newOrderAdapter != null) {
                newOrderAdapter.notifyDataSetChanged();
            } else {
                setAdapter();
            }
        }
        return view;
    }

    public void initViews(View view) {
        MyOrdersActivityRecyclerView = view.findViewById(R.id.MyOrdersActivityRecyclerView);
        this.context = getActivity();
        databaseReference = new MyDatabaseReference().getReference();
    }

    public void setAdapter() {
        if (ordersList != null && ordersList.size() > 0) {
            newOrderAdapter = new NewOrderAdapter(context, ordersList, databaseReference, new IPhoneCallBackPermission() {
                @Override
                public void checkPermissionAndProvideOrders(Orders orders) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+orders.getUserAddress().getPhoneNumber().substring(3,13)));
                            startActivity(intent);
                        }catch (Exception e){

                        }
                    } else {
                        if (getActivity() != null)
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_PERMISSION_REQUEST_CODE);
                    }
                }
            });
            MyOrdersActivityRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            MyOrdersActivityRecyclerView.setHasFixedSize(true);
            MyOrdersActivityRecyclerView.setAdapter(newOrderAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PHONE_CALL_PERMISSION_REQUEST_CODE == requestCode)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
    }
}
