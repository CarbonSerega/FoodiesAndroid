package com.example.foodiesapp.di.modules;

import com.example.foodiesapp.ui.auth.SignInFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBindingModule {
    @ContributesAndroidInjector
    abstract SignInFragment provideSignInFragment();
}
