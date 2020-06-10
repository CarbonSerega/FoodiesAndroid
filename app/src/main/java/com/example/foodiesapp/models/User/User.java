package com.example.foodiesapp.models.User;

import androidx.annotation.Nullable;

import com.example.foodiesapp.models.Error;
import com.google.gson.annotations.SerializedName;

public class User {
    private long id;
    private String name;
    private String email;
    private String picture;
    private String locale;

    @Nullable
    @SerializedName("error")
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

    @Nullable
    public Error getError() {
        return error;
    }
}
