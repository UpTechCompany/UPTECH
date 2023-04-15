package com.example.uptechapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database {

    public static List<EmergencyModel> EMERGENCIES_LIST = new ArrayList<>();

    public static void loadEmergencies (CompleteListener listener) {
        EMERGENCIES_LIST.clear();
        try {

            // get data from server
            for (int i=1; i < 11; i++) {
                EMERGENCIES_LIST.add(new EmergencyModel(
                        String.valueOf(i),
                        "someone",
                        "url",
                        "title " + String.valueOf(i),
                        "description",
                        new Date(2023, 01, 23),
                        new LatLng(56, 56)
                ));
            }

            listener.OnSuccess();

        } catch (Exception e) {
            listener.OnFailure();
        }

    }
}
