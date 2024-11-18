package com.example.sabayhonorianapp.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.sabayhonorianapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HeroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hero);

        BottomNavigationView bnvHero = findViewById(R.id.bnvHero);
        NavHostFragment nvfHero = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nfvHero);
        if(nvfHero != null){
            NavigationUI.setupWithNavController(bnvHero, nvfHero.getNavController());
        }

    }
}