package com.example.foodiesapp.client;

import com.example.foodiesapp.models.Post.PostRequest;
import com.example.foodiesapp.models.Post.PostResponse;
import com.example.foodiesapp.models.User.User;
import com.example.foodiesapp.utils.web.Endpoints;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IFoodiesClient {

    @FormUrlEncoded
    @POST(Endpoints.USER_SIGN_IN)
    Observable<User> signIn(@Field("token") String token);

    @POST(Endpoints.POST_GET)
    Observable<PostResponse> getPosts(@Body PostRequest postRequest);
}
