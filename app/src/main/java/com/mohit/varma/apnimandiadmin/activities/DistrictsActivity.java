package com.mohit.varma.apnimandiadmin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.adapters.DistrictAdapter;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.model.Districts;
import com.mohit.varma.apnimandiadmin.model.IndianState;

import java.util.List;

public class DistrictsActivity extends AppCompatActivity {
    private static final String TAG = "DistrictsActivity";
    private View DistrictActivityRootView;
    private Toolbar DistrictActivityToolbar;
    private RecyclerView DistrictActivityRecyclerView;
    private TextView DistrictActivityNoItemAddedYetTextView;
    private List<Districts> districtsList;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private DistrictAdapter districtAdapter;
    private Context activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_districts);
        initViews();


        setSupportActionBar(DistrictActivityToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        DistrictActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        IndianState indianState = (IndianState) getIntent().getSerializableExtra("state_date");
        if (indianState != null) {
            districtsList = indianState.getDistrictsList();
            if (districtsList != null && districtsList.size() > 0) {
                if (districtAdapter != null) {
                    DistrictActivityRecyclerView.setVisibility(View.VISIBLE);
                    DistrictActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                    districtAdapter.notifyDataSetChanged();
                } else {
                    DistrictActivityRecyclerView.setVisibility(View.VISIBLE);
                    DistrictActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                    setAdapter();
                }
            } else {
                DistrictActivityRecyclerView.setVisibility(View.GONE);
                DistrictActivityNoItemAddedYetTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void initViews() {
        DistrictActivityToolbar = findViewById(R.id.DistrictActivityToolbar);
        DistrictActivityToolbar = findViewById(R.id.DistrictActivityToolbar);
        DistrictActivityNoItemAddedYetTextView = findViewById(R.id.DistrictActivityNoItemAddedYetTextView);
        DistrictActivityRecyclerView = findViewById(R.id.DistrictActivityRecyclerView);
        DistrictActivityRootView = findViewById(R.id.DistrictActivityRootView);
        this.activity = this;
        databaseReference = new MyDatabaseReference().getReference();
        progressDialog = new ProgressDialog(activity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void setAdapter() {
        DistrictActivityToolbar.setTitle("Total District " + districtsList.size());
        DistrictActivityRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        DistrictActivityRecyclerView.setHasFixedSize(true);
        districtAdapter = new DistrictAdapter(activity, districtsList,databaseReference);
        DistrictActivityRecyclerView.setAdapter(districtAdapter);
    }
}
