package com.example.foodiesapp.models.User;

import androidx.annotation.NonNull;

import com.example.foodiesapp.utils.web.StatusCodes;

import javax.inject.Singleton;

@Singleton
public class UserResult {

    private final StatusCodes statusCodes;
    private final User user;
    private final Throwable error;

    private UserResult(StatusCodes statusCodes, User data, Throwable error) {

        this.statusCodes = statusCodes;
        this.user = data;
        this.error = error;
    }

    public static UserResult loading() {
        return new UserResult(StatusCodes.LOADING, null, null);
    }

    public static UserResult success(@NonNull User data) {
        return new UserResult(StatusCodes.SUCCESS, data, null);
    }

    public static UserResult error(@NonNull Throwable error) {
        return new UserResult(StatusCodes.ERROR, null, error);
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
