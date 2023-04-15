package com.example.uptechapp;

import android.os.Build;

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
                        "description " + String.valueOf(i),
                        new Date(2023, 01, 23),
                        new LatLng(56.130366 + (i), -106.346771 - (i))
                ));
            }

            listener.OnSuccess();

        } catch (Exception e) {
            listener.OnFailure();
        }

    }

    public static EmergencyModel getEmergencyByTitle(String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return EMERGENCIES_LIST.stream().filter(emergency -> emergency.getTitle().equals(title)).findAny()
                    .orElseThrow(() -> new RuntimeException("not found"));
        }
        else {
            for (EmergencyModel emergency:
                    EMERGENCIES_LIST) {
                if(emergency.getTitle().equals(title)) {
                    return emergency;
                }
            }
        }
        return null;
    }

}
