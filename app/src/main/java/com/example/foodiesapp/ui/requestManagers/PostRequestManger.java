package com.example.foodiesapp.ui.requestManagers;

import com.example.foodiesapp.models.Post.PostRequest;
import com.example.foodiesapp.models.User.User;

public final class PostRequestManger {
    public static PostRequest defaultPostRequest() {
        return new PostRequest(
                -1,
                null,
                -1,
                null,
                null,
                null
        );
    }

    public static PostRequest withUserRequest(User user) {
        return new PostRequest(
                user != null ? user.getId() : -1,
                user != null ? user.getLocale() : null,
                -1,
                null,
                null,
                null
        );
    }

}
