package com.example.uptechapp.dao;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.uptechapp.model.Emergency;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapService implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener{

    private static final String TAG = "MapService";
    private final Context context;
    private LocationManager locationManager;
    private final AppCompatActivity activity;
    private LifecycleOwner lifecycleOwner;
    private List<Emergency> myEmergencyList;



    public MapService(Context context, AppCompatActivity activity, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.activity = activity;
        this.lifecycleOwner = lifecycleOwner;
        myEmergencyList = MyViewModel.getInstance().getEmergencyLiveData().getValue();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
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
        Log.d(TAG, "onMapReady: READY");
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        List<Emergency> myEmergencyList = MyViewModel.getInstance().getEmergencyLiveData().getValue();
        Log.i("qq", "myEmergencyList" + myEmergencyList.toString());

        Log.d(TAG, "onMapReady: check before load emergencies");

        Log.i(TAG, "OnSuccess: " + myEmergencyList.toString());
        for (Emergency emergency: myEmergencyList) {
            emergency.setLocation(emergency.getLattitude(), emergency.getLongitude());
            Log.i(TAG, "emergency" + emergency.toString());
            googleMap.addMarker(new MarkerOptions().position(emergency.getLocation()).title(emergency.getTitle()));
            Log.d(TAG, "OnSuccess: add emergency");
        }

//        googleMap.setOnMarkerClickListener(marker -> {
//            Log.d(TAG, "OnSuccess: markerclicklistener");
//            Emergency emergency = Database.getEmergencyByTitle(marker.getTitle());
//
//            Dialog dialog = new Dialog(context);
//            dialog.setContentView(R.layout.dialog_fragment);
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().setGravity(Gravity.BOTTOM);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.show();
//
//            TextView tv_name = dialog.getWindow().findViewById(R.id.tv_name);
//            TextView tv_time = dialog.getWindow().findViewById(R.id.tv_time);
//            TextView tv_info = dialog.getWindow().findViewById(R.id.tv_description);
//
//            tv_name.setText(emergency.getTitle());
//            tv_info.setText(emergency.getDescription());
//            tv_time.setText(emergency.getTime().toString());
//
//
//            ImageView imageView = dialog.getWindow().findViewById(R.id.iv_image);
//            StorageReference reference = FirebaseStorage.getInstance().getReference(emergency.getPhotoUrl());
//            Glide.with(context).load(reference).into(imageView);
//
//            return false;
//        });
            final Observer<List<Emergency>> myObserver = new Observer<List<Emergency>>() {
            @Override
            public void onChanged(List<Emergency> emergencies) {
                Log.d("NIKITA", "INOF");
                Log.d("NIKITA", String.valueOf(emergencies.size()));
                myEmergencyList.clear();
                myEmergencyList.addAll(emergencies);
            }
        };
        MyViewModel.getInstance().getEmergencyLiveData().observe(lifecycleOwner, myObserver);
        Log.d(TAG, "onMapReady: proehali");
    }
}
