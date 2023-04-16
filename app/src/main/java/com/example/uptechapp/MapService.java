package com.example.uptechapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

                    imageView.setImageResource(R.drawable.gosuslugi);

//                        FirebaseStorage firebaseStorage =
//                                FirebaseStorage.getInstance("gs://mymaps-b35de.appspot.com");
//                        StorageReference reference = firebaseStorage.getReference(place.getPathToImage());
//                        Glide.with(context).load(reference).into(imageView);

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
