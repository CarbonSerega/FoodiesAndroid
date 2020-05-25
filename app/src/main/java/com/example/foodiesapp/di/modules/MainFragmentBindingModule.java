package com.example.foodiesapp.di.modules;

import com.example.foodiesapp.ui.auth.SignInFragment;
import com.example.foodiesapp.ui.home.HomeFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class MainFragmentBindingModule {
    @ContributesAndroidInjector
    abstract SignInFragment provideSignInFragment();

    @ContributesAndroidInjector
    abstract HomeFragment provideHomeFragment();
}
