package com.example.foodiesapp.models.User;

import androidx.annotation.NonNull;

import com.example.foodiesapp.utils.web.StatusCodes;

import javax.inject.Singleton;

@Singleton
public class UserResponse {

    private final StatusCodes statusCodes;
    private final User user;
    private final Throwable error;

    private UserResponse(StatusCodes statusCodes, User data, Throwable error) {

        this.statusCodes = statusCodes;
        this.user = data;
        this.error = error;
    }

    public static UserResponse loading() {
        return new UserResponse(StatusCodes.LOADING, null, null);
    }

    public static UserResponse success(@NonNull User data) {
        return new UserResponse(StatusCodes.SUCCESS, data, null);
    }

    public static UserResponse error(@NonNull Throwable error) {
        return new UserResponse(StatusCodes.ERROR, null, error);
    }

    public StatusCodes getStatusCode() {
        return statusCodes;
    }

    public User getUser() {
        return user;
    }

    public Throwable getError() {
        return error;
    }
}
