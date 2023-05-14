package com.example.uptechapp.dao;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.uptechapp.R;
import com.example.uptechapp.api.CompleteListener;
import com.example.uptechapp.model.Emergency;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MapService implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    private static final String TAG = "MapService";
    private final Context context;

    public MapService(Context context) {
        this.context = context;
    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Toast.makeText(context, latLng.latitude + " "
                + latLng.longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        Toast.makeText(context, "LONG " + latLng.latitude + " "
                + latLng.longitude, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);

        Database.loadEmergencies(new CompleteListener() {
            @Override
            public void OnSuccess() {
                for (Emergency emergency: Database.EMERGENCIES_LIST) {
                    Log.d("TENSHI", Database.EMERGENCIES_LIST.toString());
                    googleMap.addMarker(new MarkerOptions().position(emergency.getLocation()).title(emergency.getTitle()));
                }

                googleMap.setOnMarkerClickListener(marker -> {
                    Emergency emergency = Database.getEmergencyByTitle(marker.getTitle());

                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_fragment);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setGravity(Gravity.BOTTOM);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    TextView tv_name = dialog.getWindow().findViewById(R.id.tv_name);
                    TextView tv_time = dialog.getWindow().findViewById(R.id.tv_time);
                    TextView tv_info = dialog.getWindow().findViewById(R.id.tv_description);

                    tv_name.setText(emergency.getTitle());
                    tv_info.setText(emergency.getDescription());
                    tv_time.setText(emergency.getTime().toString());


                    ImageView imageView = dialog.getWindow().findViewById(R.id.iv_image);
                    StorageReference reference = FirebaseStorage.getInstance().getReference(emergency.getPhotoUrl());
                    Glide.with(context).load(reference).into(imageView);
//                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            Glide.with(context).load(uri).into(imageView);
//                        }
//                    });

                    return false;
                });
            }

            @Override
            public void OnFailure() {
                Toast.makeText(context, "Try Later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
