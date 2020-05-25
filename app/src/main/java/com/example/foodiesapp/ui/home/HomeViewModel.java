package com.example.foodiesapp.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodiesapp.models.Post.PostRequest;
import com.example.foodiesapp.models.Post.PostResponse;
import com.example.foodiesapp.repository.Repository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public final class HomeViewModel extends ViewModel {
    private Repository repository;
    private CompositeDisposable disposables;
    private MutableLiveData<PostResponse> postsLiveData;

    @Inject
    HomeViewModel(Repository repository) {
        disposables = new CompositeDisposable();
        postsLiveData = new MutableLiveData<>();
        this.repository = repository;
    }

    MutableLiveData<PostResponse> signInResponse() {
        return postsLiveData;
    }

    void callGetPosts(PostRequest postRequest) {
        disposables.add(repository.getPosts(postRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> postsLiveData.setValue(PostResponse.loading()))
                .subscribe(
                        result -> postsLiveData.setValue(PostResponse.success(result)),
                        throwable -> postsLiveData.setValue(PostResponse.error(throwable))
                ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}