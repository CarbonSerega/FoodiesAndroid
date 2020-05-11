package com.example.foodiesapp.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodiesapp.repository.Repository;
import com.example.foodiesapp.ui.auth.SignInViewModel;

import javax.inject.Inject;

public class ViewModelFactory implements ViewModelProvider.Factory{

    private Repository repository;

    @Inject
    public ViewModelFactory(Repository repository) {
        this.repository = repository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignInViewModel.class)) {
            return (T) new SignInViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
