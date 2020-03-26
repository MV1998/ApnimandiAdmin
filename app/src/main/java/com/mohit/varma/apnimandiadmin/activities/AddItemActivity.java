package com.mohit.varma.apnimandiadmin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.model.UItem;
import com.mohit.varma.apnimandiadmin.utilities.ShowSnackBar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;

import static com.mohit.varma.apnimandiadmin.utilities.Constant.IMAGE_MIME_TYPE;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.ITEMS;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.ITEM_KEY;

public class AddItemActivity extends AppCompatActivity {
    public static final String TAG = AddItemActivity.class.getSimpleName();
    private static final int RESULT_LOAD_IMAGE = 100;
    private Button AddFruitsActivityItemAddButton;
    private ImageView AddFruitsActivityItemImageView;
    private EditText AddFruitsActivityItemIdEditText, AddFruitsActivityItemCutOffPriceEditText,
            AddFruitsActivityItemPriceEditText, AddFruitsActivityItemNameEditText,
            AddFruitsActivityItemWeightEditText, AddFruitsActivityItemCategoryEditText;
    private View AddFruitActivityRootView;
    private String imageURI = null, itemId, itemCutOffPrice, itemPrice, itemName, itemWeight, itemCategory;
    private Context activity;
    private Bitmap bitmap = null;
    private MyDatabaseReference myDatabaseReference;
    private ProgressDialog progressDialog;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        initViews();

        if (getIntent().getStringExtra(ITEM_KEY) != null) {
            if (!getIntent().getStringExtra(ITEM_KEY).isEmpty()) {
                category = getIntent().getStringExtra(ITEM_KEY);
            }
        }

        AddFruitsActivityItemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");

                itemId = AddFruitsActivityItemIdEditText.getText().toString();
                itemCutOffPrice = AddFruitsActivityItemCutOffPriceEditText.getText().toString();
                itemPrice = AddFruitsActivityItemPriceEditText.getText().toString();
                itemName = AddFruitsActivityItemNameEditText.getText().toString();
                itemWeight = AddFruitsActivityItemWeightEditText.getText().toString();
                itemCategory = AddFruitsActivityItemCategoryEditText.getText().toString();

                if (!itemId.isEmpty() && !itemCutOffPrice.isEmpty() && !itemPrice.isEmpty()
                        && !itemName.isEmpty() && !itemWeight.isEmpty() && !itemCategory.isEmpty()) {
                    Log.d(TAG, "onClick: ");
                    if (bitmap != null) {
                        uploadFile(bitmap);
                    } else {
                        ShowSnackBar.snackBar(activity, AddFruitActivityRootView, activity.getResources().getString(R.string.please_select_image));
                    }
                } else {
                    ShowSnackBar.snackBar(activity, AddFruitActivityRootView, activity.getResources().getString(R.string.all_fields_are_mandetory));
                }
            }
        });

        AddFruitsActivityItemImageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType(IMAGE_MIME_TYPE);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    activity.getResources().getString(R.string.select_picture)), RESULT_LOAD_IMAGE);
        });

    }

    public void initViews() {
        AddFruitsActivityItemIdEditText = findViewById(R.id.AddFruitsActivityItemIdEditText);
        AddFruitsActivityItemCutOffPriceEditText = findViewById(R.id.AddFruitsActivityItemCutOffPriceEditText);
        AddFruitsActivityItemPriceEditText = findViewById(R.id.AddFruitsActivityItemPriceEditText);
        AddFruitsActivityItemNameEditText = findViewById(R.id.AddFruitsActivityItemNameEditText);
        AddFruitsActivityItemWeightEditText = findViewById(R.id.AddFruitsActivityItemWeightEditText);
        AddFruitsActivityItemCategoryEditText = findViewById(R.id.AddFruitsActivityItemCategoryEditText);
        AddFruitsActivityItemImageView = findViewById(R.id.AddFruitsActivityItemImageView);
        AddFruitsActivityItemAddButton = findViewById(R.id.AddFruitsActivityItemAddButton);
        AddFruitActivityRootView = findViewById(R.id.AddFruitActivityRootView);

        //initialize instance
        activity = this;
        myDatabaseReference = new MyDatabaseReference();
        progressDialog = new ProgressDialog(activity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                Log.d("FruitsActivity", "onActivityResult: ");
                final Uri imageUri = data.getData();
                assert imageUri != null;
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);
                //new BackgroundServiceForGenerateBase64StringOfImage(activity, base64String -> imageString = base64String).execute(bitmap);
                setImageToGlide(bitmap, AddFruitsActivityItemImageView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void addItemToDatabase() {
        UItem item = new UItem(Integer.parseInt(itemId),
                Integer.parseInt(itemCutOffPrice),
                Integer.parseInt(itemPrice),
                itemName,
                imageURI,
                itemWeight,
                itemCategory,
                false);
        if (item != null) {
            Log.d(TAG, "addItemToDatabase: " + new Gson().toJson(item));
            myDatabaseReference.getReference().child(ITEMS).child(category)
                    .push()
                    .setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dismissProgressDialog();
                    onBackPressed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: ");
                }
            });
        }
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
    }

    public void setImageToGlide(Bitmap bitmap, ImageView imageView) {
        Glide.with(activity).load(bitmap).apply(RequestOptions.circleCropTransform()).into(imageView);
    }


    private void uploadFile(Bitmap bitmap) {
        showProgressDialog();
        StorageReference mountainImagesRef = myDatabaseReference.getStorageReference().child(category+"/" + new Timestamp(System.currentTimeMillis()).getTime() + ".jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                dismissProgressDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return mountainImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            if (downUri != null) {
                                imageURI = downUri.toString();
                                if (imageURI != null && !imageURI.isEmpty()) {
                                    addItemToDatabase();
                                }
                            }
                        }
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
            }
        });
    }
}