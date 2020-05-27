package com.example.foodiesapp.ui.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodiesapp.models.User.UserResult;
import com.example.foodiesapp.repository.Repository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public final class SignInViewModel extends ViewModel {
    private Repository repository;
    private  CompositeDisposable disposables;
    private  MutableLiveData<UserResult> userLiveData;

    @Inject
    SignInViewModel(Repository repository) {
        disposables = new CompositeDisposable();
        userLiveData = new MutableLiveData<>();
        this.repository = repository;
    }

    public MutableLiveData<UserResult> signInResponse() {
        return userLiveData;
    }

    void callClientSignIn(String token) {
        disposables.add(repository.signIn(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> userLiveData.setValue(UserResult.loading()))
                .subscribe(
                        result -> userLiveData.setValue(UserResult.success(result)),
                        throwable -> userLiveData.setValue(UserResult.error(throwable))
                ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}