package com.example.foodiesapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiesapp.MainActivity;
import com.example.foodiesapp.R;
import com.example.foodiesapp.base.FoodiesFragment;
import com.example.foodiesapp.di.modules.factory.ViewModelFactory;
import com.example.foodiesapp.global.UserPreferences;
import com.example.foodiesapp.models.Post.Post;
import com.example.foodiesapp.models.Post.PostRequest;
import com.example.foodiesapp.models.Post.PostResult;
import com.example.foodiesapp.ui.adapters.adapter.post.PostListAdapter;
import com.example.foodiesapp.ui.adapters.adapter.post.PostSelectedListener;
import com.example.foodiesapp.ui.auth.SignInViewModel;
import com.example.foodiesapp.ui.requestManagers.PostRequestManger;

import java.util.Objects;

import javax.inject.Inject;

public final class HomeFragment extends FoodiesFragment implements PostSelectedListener, SearchView.OnQueryTextListener {

    @Inject
    ViewModelFactory viewModelFactory;

    private HomeViewModel homeViewModel;

    private RecyclerView postsView;

    private ProgressBar recyclerViewProgressBar;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewProgressBar = view.findViewById(R.id.recycler_view_progress_bar);

        homeViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
        SignInViewModel signInViewModel = new ViewModelProvider(this, viewModelFactory).get(SignInViewModel.class);


        signInViewModel.signInResponse().observe(getViewLifecycleOwner(), e -> {
            switch (e.getStatusCode()){
                case LOADING:
                    break;
                case SUCCESS: case ERROR: homeViewModel.callGetPosts(PostRequestManger.withUserRequest(e.getUser()));  break;
            }
        });

        homeViewModel.getPostsResponse().observe(getViewLifecycleOwner(), this::consumeResponse);
        postsView = requireActivity().findViewById(R.id.post_recycler_view);
        postsView.addItemDecoration(new DividerItemDecoration(getBaseActivity(), DividerItemDecoration.VERTICAL));
        postsView.setAdapter(new PostListAdapter(homeViewModel, this, this));
        postsView.setLayoutManager(new LinearLayoutManager(getContext()));
        postsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);



            }
        });
        MainActivity.searchView.setOnQueryTextListener(this);
    }




    @Override
    public void onPostSelected(Post post) {

    }

    private void consumeResponse(@NonNull PostResult postResult) {
        switch (postResult.getStatusCode()){
            case LOADING:
                handleLoadingResponse();
                break;
            case SUCCESS:
                handleSuccessResponse(postResult);
                break;
            case ERROR:
                handleErrorResponse(postResult.getError());
                break;
        }
    }

    private void handleLoadingResponse() {
        recyclerViewProgressBar.setVisibility(View.VISIBLE);
    }

    private void handleSuccessResponse(PostResult postResult) {
        if(postResult.getPostResponse() != null) {
            recyclerViewProgressBar.setVisibility(View.GONE);
        }
    }

    private void handleErrorResponse(Throwable error) {
        Log.d("POSTS_ERROR", Objects.requireNonNull(error.getMessage()));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        homeViewModel.callGetPosts(new PostRequest(
                UserPreferences.getSignedUser() != null ? UserPreferences.getSignedUser() .getId() : -1,
                UserPreferences.getSignedUser()  != null ? UserPreferences.getSignedUser() .getLocale() : null,
                -1,
                null,
                query,
                null
        ));
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        homeViewModel.callGetPosts(new PostRequest(
                UserPreferences.getSignedUser() != null ? UserPreferences.getSignedUser() .getId() : -1,
                UserPreferences.getSignedUser()  != null ? UserPreferences.getSignedUser() .getLocale() : null,
                -1,
                null,
                newText,
                null
        ));
        return false;
    }
}