package com.example.uptechapp.api;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class ListenerLocation implements LocationListener {

        public static String longitude;
        public static String latitude;


        @Override
        public void onLocationChanged(Location loc) {
             longitude = String.valueOf(loc.getLongitude());
             latitude = String.valueOf(loc.getLatitude());
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
}

