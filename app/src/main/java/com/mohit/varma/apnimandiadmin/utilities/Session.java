package com.mohit.varma.apnimandiadmin.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private static final String TAG = "Session";
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Session(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(TAG, 0);
        editor = sharedPreferences.edit();
    }


    //save delivery charge
    public static final String DELIVERY_CHARGE = "delivery_charge";

    public void setDeliveryCharge(long deliveryCharge){
        editor.putLong(DELIVERY_CHARGE,deliveryCharge);
        editor.commit();
        editor.apply();
    }

    public long getDeliveryCharge(){
        return sharedPreferences.getLong(DELIVERY_CHARGE,0);
    }

}