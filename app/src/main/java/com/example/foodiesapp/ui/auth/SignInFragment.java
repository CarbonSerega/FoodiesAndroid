package com.example.foodiesapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.foodiesapp.R;
import com.example.foodiesapp.base.FoodiesFragment;
import com.example.foodiesapp.di.modules.factory.ViewModelFactory;
import com.example.foodiesapp.models.User.User;
import com.example.foodiesapp.models.User.UserResponse;
import com.example.foodiesapp.utils.ui.NavigationViewSettings;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Objects;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignInFragment extends FoodiesFragment {

    @Inject
    public ViewModelFactory viewModelFactory;

    private SignInViewModel signInViewModel;

    private GoogleSignInClient gsc;
    private GoogleSignInAccount account;

//    private View userCardView;
//    private View userCardUnauthedView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signInViewModel = new ViewModelProvider(this, viewModelFactory).get(SignInViewModel.class);
        signInViewModel.signInResponse().observe(getViewLifecycleOwner(), this::consumeResponse);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(requireContext(), gso);

        //userCardUnauthedView = getLayoutInflater().inflate(R.layout.user_card_unauthed, requireActivity().findViewById(R.id.user_card_unauthed_view));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("ON_START", "stared");
        this.account = GoogleSignIn.getLastSignedInAccount(requireContext());
        updateUI();
        gsc.silentSignIn().addOnCompleteListener(requireActivity(), this::handleSignInResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    public void onSignInClick(View view) {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    public void onSignOutClick(View view) {
        gsc.signOut();
        gsc.revokeAccess().addOnCompleteListener((Executor) this, task -> {
            account = null;
            updateUI();
        });
    }

    private void updateUI() {
//        NavigationViewSettings.switchNavigationView(this.account == null,
//                userCardUnauthedView, getView(), requireActivity().findViewById(R.id.left_sidebar));
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> task) {
        try {
            account = task.getResult(ApiException.class);
            if(account != null) {
                updateUI();
                signInViewModel.callClientSignIn(account.getIdToken());
            }
        } catch (ApiException e) {
            account = null;
            Log.w("HandleSignInResult", "Error -> ", e);
        }
    }

    private void consumeResponse(@NonNull UserResponse userResponse) {
        switch (userResponse.getStatusCode()) {
            case LOADING:
                handleLoadingResponse();
                break;
            case SUCCESS:
                handleSuccessResponse(userResponse.getUser());
                break;
            case ERROR:
                handleErrorResponse(userResponse.getError());
                break;
        }
    }

    private void handleSuccessResponse(User user) {
        requireView().findViewById(R.id.left_panel_progress_bar).setVisibility(View.GONE);
        if(user != null){
            TextView userNameTextView = requireView().findViewById(R.id.current_user_name_text_view);
            TextView userEmailTextView = requireView().findViewById(R.id.current_user_email_text_view);
            CircleImageView userPictureImageView = requireView().findViewById(R.id.current_user_image_view);

            userNameTextView.setText(user.getName());
            userEmailTextView.setText(user.getEmail());
            Glide.with(this)
                    .load(user.getPicture())
                    .into(userPictureImageView);
        }
    }

    private void handleLoadingResponse() {
        requireView().findViewById(R.id.left_panel_progress_bar).setVisibility(View.VISIBLE);
    }

    private void handleErrorResponse(Throwable error) {
        Log.d("ERROR", Objects.requireNonNull(error.getMessage()));
    }

    @Override
    protected int layoutRes() {
        return R.layout.user_card;
    }
}
