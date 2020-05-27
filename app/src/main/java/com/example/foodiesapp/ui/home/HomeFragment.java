package com.example.foodiesapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiesapp.R;
import com.example.foodiesapp.base.FoodiesFragment;
import com.example.foodiesapp.di.modules.factory.ViewModelFactory;
import com.example.foodiesapp.global.UserPreferences;
import com.example.foodiesapp.models.Post.Post;
import com.example.foodiesapp.models.Post.PostRequest;
import com.example.foodiesapp.models.Post.PostResult;
import com.example.foodiesapp.models.User.User;
import com.example.foodiesapp.ui.adapters.adapter.post.PostListAdapter;
import com.example.foodiesapp.ui.adapters.adapter.post.PostSelectedListener;
import com.example.foodiesapp.ui.auth.SignInViewModel;

import java.util.Objects;

import javax.inject.Inject;

public final class HomeFragment extends FoodiesFragment implements PostSelectedListener {

    @Inject
    ViewModelFactory viewModelFactory;

    private HomeViewModel homeViewModel;

    private SignInViewModel signInViewModel;

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
        signInViewModel = new ViewModelProvider(this, viewModelFactory).get(SignInViewModel.class);


        signInViewModel.signInResponse().observe(getViewLifecycleOwner(), e -> {
            switch (e.getStatusCode()){
                case LOADING:
                    break;
                case SUCCESS: case ERROR: homeViewModel.callGetPosts(createDefaultPostRequest());  break;

            }
        });

        homeViewModel.getPostsResponse().observe(getViewLifecycleOwner(), this::consumeResponse);
        postsView = requireActivity().findViewById(R.id.post_recycler_view);
        postsView.addItemDecoration(new DividerItemDecoration(getBaseActivity(), DividerItemDecoration.VERTICAL));
        postsView.setAdapter(new PostListAdapter(homeViewModel, this, this));
        postsView.setLayoutManager(new LinearLayoutManager(getContext()));
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

    private PostRequest createDefaultPostRequest() {
        User user = UserPreferences.getSignedUser();
        Log.d("USER_INNER", user == null ? "NULL" : user.getName());
        return new PostRequest(
                user != null ? user.getId() : -1,
                user != null ? user.getLocale() : null,
                -1,
                null,
                null,
                null
        );
    }
}