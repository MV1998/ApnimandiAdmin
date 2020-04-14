package com.mohit.varma.apnimandiadmin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.adapters.OngoingOrderAdapter;
import com.mohit.varma.apnimandiadmin.adapters.PastOrderAdapter;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.model.Orders;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastOrderFragment extends Fragment {
    private static final String TAG = "PastOrderFragment";
    private RecyclerView MyOrdersActivityRecyclerView;
    private DatabaseReference databaseReference;
    private Context context;
    private List<Orders> ordersList = new ArrayList<>();
    private PastOrderAdapter pastOrderAdapter;
    private View rootView;

    public PastOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_order, container, false);
        if (ordersList != null) {
            ordersList.clear();
            ordersList = (List<Orders>) getArguments().getSerializable("pastOrder");
            Log.d(TAG, "pastOrder--->: " + new Gson().toJson(ordersList));
        }
        initViews(view);
        if (ordersList != null && ordersList.size() > 0) {
            if (pastOrderAdapter != null) {
                pastOrderAdapter.notifyDataSetChanged();
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
        rootView = view.findViewById(R.id.rootView);
    }

    public void setAdapter() {
        if (ordersList != null && ordersList.size() > 0) {
            pastOrderAdapter = new PastOrderAdapter(context, ordersList, databaseReference, rootView);
            MyOrdersActivityRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            MyOrdersActivityRecyclerView.setHasFixedSize(true);
            MyOrdersActivityRecyclerView.setAdapter(pastOrderAdapter);
        }
    }
}
