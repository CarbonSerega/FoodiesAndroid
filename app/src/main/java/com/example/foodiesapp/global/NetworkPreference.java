package com.example.foodiesapp.global;

public final class NetworkPreference {
    private static boolean isConnected;

    public static boolean isConnected() {
        return isConnected;
    }

    public static void setIsConnected(boolean isConnected) {
        NetworkPreference.isConnected = isConnected;
    }
}
