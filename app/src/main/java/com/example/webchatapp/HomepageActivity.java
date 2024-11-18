package com.example.webchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {

    String nama,KEY_NAME = "NAMA";

    TextView namas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //masukkan kesini bundle tadi
        namas = (TextView) findViewById(R.id.welcomeText);
        Bundle extras = getIntent().getExtras();
        nama = extras.getString(KEY_NAME);
        namas.setText("Hello, " + nama);

        // Tombol Profile
        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Tombol Help
        Button helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, HelpActivity.class);
            startActivity(intent);
        });

        // Tombol About
        Button AboutButton = findViewById(R.id.aboutButton);
        AboutButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, AboutActivity.class);
            startActivity(intent);
        });


        // Tombol Help
        Button BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
