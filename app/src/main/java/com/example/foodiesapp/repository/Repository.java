package com.example.foodiesapp.repository;

import com.example.foodiesapp.client.IFoodiesClient;
import com.example.foodiesapp.models.User.User;

import io.reactivex.Observable;
import retrofit2.Call;

public class Repository {
    private IFoodiesClient foodiesClient;

    public Repository(IFoodiesClient foodiesClient) {
        this.foodiesClient = foodiesClient;
    }

    public Observable<User> signIn(String token) {
        return foodiesClient.signIn(token);
    }
}
