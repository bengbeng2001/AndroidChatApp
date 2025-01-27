package com.example.webchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public FirebaseAuth auth;
    public RecyclerView mainUserRecyclerView;
    public UserAdapter adapter;
    public FirebaseDatabase database;
    public ArrayList<Users> usersArrayList;
    public ImageView imgLogout, updatebtn, deletebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFields();
        setupRecyclerView();
        fetchUsersFromDatabase();

        handleLogout();
        handleUpdateNavigation();
        checkAuthentication();

        deleteUserFromDatabase();
    }
    private void initializeFields() {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        updatebtn = findViewById(R.id.updateBtn);
        imgLogout = findViewById(R.id.logoutimg);
        deletebtn = findViewById(R.id.deleteBtn);

        usersArrayList = new ArrayList<>();
        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
    }
    private void deleteUserFromDatabase() {
        deletebtn.setOnClickListener(v -> {
            // Tampilkan dialog konfirmasi
            new AlertDialog.Builder(this)
                    .setTitle("Hapus Akun")
                    .setMessage("Anda yakin ingin menghapus akun INI? Ini akan MENGHAPUS SEMUA DATA dan ME-LOGOUT ANDA.")
                    .setPositiveButton("Hapus", (dialog, which) -> {
                        // Tampilkan progress dialog
                        ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Menghapus akun...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        // Dapatkan user saat ini
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                        if (currentUser != null) {
                            String uid = currentUser.getUid();

                            // Hapus data dari Realtime Database
                            DatabaseReference userRef = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("Users")
                                    .child(uid);

                            userRef.removeValue()
                                    .addOnCompleteListener(databaseTask -> {
                                        if (databaseTask.isSuccessful()) {
                                            // Hapus dari Authentication
                                            currentUser.delete()
                                                    .addOnCompleteListener(authTask -> {
                                                        progressDialog.dismiss();

                                                        if (authTask.isSuccessful()) {
                                                            // Hapus data tambahan jika perlu
                                                            deleteAdditionalUserData(uid);

                                                            // Navigasi ke layar login
                                                            navigateToLogin();

                                                            Toast.makeText(this,
                                                                    "Akun berhasil dihapus",
                                                                    Toast.LENGTH_SHORT).show();

                                                            finish();
                                                        } else {
                                                            handleDeleteError(authTask.getException());
                                                        }
                                                    });
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(this,
                                                    "Gagal menghapus data dari database",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(this,
                                    "Tidak ada user yang login",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });
    }
    private void deleteAdditionalUserData(String uid) {
        // Hapus data tambahan seperti pesan, kontak, dll
        DatabaseReference chatsRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("chats");

        // Hapus semua chat terkait user
        chatsRef.orderByChild("participants/" + uid)
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                            chatSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("DeleteUser", "Error deleting chats", error.toException());
                    }
                });

        // Hapus data tambahan lainnya sesuai kebutuhan
        // Misalnya: riwayat panggilan, status, dll
    }
    private void handleDeleteError(Exception exception) {
        // Tangani berbagai kemungkinan error
        if (exception instanceof FirebaseAuthRecentLoginRequiredException) {
            // User perlu login ulang sebelum menghapus akun
            Toast.makeText(this,
                    "Harap login ulang sebelum menghapus akun",
                    Toast.LENGTH_LONG).show();

            // Opsional: arahkan ke layar login
            FirebaseAuth.getInstance().signOut();
            navigateToLogin();
        } else if (exception instanceof FirebaseNetworkException) {
            Toast.makeText(this,
                    "Gagal menghapus akun. Periksa koneksi internet",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,
                    "Gagal menghapus akun: " + exception.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
    private void setupRecyclerView() {
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(MainActivity.this, usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);
    }
    private void fetchUsersFromDatabase() {
        DatabaseReference reference = database.getReference().child("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear(); // Clear list to avoid duplication
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user != null) {
                        usersArrayList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleLogout() {
        imgLogout.setOnClickListener(v -> {
            // Tampilkan dialog konfirmasi logout
            showLogoutConfirmationDialog();
        });
    }
    private void showLogoutConfirmationDialog() {
        // Gunakan AlertDialog dari AndroidX
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    // Proses logout
                    performLogout();
                })
                .setNegativeButton("Tidak", (dialog, which) -> {
                    // Tutup dialog
                    dialog.dismiss();
                })
                .create()
                .show();
    }
    private void performLogout() {
        // Tampilkan progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging out...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Proses logout
        auth.signOut();

        // Update status online di Firebase Realtime Database
        if (auth.getCurrentUser() != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(auth.getCurrentUser().getUid());

            userRef.child("status").setValue("offline")
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // Navigasi ke layar login
                            navigateToLogin();
                            finish();
                        } else {
                            // Tampilkan pesan error jika gagal
                            Toast.makeText(this,
                                    "Gagal memperbarui status",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Jika tidak ada user yang login
            progressDialog.dismiss();
            navigateToLogin();
            finish();
        }
    }
    private void handleUpdateNavigation() {
        updatebtn.setOnClickListener(v -> {
            Intent gotoSettings = new Intent(MainActivity.this, UpdateActivity.class);
            startActivity(gotoSettings);
        });
    }
    private void checkAuthentication() {
        if (auth.getCurrentUser() == null) {
            navigateToLogin();
        }
    }
    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
