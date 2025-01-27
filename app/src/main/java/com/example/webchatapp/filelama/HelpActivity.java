//package com.example.webchatapp.lama;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.webchatapp.R;
//
//public class HelpActivity extends AppCompatActivity {
//
//    Button contactBtn;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_help);
//
//        // Inisialisasi tombol
//        contactBtn = findViewById(R.id.contact_support_button);
//
//        // Menambahkan aksi pada tombol untuk membuka aplikasi email
//        contactBtn.setOnClickListener(v -> {
//            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@webchatapp.com"));
//            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Bantuan Aplikasi WebChatApp");
//            emailIntent.putExtra(Intent.EXTRA_TEXT, "Isi pesan Anda di sini...");
//
//            // Memastikan ada aplikasi email yang tersedia
//            if (emailIntent.resolveActivity(getPackageManager()) != null) {
//                startActivity(emailIntent);
//            } else {
//                Toast.makeText(HelpActivity.this, "Tolong input yang benar", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Tombol Back untuk kembali ke Homepage
//        findViewById(R.id.BackButton).setOnClickListener(v ->
//                startActivity(new Intent(HelpActivity.this, HomepageActivity.class))
//        );
//    }
//}
