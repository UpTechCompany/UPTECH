package com.example.uptechapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EmergencyApi {
    @GET("emergency")
    Call<List<Emergency>> getEmergency();

    @GET("emergency/{id}")
    Call<Emergency> getEmergency(
            @Path("id")
            long id
    );

    @DELETE("emergency/{id}")
    Call<Void> deleteEmergency(
            @Path("id")
            long id
    );
}
