package com.mohit.varma.apnimandiadmin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.model.UItem;
import com.mohit.varma.apnimandiadmin.model.UItemDescription;
import com.mohit.varma.apnimandiadmin.utilities.ShowSnackBar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;

import static com.mohit.varma.apnimandiadmin.utilities.Constant.ITEMS;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.ITEM_KEY;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.generateUniqueId;

public class AddItemActivity extends AppCompatActivity {
    public static final String TAG = AddItemActivity.class.getSimpleName();
    private static final int RESULT_LOAD_IMAGE = 100;
    private TextInputEditText AddItemActivityItemIDEditText,AddItemActivityItemPriceEditText,AddItemActivityItemCutOffPriceEditText,
            AddItemActivityItemNameEditText,AddItemActivityItemWeightEditText,AddItemActivityItemCategoryEditText,
            AddItemActivityItemDescriptionEditText,AddItemActivityItemCaloriesEditText,AddItemActivityItemFatEditText,AddItemActivityItemProteinEditText;
    private MaterialButton AddItemActivityAddItemButton;
    private Toolbar AddItemActivityToolbar;
    private ImageView AddItemActivityItemImageView;
    private View AddItemActivityRootView;
    private Context activity;
    private Bitmap bitmap = null;
    private ProgressDialog progressDialog;
    private StorageReference mountainImagesRef;
    private MyDatabaseReference myDatabaseReference;
    private String imageURI = null, itemId, itemCutOffPrice, itemPrice, itemName, itemWeight, itemCategory, imageName = " ",
            itemDescription, itemCalories, itemFat, itemProtein,category;
    private UItem uItem = null;

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
        AddItemActivityItemIDEditText.setClickable(false);
        AddItemActivityItemIDEditText.setText("" + generateUniqueId());

        AddItemActivityAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemId = AddItemActivityItemIDEditText.getText().toString();
                itemCutOffPrice = AddItemActivityItemCutOffPriceEditText.getText().toString();
                itemPrice = AddItemActivityItemPriceEditText.getText().toString();
                itemName = AddItemActivityItemNameEditText.getText().toString();
                itemWeight = AddItemActivityItemWeightEditText.getText().toString();
                itemCategory = AddItemActivityItemCategoryEditText.getText().toString();
                itemDescription = AddItemActivityItemDescriptionEditText.getText().toString();
                itemCalories = AddItemActivityItemCaloriesEditText.getText().toString();
                itemFat = AddItemActivityItemFatEditText.getText().toString();
                itemProtein = AddItemActivityItemProteinEditText.getText().toString();

                if (!itemId.isEmpty() && !itemCutOffPrice.isEmpty() && !itemPrice.isEmpty()
                        && !itemName.isEmpty() && !itemWeight.isEmpty() && !itemCategory.isEmpty() && !itemDescription.isEmpty()
                        && !itemCalories.isEmpty() && !itemFat.isEmpty() && !itemProtein.isEmpty()) {
                    Log.d(TAG, "onClick: ");
                    if (bitmap != null) {
                        uploadFile(bitmap);
                    } else {
                        ShowSnackBar.snackBar(activity, AddItemActivityRootView, activity.getResources().getString(R.string.please_select_image));
                    }
                } else {
                    ShowSnackBar.snackBar(activity, AddItemActivityRootView, activity.getResources().getString(R.string.all_fields_are_mandetory));
                }
            }
        });

        AddItemActivityItemImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intent,
                    activity.getResources().getString(R.string.select_picture)), RESULT_LOAD_IMAGE);
        });

        AddItemActivityToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void initViews() {
        AddItemActivityItemImageView = findViewById(R.id.AddItemActivityItemImageView);
        AddItemActivityRootView = findViewById(R.id.AddItemActivityRootView);
        //Material EditText and Button initialized
        AddItemActivityItemIDEditText = (TextInputEditText) findViewById(R.id.AddItemActivityItemIDEditText);
        AddItemActivityItemPriceEditText = (TextInputEditText) findViewById(R.id.AddItemActivityItemPriceEditText);
        AddItemActivityItemCutOffPriceEditText = (TextInputEditText) findViewById(R.id.AddItemActivityItemCutOffPriceEditText);
        AddItemActivityItemNameEditText = (TextInputEditText) findViewById(R.id.AddItemActivityItemNameEditText);
        AddItemActivityItemWeightEditText = (TextInputEditText) findViewById(R.id.AddItemActivityItemWeightEditText);
        AddItemActivityItemCategoryEditText = (TextInputEditText) findViewById(R.id.AddItemActivityItemCategoryEditText);
        AddItemActivityItemDescriptionEditText = (TextInputEditText) findViewById(R.id.AddItemActivityItemDescriptionEditText);
        AddItemActivityItemCaloriesEditText = (TextInputEditText) findViewById(R.id.AddItemActivityItemCaloriesEditText);
        AddItemActivityItemFatEditText = (TextInputEditText) findViewById(R.id.AddItemActivityItemFatEditText);
        AddItemActivityItemProteinEditText = (TextInputEditText) findViewById(R.id.AddItemActivityItemProteinEditText);
        AddItemActivityAddItemButton = (MaterialButton) findViewById(R.id.AddItemActivityAddItemButton);
        AddItemActivityToolbar = (Toolbar) findViewById(R.id.AddItemActivityToolbar);
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
                final Uri imageUri = data.getData();
                assert imageUri != null;
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                imageName = getPath(activity, imageUri).substring(getPath(activity, imageUri).lastIndexOf("/") + 1);
                bitmap = BitmapFactory.decodeStream(imageStream);
                setImageToGlide(bitmap, AddItemActivityItemImageView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void addItemToDatabase() {
        UItemDescription uItemDescription = new UItemDescription(itemDescription,itemCalories,itemFat,itemProtein);
        if(uItemDescription!=null){
            uItem = new UItem(Integer.parseInt(itemId),
                    Integer.parseInt(itemCutOffPrice),
                    Integer.parseInt(itemPrice),
                    itemName,
                    imageURI,
                    itemWeight,
                    itemCategory,
                    false,uItemDescription);
            if (uItem != null) {
                Log.d(TAG, "addItemToDatabase: " + new Gson().toJson(uItem));
                myDatabaseReference.getReference().child(ITEMS).child(category)
                        .push()
                        .setValue(uItem).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        if (imageName != null && !imageName.isEmpty()) {
            mountainImagesRef = myDatabaseReference.getStorageReference().child(category + "/" + imageName);
        } else {
            mountainImagesRef = myDatabaseReference.getStorageReference().child(category + "/" + new Timestamp(System.currentTimeMillis()).getTime() + ".jpg");
        }
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

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }
}