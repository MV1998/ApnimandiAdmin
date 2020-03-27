package com.mohit.varma.apnimandiadmin.background;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.mohit.varma.apnimandiadmin.interfaces.Base64CallBack;

import java.io.ByteArrayOutputStream;

/**
 * At this time this class is not in use but might be use when needed.
 */

public class BackgroundServiceForGenerateBase64StringOfImage extends AsyncTask<Bitmap,Void,String> {
    public static final String TAG = BackgroundServiceForGenerateBase64StringOfImage.class.getSimpleName();
    private Context context;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteArray;
    private String imageString;
    private Base64CallBack base64CallBack;

    public BackgroundServiceForGenerateBase64StringOfImage(Context context,Base64CallBack base64CallBack) {
        Log.d(TAG, "BackgroundServiceForGenerateBase64StringOfImage: ");
        this.context = context;
        this.base64CallBack = base64CallBack;
        progressDialog = new ProgressDialog(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog();
    }

    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        if(bitmaps.length>0){
            bitmap = bitmaps[0];
            if(bitmap != null){
                imageString = getBase64ImageData(bitmap);
                Log.d(TAG, "BackgroundServiceForGenerateBase64StringOfImage: ");
                return imageString;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String base64String) {
        super.onPostExecute(base64String);
        if(base64String != null && !base64String.isEmpty()){
            Log.d(TAG, "onPostExecute: "+base64String);
            dismissProgressDialog();
            if( base64CallBack != null){
                Log.d(TAG, "onPostExecute: "+base64String);
                base64CallBack.base64String(base64String);
            }
        }
    }

    public void showProgressDialog(){
        if(progressDialog != null && !progressDialog.isShowing()){
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void dismissProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public String getBase64ImageData(Bitmap bitmap) {
        byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}