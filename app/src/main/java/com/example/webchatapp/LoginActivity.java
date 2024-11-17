package com.example.webchatapp;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button btnlogin, btnNext;

    private String KEY_NAME = "NAMA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.usernameLogin);  // Inisialisasi EditText untuk username
        password = findViewById(R.id.passwordLogin);  // Inisialisasi EditText untuk password
        btnlogin = findViewById(R.id.btnLogin); // Inisialisasi tombol login
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernameInput = username.getText().toString();
                String passwordInput = password.getText().toString();

                // Validasi login (misalnya, cek dengan username dan password yang sudah diset
                if (isValidLogin(usernameInput, passwordInput)) {
                    Toast.makeText(LoginActivity.this, "Selamat Datang Annas", Toast.LENGTH_SHORT).show();
                    Intent pindah = new Intent(LoginActivity.this, HomepageActivity.class); //Membuat Intent
                    pindah.putExtra(KEY_NAME, usernameInput);
                    startActivity(pindah);
                } else {
                    // Tampilkan pesan error jika login gagal
                    Toast.makeText(LoginActivity.this, "Username / Password Wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean isValidLogin(String username, String password) {
        return username.equals("Annas") && password.equals("1234");
    }

}
