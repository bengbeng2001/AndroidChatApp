//package com.example.webchatapp.lama;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.webchatapp.R;
//
//public class AboutActivity extends AppCompatActivity {
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_about);
//
//        findViewById(R.id.facebook_button).setOnClickListener(v -> {
//            String facebookUrl = "https://www.facebook.com/AnnasNuur"; // Ganti dengan URL Facebook Anda
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
//            startActivity(intent);
//        });
//
//
//        findViewById(R.id.BackButton).setOnClickListener(v ->
//                startActivity(new Intent(AboutActivity.this, HomepageActivity.class))
//        );
//    }
//}
