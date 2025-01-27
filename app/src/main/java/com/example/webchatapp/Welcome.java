package com.example.webchatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.webchatapp.fragment.Page1;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Begin the fragment transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Create an instance of the Page1 fragment
        Page1 firstPageFragment = new Page1();

        // Replace the container with the fragment
        transaction.replace(R.id.fragment_container, firstPageFragment);

        // Commit the transaction
        transaction.commit();
    }
}
