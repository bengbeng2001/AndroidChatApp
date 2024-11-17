package com.example.webchatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button btnlogin;

    private String KEY_NAME = "NAMA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi Views
        btnlogin = findViewById(R.id.btnLogin);
        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);

        // Action untuk Button Login
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameLogin = username.getText().toString();
                String passwordLogin = password.getText().toString();

                if (!usernameLogin.isEmpty() && usernameLogin.equals("Annas")
                        && (!passwordLogin.isEmpty() && passwordLogin.equals("1234"))) {
                    Intent i = new Intent(MainActivity.this, HomepageActivity.class);
                    i.putExtra(KEY_NAME, usernameLogin); // Mengirimkan nama pengguna
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Username / Password Wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
