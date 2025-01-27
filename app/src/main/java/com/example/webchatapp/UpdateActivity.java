package com.example.webchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateActivity extends AppCompatActivity {
    private EditText setname, setstatus;
    private Button donebtn;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private String email, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Inisialisasi komponen
        initializeComponents();

        // Ambil data pengguna dari Firebase
        fetchUserData();

        // Setup listener untuk tombol selesai
        setupDoneButtonListener();
    }

    private void initializeComponents() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        setname = findViewById(R.id.settingName);
        setstatus = findViewById(R.id.settingStatus);
        donebtn = findViewById(R.id.doneBtn);

        // Konfigurasi progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan perubahan...");
        progressDialog.setCancelable(false);
    }

    private void fetchUserData() {
        DatabaseReference reference = database.getReference()
                .child("Users")
                .child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Ambil data pengguna
                email = snapshot.child("email").getValue(String.class);
                password = snapshot.child("password").getValue(String.class);
                String name = snapshot.child("username").getValue(String.class);
                String status = snapshot.child("status").getValue(String.class);

                // Set data ke EditText
                setname.setText(name);
                setstatus.setText(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateActivity.this,
                        "Gagal mengambil data",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDoneButtonListener() {
        donebtn.setOnClickListener(view -> {
            // Tampilkan progress dialog
            progressDialog.show();

            // Ambil data dari input
            String name = setname.getText().toString().trim();
            String status = setstatus.getText().toString().trim();

            // Validasi input
            if (name.isEmpty() || status.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(this, "Nama dan status tidak boleh kosong",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Update data pengguna
            updateUserProfile(name, status);
        });
    }

    private void updateUserProfile(String name, String status) {
        DatabaseReference reference = database.getReference()
                .child("Users")
                .child(auth.getUid());

        // Buat objek Users baru
        Users users = new Users(
                auth.getUid(),
                name,
                email,
                password,
                status
        );

        // Simpan data ke Firebase
        reference.setValue(users)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Profil berhasil diperbarui",
                                Toast.LENGTH_SHORT).show();

                        // Kembali ke MainActivity
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Gagal memperbarui profil",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}