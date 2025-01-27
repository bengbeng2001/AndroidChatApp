package com.example.webchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    Button btnLogReg, btnReg;
    EditText username, email, password, confirmationpassword;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        username = findViewById(R.id.usernameReg);
        email = findViewById(R.id.emailReg);
        password = findViewById(R.id.passwordReg);
        confirmationpassword = findViewById(R.id.passwordConfirm);

        btnReg = findViewById(R.id.btnReg);
        btnLogReg = findViewById(R.id.btnLogReg);

        btnLogReg.setOnClickListener(v -> {
            Intent gotoLog = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(gotoLog);
            finish();
        });

        btnReg.setOnClickListener(v -> RegistrationUser());
    }

    private void RegistrationUser() {
        String RegUser = username.getText().toString().trim();
        String Regemail = email.getText().toString().trim();
        String Regpassword = password.getText().toString().trim();
        String cPassword = confirmationpassword.getText().toString().trim();
        String status = "Hey I'm Using This Application";

        // Validasi input kosong
        if (Regemail.isEmpty() || Regpassword.isEmpty() || RegUser.isEmpty() || cPassword.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Semua kolom wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi format email
        if (!Regemail.matches(emailPattern)) {
            Toast.makeText(RegisterActivity.this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi panjang password
        if (Regpassword.length() < 6) {
            Toast.makeText(RegisterActivity.this, "Password harus terdiri dari minimal 6 karakter", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi kesesuaian password
        if (!Regpassword.equals(cPassword)) {
            Toast.makeText(RegisterActivity.this, "Password dan Konfirmasi Password tidak cocok", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat akun baru dengan Firebase Authentication
        auth.createUserWithEmailAndPassword(Regemail, Regpassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Simpan data pengguna ke Firebase Realtime Database
                saveUserToDatabase(RegUser, Regemail, Regpassword, status);
                Toast.makeText(RegisterActivity.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                Intent goToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(goToLogin);
                finish();
            } else {
                String errorMessage = task.getException().getMessage();
                Toast.makeText(RegisterActivity.this, "Registrasi gagal: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserToDatabase(String username, String email, String newpassword, String status) {
        String uid = auth.getCurrentUser().getUid(); // Dapatkan UID pengguna
        Users user = new Users(uid, username, email, newpassword, status); // Model untuk menyimpan data pengguna

        database.getReference().child("Users").child(uid).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RegisterActivity.this, "Data pengguna disimpan", Toast.LENGTH_SHORT).show();
                Log.d("firebase", String.valueOf(task.getResult()));
            } else {
                Toast.makeText(RegisterActivity.this, "Gagal menyimpan data pengguna", Toast.LENGTH_SHORT).show();
                Log.e("firebase", "Error getting data", task.getException());
            }
        });
    }
}
