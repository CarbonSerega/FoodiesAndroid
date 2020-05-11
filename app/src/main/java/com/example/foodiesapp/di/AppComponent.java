package com.example.foodiesapp.di;

import com.example.foodiesapp.ui.auth.MainActivity;
import com.example.foodiesapp.ui.home.HomeFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, ClientBuilder.class})
@Singleton
public interface AppComponent {
    void injectInMainActivity(MainActivity loginActivity);
    void injectInHomeFrarment(HomeFragment homeFragment);
}
