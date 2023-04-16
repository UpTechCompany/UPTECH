package com.example.uptechapp.dao;

import android.os.Build;

import androidx.annotation.NonNull;

import com.example.uptechapp.api.CompleteListener;
import com.example.uptechapp.api.EmergencyApiService;
import com.example.uptechapp.model.Emergency;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Database {

    public static List<Emergency> EMERGENCIES_LIST = new ArrayList<>();

    public static void loadEmergencies (CompleteListener listener) {
        EMERGENCIES_LIST.clear();
        try {

//            // get data from server
//            for (int i=1; i < 11; i++) {
//                EMERGENCIES_LIST.add(new EmergencyModel(
//                        String.valueOf(i),
//                        "someone",
//                        "url",
//                        "title " + String.valueOf(i),
//                        "description " + String.valueOf(i),
//                        new Date(2023, 01, 23),
//                        new LatLng(56.130366 + (i), -106.346771 - (i))
//                ));
//            }

            EmergencyApiService.getInstance().getEmergency().enqueue(new Callback<List<Emergency>>() {
                @Override
                public void onResponse(@NonNull Call<List<Emergency>> call, @NonNull Response<List<Emergency>> response) {
                    for (int i = 0; i < response.body().size(); i++){
                        EMERGENCIES_LIST.add(response.body().get(i));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Emergency>> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });

            listener.OnSuccess();

        } catch (Exception e) {
            listener.OnFailure();
        }

    }

    public static Emergency getEmergencyByTitle(String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return EMERGENCIES_LIST.stream().filter(emergency -> emergency.getTitle().equals(title)).findAny()
                    .orElseThrow(() -> new RuntimeException("not found"));
        }
        else {
            for (Emergency emergency:
                    EMERGENCIES_LIST) {
                if(emergency.getTitle().equals(title)) {
                    return emergency;
                }
            }
        }
        return null;
    }

}
