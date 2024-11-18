package com.example.sabayhonorianapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sabayhonorianapp.view.HeroActivity;
import com.example.sabayhonorianapp.view.LoginActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.AppCheckProviderFactory;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.getInstance().setAutomaticResourceManagementEnabled(true);
        FirebaseApp.initializeApp(this);
        AppCheckProviderFactory providerFactory = PlayIntegrityAppCheckProviderFactory.getInstance();
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(providerFactory);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
           startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, HeroActivity.class));
        }


    }
}