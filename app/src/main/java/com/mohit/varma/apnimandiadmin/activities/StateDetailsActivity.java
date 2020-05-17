package com.mohit.varma.apnimandiadmin.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.adapters.IndianStateAdapter;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.interfaces.IAddDistrictCallBack;
import com.mohit.varma.apnimandiadmin.model.Districts;
import com.mohit.varma.apnimandiadmin.model.IndianState;
import com.mohit.varma.apnimandiadmin.utilities.IsInternetConnectivity;
import com.mohit.varma.apnimandiadmin.utilities.ShowSnackBar;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class StateDetailsActivity extends AppCompatActivity {
    public final String TAG = "StateDetailsActivity";
    private Toolbar StateDetailsActivityToolbar;
    private RecyclerView StateDetailsActivityRecyclerView;
    private FloatingActionButton StateDetailsActivityFab;
    private TextView StateDetailsActivityNoItemAddedYetTextView;
    private View StateDetailsActivityRootView, alertView;
    private TextInputEditText StateDetailsCardViewStateName, StateDetailsCardViewStateCode, StateDetailsCardViewStateAbbr;
    private TextInputEditText DistrictCardViewDistrictName, DistrictCardViewDistrictPinCode, DistrictCardViewDistrictAbbr;
    private ImageView dialogCancelButton;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private AlertDialog updateAlertDialog;
    private Context activity;
    private Intent intent;
    private List<IndianState> indianStates = new LinkedList<>();
    private IndianStateAdapter indianStateAdapter;
    private String flag = "States";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_details);

        initViews();

        setSupportActionBar(StateDetailsActivityToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        StateDetailsActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        StateDetailsActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsInternetConnectivity.isConnected(activity)) {
                    showUpdateDialog();
                } else {
                    ShowSnackBar.snackBar(activity, StateDetailsActivityRootView, activity.getResources().getString(R.string.please_check_internet_connectivity));
                }
            }
        });


        databaseReference.child(flag).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    indianStates.clear();
                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                        for (DataSnapshot states : dataSnapshot.getChildren()) {
                            IndianState indianState = states.getValue(IndianState.class);
                            indianStates.add(indianState);
                            Log.d(TAG, "onCreate: " + indianStates.size());
                        }
                        if (indianStates != null && indianStates.size() > 0) {
                            StateDetailsActivityToolbar.setTitle("Total States " + indianStates.size());
                            if (indianStateAdapter != null) {
                                dismissProgressDialog();
                                StateDetailsActivityRecyclerView.setVisibility(View.VISIBLE);
                                StateDetailsActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                indianStateAdapter.notifyDataSetChanged();
                            } else {
                                dismissProgressDialog();
                                StateDetailsActivityRecyclerView.setVisibility(View.VISIBLE);
                                StateDetailsActivityNoItemAddedYetTextView.setVisibility(View.GONE);
                                //in setadapter
                                setAdapter();
                            }
                        }
                    } else {
                        StateDetailsActivityRecyclerView.setVisibility(View.GONE);
                        StateDetailsActivityNoItemAddedYetTextView.setVisibility(View.VISIBLE);
                        dismissProgressDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //**************************************************************

    }

    public void initViews() {
        StateDetailsActivityToolbar = findViewById(R.id.StateDetailsActivityToolbar);
        StateDetailsActivityRecyclerView = findViewById(R.id.StateDetailsActivityRecyclerView);
        StateDetailsActivityFab = findViewById(R.id.StateDetailsActivityFab);
        StateDetailsActivityNoItemAddedYetTextView = findViewById(R.id.StateDetailsActivityNoItemAddedYetTextView);
        StateDetailsActivityRootView = findViewById(R.id.StateDetailsActivityRootView);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        if (updateAlertDialog != null) {
            if (updateAlertDialog.isShowing()) {
                updateAlertDialog.show();
            }
        }
    }

    public void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        alertView = LayoutInflater.from(activity).inflate(R.layout.add_state_details_dialog_layout, null, false);
        builder.setView(alertView);
        StateDetailsCardViewStateName = alertView.findViewById(R.id.StateDetailsCardViewStateName);
        StateDetailsCardViewStateCode = alertView.findViewById(R.id.StateDetailsCardViewStateCode);
        StateDetailsCardViewStateAbbr = alertView.findViewById(R.id.StateDetailsCardViewStateAbbr);
        dialogCancelButton = alertView.findViewById(R.id.dialogCancelButton);
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissUpdateAlertDialog();
            }
        });
        MaterialButton addButton = alertView.findViewById(R.id.StateDetailsCardViewAddStateButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(StateDetailsActivityRootView.getWindowToken(), 0);
                }
                addStateToDataBase();
            }
        });
        updateAlertDialog = builder.create();
        updateAlertDialog.show();
    }

    public void dismissUpdateAlertDialog() {
        if (updateAlertDialog != null && updateAlertDialog.isShowing()) {
            updateAlertDialog.dismiss();
        }
    }

    public void addStateToDataBase() {
        String stateName = StateDetailsCardViewStateName.getText().toString();
        String stateCode = StateDetailsCardViewStateCode.getText().toString();
        String stateAbbr = StateDetailsCardViewStateAbbr.getText().toString();
        if (indianStates != null && indianStates.size()>0){
            for (int i = 0; i < indianStates.size(); i++){
                if (indianStates.get(i).getStateCode() == Integer.parseInt(stateCode)){
                    ShowSnackBar.snackBar(activity, StateDetailsActivityRootView, activity.getResources().getString(R.string.state_code_already_used));
                    dismissUpdateAlertDialog();
                    return;
                }
            }
        }
        if (IsInternetConnectivity.isConnected(activity)) {
            if (stateName != null && !stateName.isEmpty() && stateCode != null && !stateCode.isEmpty() &&
                    stateAbbr != null && !stateAbbr.isEmpty()) {
                dismissUpdateAlertDialog();
                showProgressDialog();
                IndianState indianState = new IndianState();
                indianState.setStateName(stateName);
                indianState.setStateCode(Integer.parseInt(stateCode));
                indianState.setStateAbbr(stateAbbr.toUpperCase());
                if (indianState != null) {
                    databaseReference.child(flag)
                            .push()
                            .setValue(indianState).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dismissProgressDialog();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: ");
                        }
                    });
                }
            } else {
                ShowSnackBar.snackBar(activity, StateDetailsActivityRootView, activity.getResources().getString(R.string.all_fields_are_mandetory));
            }
        } else {
            ShowSnackBar.snackBar(activity, StateDetailsActivityRootView, activity.getResources().getString(R.string.please_check_internet_connectivity));
        }
    }

    public void setAdapter() {
        if (indianStates != null && indianStates.size() > 0) {
            Log.d(TAG, "setAdapter: " + new Gson().toJson(indianStates));
            StateDetailsActivityRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            StateDetailsActivityRecyclerView.setHasFixedSize(true);
            indianStateAdapter = new IndianStateAdapter(activity, indianStates, new IAddDistrictCallBack() {
                @Override
                public void callBack(IndianState indianState) {
                    Log.d(TAG, "callBack: " + new Gson().toJson(indianState));
                    showDialogForAddDistrict(indianState);
                }
            },databaseReference);
            StateDetailsActivityRecyclerView.setAdapter(indianStateAdapter);
        }
    }

    public void showDialogForAddDistrict(IndianState indianState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        alertView = LayoutInflater.from(activity).inflate(R.layout.add_district_dialog_layout, null, false);
        builder.setView(alertView);
        DistrictCardViewDistrictName = alertView.findViewById(R.id.DistrictCardViewDistrictName);
        DistrictCardViewDistrictPinCode = alertView.findViewById(R.id.DistrictCardViewDistrictPinCode);
        DistrictCardViewDistrictAbbr = alertView.findViewById(R.id.DistrictCardViewDistrictAbbr);
        dialogCancelButton = alertView.findViewById(R.id.dialogCancelButton);
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissUpdateAlertDialog();
            }
        });
        MaterialButton addButton = alertView.findViewById(R.id.DistrictCardViewDistrictAbbrAddDistrict);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(StateDetailsActivityRootView.getWindowToken(), 0);
                }
                addDistrictToState(indianState);
            }
        });
        updateAlertDialog = builder.create();
        updateAlertDialog.show();
    }


    public void addDistrictToState(IndianState indianState) {
        String districtName = DistrictCardViewDistrictName.getText().toString();
        String districtPinCode = DistrictCardViewDistrictPinCode.getText().toString();
        String districtAbbr = DistrictCardViewDistrictAbbr.getText().toString();
        if (indianState != null){
            if (indianState.getDistrictsList() != null && indianState.getDistrictsList().size()>0){
                for (int i = 0; i < indianStates.size(); i++){
                    if (indianState.getDistrictsList().get(i).getDistrictPinCode() == Integer.parseInt(districtPinCode)){
                        ShowSnackBar.snackBar(activity, StateDetailsActivityRootView, activity.getResources().getString(R.string.district_already_exists));
                        dismissUpdateAlertDialog();
                        return;
                    }
                }
            }
        }
        if (IsInternetConnectivity.isConnected(activity)) {
            if (districtName != null && !districtName.isEmpty() && districtPinCode != null && !districtPinCode.isEmpty() &&
                    districtAbbr != null && !districtAbbr.isEmpty()) {
                dismissUpdateAlertDialog();
                showProgressDialog();
                Districts districts = new Districts();
                districts.setDistrictName(districtName);
                districts.setDistrictPinCode(Integer.parseInt(districtPinCode));
                districts.setDistrictAbbr(districtAbbr);

                List<Districts> districtsList = indianState.getDistrictsList();
                if (districtsList != null && districtsList.size() > 0) {
                    districtsList.add(districts);
                    indianState.setDistrictsList(districtsList);
                } else {
                    districtsList = new LinkedList<>();
                    districtsList.add(districts);
                    indianState.setDistrictsList(districtsList);
                }

                if (indianState != null) {
                    databaseReference.child(flag).orderByChild("stateCode").equalTo(indianState.getStateCode()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                item.getRef().setValue(indianState).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dismissProgressDialog();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dismissProgressDialog();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            } else {
                ShowSnackBar.snackBar(activity, StateDetailsActivityRootView, activity.getResources().getString(R.string.all_fields_are_mandetory));
            }
        } else {
            ShowSnackBar.snackBar(activity, StateDetailsActivityRootView, activity.getResources().getString(R.string.please_check_internet_connectivity));
        }
    }
    public static class SecretObject implements Serializable {

        private static final long serialVersionUID = -1335351770906357695L;

        private final String message;

        public SecretObject(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "SecretObject [message=" + message + "]";
        }

    }

    public void encryptAndDecryptData(){
        Cipher ecipher;
        Cipher dcipher;

        SecretKey key;

        // generate secret key using DES algorithm
        try {
            key = KeyGenerator.getInstance("DES").generateKey();

            ecipher = Cipher.getInstance("DES");
            dcipher = Cipher.getInstance("DES");

            // initialize the ciphers with the given key

            ecipher.init(Cipher.ENCRYPT_MODE, key);

            dcipher.init(Cipher.DECRYPT_MODE, key);

            // create a sealed object

//            SealedObject sealed = new SealedObject(new SecretObject("My secret message"), ecipher);
            Log.d(TAG, "onCreate: " + new Gson().toJson(indianStates));
            Log.d(TAG, "onCreate: " + indianStates.size());
            IndianState indianState = indianStates.get(0);
            Log.d(TAG, "onCreate: " + new Gson().toJson(indianState));
            SealedObject indianStateSealedObject = new SealedObject(indianState,ecipher);

            Log.d(TAG, "encrypt IndianState " + new Gson().toJson(indianStateSealedObject));
            indianState = (IndianState) indianStateSealedObject.getObject(dcipher);
            Log.d(TAG, "Decrypt IndianState " + new Gson().toJson(indianState));
            // get the algorithm with the object has been sealed

//            String algorithm = sealed.getAlgorithm();
//
//            Log.d(TAG, "Algorithm: " + algorithm);
//
//            // unseal (decrypt) the object
//
//            SecretObject o = (SecretObject) sealed.getObject(dcipher);
//
//            Log.d(TAG, "Original Object: " + o + new Gson().toJson(sealed));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
