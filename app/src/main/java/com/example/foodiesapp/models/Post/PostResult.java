package com.example.foodiesapp.models.Post;

import androidx.annotation.NonNull;

import com.example.foodiesapp.utils.web.StatusCodes;

public final class PostResult {
    private final StatusCodes statusCodes;
    private final PostResponse postResponse;
    private final Throwable error;

    private PostResult(StatusCodes statusCodes, PostResponse data, Throwable error) {

        this.statusCodes = statusCodes;
        this.postResponse = data;
        this.error = error;
    }

    public static PostResult loading() {
        return new PostResult(StatusCodes.LOADING, null, null);
    }

    public static PostResult success(@NonNull PostResponse data) {
        return new PostResult(StatusCodes.SUCCESS, data, null);
    }

    public static PostResult error(@NonNull Throwable error) {
        return new PostResult(StatusCodes.ERROR, null, error);
    }

    public StatusCodes getStatusCode() {
        return statusCodes;
    }

    public PostResponse getPostResponse() {
        return postResponse;
    }

    public Throwable getError() {
        return error;
    }
}
