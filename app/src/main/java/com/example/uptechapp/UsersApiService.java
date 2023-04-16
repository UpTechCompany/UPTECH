package com.example.uptechapp;

import com.example.uptechapp.RetrofitService;
import com.example.uptechapp.UsersApi;

public class UsersApiService {

    private static UsersApi usersApi;

    private static UsersApi create() {
        return RetrofitService.getInstance().create(UsersApi.class);
    }

    public static UsersApi getInstance() {
        if (usersApi == null) usersApi = create();
        return usersApi;
    }
}
