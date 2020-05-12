package com.example.foodiesapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SignInFragment extends FoodiesFragment {

    @Inject
    ViewModelFactory viewModelFactory;

    private SignInViewModel signInViewModel;

    private GoogleSignInClient gsc;
    private GoogleSignInAccount account;

    private RelativeLayout root;

    private View userCardView;
    private View userCardUnauthedView;

    @Override
    protected int layoutRes() {
        return R.layout.user_card_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        root = view.findViewById(R.id.user_card_fragment_view);

        signInViewModel = new ViewModelProvider(this, viewModelFactory).get(SignInViewModel.class);
        signInViewModel.signInResponse().observe(getViewLifecycleOwner(), this::consumeResponse);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(requireContext(), gso);

        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            userCardView = inflater.inflate(R.layout.user_card, requireActivity().findViewById(R.id.user_card_view));
            Button signOutButton = userCardView.findViewById(R.id.left_menu_sign_out_button);
            signOutButton.setOnClickListener(this::onSignOutClick);

            userCardUnauthedView = inflater.inflate(R.layout.user_card_unauthed, requireActivity().findViewById(R.id.user_card_unauthed_view));
            Button signInButton = userCardUnauthedView.findViewById(R.id.left_menu_google_sign_in_button);
            signInButton.setOnClickListener(this::onSignInClick);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(requireContext());
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

    private void onSignInClick(View view) {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    private void onSignOutClick(View view) {
        gsc.signOut();
        gsc.revokeAccess().addOnCompleteListener(requireActivity(), task -> {
            account = null;
            updateUI();
        });
    }

    private void updateUI() {
        if(account != null) {
            if (userCardView.getParent() != null)
                ((ViewGroup) (userCardView.getParent())).removeView(userCardView);
            root.removeView(userCardUnauthedView);
            root.addView(userCardView);
        } else {
            if (userCardUnauthedView.getParent() != null)
                ((ViewGroup) (userCardUnauthedView.getParent())).removeView(userCardUnauthedView);
            root.removeView(userCardView);
            root.addView(userCardUnauthedView);
        }
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
        ProgressBar progressBar = root.findViewById(R.id.left_panel_progress_bar);

        if (progressBar != null)
          progressBar.setVisibility(View.GONE);

        if(user != null){

            if(user.getError() == null) {
                TextView userNameTextView = requireView().findViewById(R.id.current_user_name_text_view);
                TextView userEmailTextView = requireView().findViewById(R.id.current_user_email_text_view);
                CircleImageView userPictureImageView = requireView().findViewById(R.id.current_user_image_view);

                userNameTextView.setText(user.getName());
                userEmailTextView.setText(user.getEmail());
                Glide.with(this)
                        .load(user.getPicture())
                        .into(userPictureImageView);
            } else {
                if (user.getError().getDetails() != null) {
                    Log.d("UserError", user.getError().getDetails());
                }
            }
        }
    }

    private void handleLoadingResponse() {
        requireView().findViewById(R.id.left_panel_progress_bar).setVisibility(View.VISIBLE);
    }

    private void handleErrorResponse(Throwable error) {
        Log.d("ERROR", Objects.requireNonNull(error.getMessage()));
    }


}
