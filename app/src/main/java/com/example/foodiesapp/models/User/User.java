package com.example.foodiesapp.models.User;

import androidx.annotation.Nullable;

import com.example.foodiesapp.models.Error;

public class User {
    private long id;
    private String name;
    private String email;
    private String picture;
    private String locale;

    @Nullable
    private Error error;


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }

    public String getLocale() {
        return locale;
    }
}
