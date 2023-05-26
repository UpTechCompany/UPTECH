package com.example.uptechapp.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uptechapp.R;
import com.example.uptechapp.api.EmergencyApiService;
import com.example.uptechapp.api.ListenerLocation;
import com.example.uptechapp.api.MyLocationListener;
import com.example.uptechapp.dao.MapService;
import com.example.uptechapp.model.Emergency;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreatingActivity";
    private Button btnChoose, btnShare;
    private ImageView emergencyImg;
    private EditText emergencyLabel, emergencyDescription;
    private Uri uriImage;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    ActivityResultLauncher<String[]> locationPermissionRequest;
    public static Double longitude;
    public static Double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                Toast.makeText(this, "Precise location access granted", Toast.LENGTH_SHORT).show();
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                //
                                Toast.makeText(this, "Only approximate location access granted.", Toast.LENGTH_SHORT).show();
                            } else {
                                //
                                Toast.makeText(this, "No location access granted. Denied", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

        if (!checkLoc()) {
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }


        Log.d("NIKITAQ", "STARTED");
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkLoc();

        if (!checkLoc()) {
            return;
        }
        if (ActivityCompat.checkSelfPermission((Activity) this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((Activity) this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                });

        init();

    }

    boolean checkLoc(){
        return ActivityCompat.checkSelfPermission((Activity) this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission((Activity) this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static Double getLongitude() {
        return longitude;
    }

    public static Double getLatitude() {
        return latitude;
    }

    private void init() {
        BottomNavigationView nav = findViewById(R.id.bottomNavBar);


        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_feed:
                        Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_create:
                        intent = new Intent(CreateActivity.this, CreateActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_map:
                        intent = new Intent(CreateActivity.this, MapActivity.class);
                        startActivity(intent);

                        return true;
                }
                return false;
            }
        });

        btnChoose = findViewById(R.id.btnChoosePicture);
        btnShare = findViewById(R.id.btnShare);
        emergencyLabel = findViewById(R.id.editTextLabel);
        emergencyDescription = findViewById(R.id.editTextDescription);
        emergencyImg = findViewById(R.id.emergencyImg);

        storageReference = FirebaseStorage.getInstance().getReference("Emergency");

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareEmergency();
            }
        });

    }



    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uriImage = data.getData();
            emergencyImg.setImageURI(uriImage);
        } else {
            Toast.makeText(CreateActivity.this, "Try Later", Toast.LENGTH_SHORT).show();
        }
    }


    private void shareEmergency() {
        if (uriImage != null) {

            int id = 1;

            StorageReference fileReference = storageReference.child(String.valueOf(id) + "/Photo." + getFileExtension(uriImage));

            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;

                            String url = downloadUri.toString();
                            String[] time = Calendar.getInstance().getTime().toString().split(" ");
                            Log.i("time", "Time" + Arrays.toString(time));
                            Log.i(TAG, "onSuccess: " + MyLocationListener.getLongitude() + " " + MyLocationListener.getLatitude());

                            Emergency emergency = new Emergency(
                                    "-1",
                                    emergencyLabel.getText().toString(),
                                    emergencyDescription.getText().toString(),
                                    Calendar.getInstance().getTime().toString(),
                                    url,
                                    getLatitude(),
                                    getLongitude()
                            );

                            EmergencyApiService.getInstance().postJson(emergency).enqueue(new Callback<Emergency>() {
                                @Override
                                public void onResponse(@NonNull Call<Emergency> call, @NonNull Response<Emergency> response) {
                                    Log.i(TAG, "Response - " + call.toString());
                                }

                                @Override
                                public void onFailure(@NonNull Call<Emergency> call, @NonNull Throwable t) {
                                    Log.i(TAG, "FAIL - " + t.getMessage());
                                }
                            });

                        }
                    });

                    Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(CreateActivity.this, "File was not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uriImage) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uriImage));
    }
}
