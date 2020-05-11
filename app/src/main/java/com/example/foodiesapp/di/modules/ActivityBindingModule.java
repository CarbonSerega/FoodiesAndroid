package com.example.foodiesapp.di.modules;

import com.example.foodiesapp.ui.auth.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Module
public abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract MainActivity bindMainActivity();
}
