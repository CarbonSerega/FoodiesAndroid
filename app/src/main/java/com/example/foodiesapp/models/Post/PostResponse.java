package com.example.foodiesapp.models.Post;

import androidx.annotation.NonNull;

import com.example.foodiesapp.utils.web.StatusCodes;

import java.util.List;

public final class PostResponse {
    private final StatusCodes statusCodes;
    private final List<Post> posts;
    private final Throwable error;

    private PostResponse(StatusCodes statusCodes, List<Post> data, Throwable error) {

        this.statusCodes = statusCodes;
        this.posts = data;
        this.error = error;
    }

    public static PostResponse loading() {
        return new PostResponse(StatusCodes.LOADING, null, null);
    }

    public static PostResponse success(@NonNull List<Post> data) {
        return new PostResponse(StatusCodes.SUCCESS, data, null);
    }

    public static PostResponse error(@NonNull Throwable error) {
        return new PostResponse(StatusCodes.ERROR, null, error);
    }

    public StatusCodes getStatusCode() {
        return statusCodes;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Throwable getError() {
        return error;
    }
}
