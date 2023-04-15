package com.example.uptechapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenu;

public class MainActivity extends AppCompatActivity {

    private RecyclerView emergencyFeed;
    private Dialog progressBar;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        emergencyFeed.setLayoutManager(layoutManager);

        findViewById(R.id.showMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        Database.loadEmergencies(new CompleteListener() {
            @Override
            public void OnSuccess() {
                EmergencyAdapter adapter = new EmergencyAdapter(Database.EMERGENCIES_LIST);
                emergencyFeed.setAdapter(adapter);

                progressBar.dismiss();
            }

            @Override
            public void OnFailure() {
                progressBar.dismiss();

            }
        });

    }

    private void init() {

        BottomNavigationView nav = findViewById(R.id.bottomNavBar);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_feed:
                        Toast.makeText(MainActivity.this, "Feed", Toast.LENGTH_SHORT).show();
//                        setFragment(new CategoryFragment());
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_create:
                        Toast.makeText(MainActivity.this, "Create", Toast.LENGTH_SHORT).show();
//                        setFragment(new LeaderBordFragment());
                        intent = new Intent(MainActivity.this, CreateActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_map:
                        Toast.makeText(MainActivity.this, "MAP", Toast.LENGTH_SHORT).show();
//                        setFragment(new AccountFragment());
                        intent = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(intent);

                        return true;
                }
                return false;
            }
        });

        emergencyFeed = findViewById(R.id.emergencyFeed);

        progressBar = new Dialog(MainActivity.this);
        progressBar.setContentView(R.layout.dialog_layout);
        progressBar.setCancelable(false);
        progressBar.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressBar.findViewById(R.id.dialogText);
        dialogText.setText("Loading");

        progressBar.show();
    }

}