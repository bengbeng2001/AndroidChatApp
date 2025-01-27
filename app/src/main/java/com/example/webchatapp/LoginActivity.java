package com.example.webchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.webchatapp.lama.HomepageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText email, password;
    Button btnlogin, btnRegLog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailLog);
        password = findViewById(R.id.passwordLog);

        btnlogin = findViewById(R.id.btnLogin);
        btnRegLog = findViewById(R.id.btnRegLog);

        btnRegLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToReg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(goToReg);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }
    private void LoginUser(){
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();

        // Validasi input kosong
        if (Email.isEmpty() || Password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi format email
        if (!Email.matches(emailPattern)) {
            Toast.makeText(LoginActivity.this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proses login dengan Firebase Authentication
        auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    try {
                        Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                        Intent gotoMain = new Intent(LoginActivity.this, MainActivity.class); // Membuat Intent
                        startActivity(gotoMain);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();}
                } else {
                    Toast.makeText(LoginActivity.this, "Login gagal. Tolong coba lagi", Toast.LENGTH_SHORT).show();}
            }
        });
    }
}
