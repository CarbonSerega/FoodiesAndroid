package com.example.foodiesapp.client;

import com.example.foodiesapp.models.User.User;
import com.example.foodiesapp.utils.web.Endpoints;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IFoodiesClient {
    @POST(Endpoints.USER_SIGN_IN)
    @FormUrlEncoded
    Observable<User> signIn(@Field("token") String token);
}
