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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.mohit.varma.apnimandiadmin.R;
import com.mohit.varma.apnimandiadmin.background.BackgroundServiceForGenerateBase64StringOfImage;
import com.mohit.varma.apnimandiadmin.firebase.MyDatabaseReference;
import com.mohit.varma.apnimandiadmin.model.UItem;
import com.mohit.varma.apnimandiadmin.utilities.ShowSnackBar;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.mohit.varma.apnimandiadmin.utilities.Constant.IMAGE_MIME_TYPE;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.ITEMS;
import static com.mohit.varma.apnimandiadmin.utilities.Constant.ITEM_KEY;

public class AddVegetableActivity extends AppCompatActivity {
    public static final String TAG = AddFruitActivity.class.getSimpleName();
    private static final int RESULT_LOAD_IMAGE = 100;

    private Button AddVegetableActivityItemAddButton;
    private ImageView AddVegetableActivityItemImageView;
    private EditText AddVegetableActivityItemIdEditText, AddVegetableActivityItemCutOffPriceEditText,
            AddVegetableActivityItemPriceEditText, AddVegetableActivityItemNameEditText,
            AddVegetableActivityItemWeightEditText, AddVegetableActivityItemCategoryEditText;
    private View AddVegetableActivityRootView;

    private String imageString, itemId, itemCutOffPrice, itemPrice, itemName, itemWeight, itemCategory;
    private Context activity;
    private Bitmap bitmap;

    private MyDatabaseReference myDatabaseReference;
    private ProgressDialog progressDialog;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vegetable);

        initViews();

        if (getIntent().getStringExtra(ITEM_KEY) != null) {
            if (!getIntent().getStringExtra(ITEM_KEY).isEmpty()) {
                category = getIntent().getStringExtra(ITEM_KEY);
                Log.d(TAG, "onCreate: " + category);
            }
        }

        AddVegetableActivityItemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");

                itemId = AddVegetableActivityItemIdEditText.getText().toString();
                itemCutOffPrice = AddVegetableActivityItemCutOffPriceEditText.getText().toString();
                itemPrice = AddVegetableActivityItemPriceEditText.getText().toString();
                itemName = AddVegetableActivityItemNameEditText.getText().toString();
                itemWeight = AddVegetableActivityItemWeightEditText.getText().toString();
                itemCategory = AddVegetableActivityItemCategoryEditText.getText().toString();

                if (!itemId.isEmpty() && !itemCutOffPrice.isEmpty() && !itemPrice.isEmpty()
                        && !itemName.isEmpty() && !itemWeight.isEmpty() && !itemCategory.isEmpty()) {
                    Log.d(TAG, "onClick: ");
                    if (imageString != null && !imageString.isEmpty()) {
                        Log.d(TAG, "onClick: ");
                        addItemToDatabase();
                    } else {
                        ShowSnackBar.snackBar(activity, AddVegetableActivityRootView, activity.getResources().getString(R.string.please_select_image));
                    }
                } else {
                    ShowSnackBar.snackBar(activity, AddVegetableActivityRootView, activity.getResources().getString(R.string.all_fields_are_mandetory));
                }
            }
        });

        AddVegetableActivityItemImageView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType(IMAGE_MIME_TYPE);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    activity.getResources().getString(R.string.select_picture)), RESULT_LOAD_IMAGE);
        });
    }

    public void initViews() {
        AddVegetableActivityItemIdEditText = (EditText) findViewById(R.id.AddVegetableActivityItemIdEditText);
        AddVegetableActivityItemCutOffPriceEditText = (EditText) findViewById(R.id.AddVegetableActivityItemCutOffPriceEditText);
        AddVegetableActivityItemPriceEditText = (EditText) findViewById(R.id.AddVegetableActivityItemPriceEditText);
        AddVegetableActivityItemNameEditText = (EditText) findViewById(R.id.AddVegetableActivityItemNameEditText);
        AddVegetableActivityItemWeightEditText = (EditText) findViewById(R.id.AddVegetableActivityItemWeightEditText);
        AddVegetableActivityItemCategoryEditText = (EditText) findViewById(R.id.AddVegetableActivityItemCategoryEditText);
        AddVegetableActivityItemImageView = (ImageView) findViewById(R.id.AddVegetableActivityItemImageView);
        AddVegetableActivityItemAddButton = (Button) findViewById(R.id.AddVegetableActivityItemAddButton);
        AddVegetableActivityRootView = (View) findViewById(R.id.AddVegetableActivityRootView);

        //initialize instance
        activity = this;
        myDatabaseReference = new MyDatabaseReference();
        progressDialog = new ProgressDialog(activity);
    }

    public void addItemToDatabase() {
        showProgressDialog();
        UItem item = new UItem(Integer.parseInt(itemId),
                Integer.parseInt(itemCutOffPrice),
                Integer.parseInt(itemPrice),
                itemName,
                imageString,
                itemWeight,
                itemCategory);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                final Uri imageUri = data.getData();
                assert imageUri != null;
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);
                new BackgroundServiceForGenerateBase64StringOfImage(activity, base64String -> imageString = base64String).execute(bitmap);
                setImageToGlide(bitmap, AddVegetableActivityItemImageView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setImageToGlide(Bitmap bitmap, ImageView imageView) {
        Glide.with(activity).load(bitmap).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
