package com.mohit.varma.apnimandiadmin.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mohit.varma.apnimandiadmin.R;

import java.util.Objects;
import java.util.function.Consumer;

public class UsersActivity extends AppCompatActivity {
    private Toolbar usersActivityToolbar;
    private Context activity;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        //initialization of views
        initViews();


        toolbarConsumer.accept(usersActivityToolbar);

    }

    public void initViews(){
        usersActivityToolbar = (Toolbar) findViewById(R.id.usersActivityToolbar);
        activity = this;
    }

    Consumer<Toolbar> toolbarConsumer = usersActivityToolbar ->{
        if(usersActivityToolbar != null && activity != null){
            setSupportActionBar(usersActivityToolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            usersActivityToolbar.setTitle(activity.getResources().getString(R.string.user));
        }
    };


}
