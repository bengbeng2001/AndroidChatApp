//package com.example.webchatapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.webchatapp.lama.EditProfileActivity;
//import com.example.webchatapp.lama.HomepageActivity;
//
//public class ProfileActivity extends AppCompatActivity {
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        Button editProfileButton = findViewById(R.id.edit_profile_button);
//        editProfileButton.setOnClickListener(v -> {
//            Intent editProfileIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
//            startActivity(editProfileIntent);
//        });
//
//
//        findViewById(R.id.BackButton).setOnClickListener(v ->
//                startActivity(new Intent(ProfileActivity.this, HomepageActivity.class))
//        );
//    }
//}
