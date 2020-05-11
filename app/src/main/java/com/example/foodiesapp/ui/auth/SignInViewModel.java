package com.example.foodiesapp.ui.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodiesapp.models.User.UserResponse;
import com.example.foodiesapp.repository.Repository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SignInViewModel extends ViewModel {
    private Repository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<UserResponse> userLiveData = new MutableLiveData<>();

    public SignInViewModel(Repository repository) {
        this.repository = repository;
    }

    MutableLiveData<UserResponse> signInResponse() {
        return userLiveData;
    }

    void callClientSignIn(String token) {
        disposables.add(repository.signIn(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> userLiveData.setValue(UserResponse.loading()))
                .subscribe(
                        result -> userLiveData.setValue(UserResponse.success(result)),
                        throwable -> userLiveData.setValue(UserResponse.error(throwable))
                ));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
