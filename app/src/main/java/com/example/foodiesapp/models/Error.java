package com.example.foodiesapp.models;

import androidx.annotation.Nullable;

public class Error {
    private int code;
    private String msg;

    @Nullable
    private String details;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Nullable
    public String getDetails() {
        return details;
    }
}
