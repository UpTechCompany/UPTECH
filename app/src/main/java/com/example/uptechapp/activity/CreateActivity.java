package com.example.uptechapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
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
import com.example.uptechapp.model.Emergency;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        init();

    }

    private void init() {
        BottomNavigationView nav = findViewById(R.id.bottomNavBar);


        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_feed:
                        Toast.makeText(CreateActivity.this, "Feed", Toast.LENGTH_SHORT).show();
//                        setFragment(new CategoryFragment());
                        Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_create:
                        Toast.makeText(CreateActivity.this, "Create", Toast.LENGTH_SHORT).show();
//                        setFragment(new LeaderBordFragment());
                        intent = new Intent(CreateActivity.this, CreateActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_map:
                        Toast.makeText(CreateActivity.this, "MAP", Toast.LENGTH_SHORT).show();
//                        setFragment(new AccountFragment());
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

        storageReference = FirebaseStorage.getInstance().getReference("EmergencyPictures");

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

            //Save image
            // TODO: change id

            int id = 1;

            StorageReference fileReference = storageReference.child(String.valueOf(id) + "/ProfilePictures." + getFileExtension(uriImage));
            //Toast.makeText(UploadProfilePictureActivity.this, fileReference.toString(), Toast.LENGTH_SHORT).show();

            //Upload image to Storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;

                            String url = downloadUri.toString();

                            Log.i(TAG, "URI - " + url);

                            Emergency emergency = new Emergency(
                                    "-1",
                                    emergencyLabel.getText().toString(),
                                    emergencyDescription.getText().toString(),
                                    Calendar.getInstance().getTime().toString(),
                                    url,
                                    "1,1"

                            );

                            Log.i(TAG, "MODEL - " + emergency.toString());

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

                    Toast.makeText(CreateActivity.this, "Emergency created", Toast.LENGTH_SHORT).show();

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
