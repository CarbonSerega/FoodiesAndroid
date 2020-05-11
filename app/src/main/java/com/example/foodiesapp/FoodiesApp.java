package com.example.foodiesapp;

import android.app.Application;
import android.content.Context;

import com.example.foodiesapp.di.AppComponent;
import com.example.foodiesapp.di.AppModule;
import com.example.foodiesapp.di.ClientBuilder;
import com.example.foodiesapp.di.DaggerAppComponent;

public final class FoodiesApp extends Application {
    AppComponent appComponent;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).clientBuilder(new ClientBuilder()).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }
}
