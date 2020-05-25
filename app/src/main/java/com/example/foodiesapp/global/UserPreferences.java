package com.example.foodiesapp.global;

import androidx.annotation.NonNull;

import com.example.foodiesapp.models.User.User;

public final class UserPreferences {
    private static User signedUser;
    private static boolean hasError;

    public static User getSignedUser() {
        return signedUser;
    }

    public static void setSignedUser(@NonNull User signedUser) {
        UserPreferences.signedUser = signedUser;
    }

    public static void setSignedUserOnNull() {
        UserPreferences.signedUser = null;
    }
}