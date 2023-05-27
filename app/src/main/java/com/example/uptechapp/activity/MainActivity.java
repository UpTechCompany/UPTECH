package com.example.uptechapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uptechapp.dao.MyViewModel;
import com.example.uptechapp.api.CompleteListener;
import com.example.uptechapp.dao.Database;
import com.example.uptechapp.dao.EmergencyAdapter;
import com.example.uptechapp.R;
import com.example.uptechapp.model.Emergency;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView emergencyFeed;
    private Dialog progressBar;
    private TextView dialogText;

    List<Emergency> myEmergencyList;
    EmergencyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        emergencyFeed.setLayoutManager(layoutManager);

        myEmergencyList = new ArrayList<Emergency>();
        adapter = new EmergencyAdapter(myEmergencyList, getApplicationContext());

        emergencyFeed.setAdapter(adapter);

        Database.loadEmergencies(new CompleteListener() {
            @Override
            public void OnSuccess() {
                progressBar.dismiss();
            }

            @Override
            public void OnFailure() {
                progressBar.dismiss();

            }
        });

        final Observer<List<Emergency>> myObserver = new Observer<List<Emergency>>() {
            @Override
            public void onChanged(List<Emergency> emergencies) {
                Log.d("NIKITA", "INOF");
                Log.d("NIKITA", String.valueOf(emergencies.size()));
                myEmergencyList.clear();
                myEmergencyList.addAll(emergencies);
                adapter.notifyDataSetChanged();
            }
        };
        MyViewModel.getInstance().getEmergencyLiveData().observe(this, myObserver);
//        MyViewModel.getInstance().getEmergencyLiveData().getValue();

    }

    private void init() {

        BottomNavigationView nav = findViewById(R.id.bottomNavBar);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_feed:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_create:
                        intent = new Intent(MainActivity.this, CreateActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_map:
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