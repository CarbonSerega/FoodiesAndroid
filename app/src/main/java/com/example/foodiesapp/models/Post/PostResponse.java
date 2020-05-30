package com.example.foodiesapp.models.Post;

import com.example.foodiesapp.models.Error;

import java.util.List;

public final class PostResponse {
    private List<Post> posts;
    private int newPosts;
    private Error error;

    public List<Post> getPosts() {
        return posts;
    }

    public int getNewPosts() {
        return newPosts;
    }

    public Error getError() {
        return error;
    }
}
