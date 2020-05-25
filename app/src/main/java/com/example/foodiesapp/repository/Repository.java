package com.example.foodiesapp.repository;

import com.example.foodiesapp.client.IFoodiesClient;
import com.example.foodiesapp.models.Post.Post;
import com.example.foodiesapp.models.Post.PostRequest;
import com.example.foodiesapp.models.Post.PostResponse;
import com.example.foodiesapp.models.User.User;

import java.util.List;

import io.reactivex.Observable;

public class Repository {
    private IFoodiesClient foodiesClient;

    public Repository(IFoodiesClient foodiesClient) {
        this.foodiesClient = foodiesClient;
    }

    public Observable<User> signIn(String token) {
        return foodiesClient.signIn(token);
    }

    public Observable<List<Post>> getPosts(PostRequest postRequest) {
        return foodiesClient.getPosts(postRequest);
    }
}
