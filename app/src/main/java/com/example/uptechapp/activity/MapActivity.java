package com.example.uptechapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.uptechapp.dao.MapService;
import com.example.uptechapp.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        init();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);

        mapFragment.getMapAsync(new MapService(this));
        MapService mapService = new MapService(this);
    }

    private void init() {
        BottomNavigationView nav = findViewById(R.id.bottomNavBar);


        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_feed:
                        Toast.makeText(MapActivity.this, "Feed", Toast.LENGTH_SHORT).show();
//                        setFragment(new CategoryFragment());
                        Intent intent = new Intent(MapActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_create:
                        Toast.makeText(MapActivity.this, "Create", Toast.LENGTH_SHORT).show();
//                        setFragment(new LeaderBordFragment());
                        intent = new Intent(MapActivity.this, CreateActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_map:
                        Toast.makeText(MapActivity.this, "MAP", Toast.LENGTH_SHORT).show();
//                        setFragment(new AccountFragment());
                        intent = new Intent(MapActivity.this, MapActivity.class);
                        startActivity(intent);

                        return true;
                }
                return false;
            }
        });

    }
}