package com.example.foodiesapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.foodiesapp.global.NetworkPreference;
import com.example.foodiesapp.global.UserPreferences;
import com.example.foodiesapp.models.User.User;
import com.example.foodiesapp.models.User.UserResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public final class SignInFragment extends FoodiesFragment {

    @Inject
    ViewModelFactory viewModelFactory;

    private SignInViewModel signInViewModel;

    private GoogleSignInClient gsc;
    private GoogleSignInAccount account;

    private RelativeLayout root;

    private View userCardView;
    private View userCardUnauthedView;

    private TextView userNameTextView;
    private TextView userEmailTextView;
    private TextView userCardErrorTextView;
    private CircleImageView userPictureImageView;

    private Button signOutButton;
    private Button detailsButton;
    private ImageView reloader;

    private ProgressBar progressBar;

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
            signOutButton = userCardView.findViewById(R.id.left_menu_sign_out_button);
            signOutButton.setOnClickListener(this::onSignOutClick);

            detailsButton = userCardView.findViewById(R.id.left_menu_details_button);

            reloader = userCardView.findViewById(R.id.user_card_reloader);
            reloader.setOnClickListener(this::onSignInClick);

            userCardErrorTextView = userCardView.findViewById(R.id.user_card_error_text);

            progressBar = userCardView.findViewById(R.id.left_panel_progress_bar);

            userCardUnauthedView = inflater.inflate(R.layout.user_card_unauthed, requireActivity().findViewById(R.id.user_card_unauthed_view));
            Button signInButton = userCardUnauthedView.findViewById(R.id.left_menu_google_sign_in_button);
            signInButton.setOnClickListener(this::onSignInClick);

            userNameTextView = userCardView.findViewById(R.id.current_user_name_text_view);
            userEmailTextView = userCardView.findViewById(R.id.current_user_email_text_view);
            userPictureImageView = userCardView.findViewById(R.id.current_user_image_view);
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
        if(NetworkPreference.isConnected()) {
            gsc.signOut();
            gsc.revokeAccess().addOnCompleteListener(requireActivity(), task -> {
                account = null;
                UserPreferences.setSignedUserOnNull();
                updateValues();
                updateUI();
            });
        } else {
            UserPreferences.setSignedUserOnNull();
            updateValues();
            showUserCardReloader(R.string.user_card_timeout_error);
        }
    }

    private void updateUI() {
        root.removeAllViews();
        if (account != null) {
            if (userCardView.getParent() != null)
                ((ViewGroup) (userCardView.getParent())).removeView(userCardView);
            root.addView(userCardView);
        } else {
            if (userCardUnauthedView.getParent() != null)
                ((ViewGroup) (userCardUnauthedView.getParent())).removeView(userCardUnauthedView);
            root.addView(userCardUnauthedView);
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> task) {
        try {
            account = task.getResult(ApiException.class);
            if(account != null) {
                updateUI();
                signInViewModel.callClientSignIn(account.getIdToken());
                Log.d("TOKEN", account.getIdToken());
            }
        } catch (ApiException e) {
            UserPreferences.setSignedUserOnNull();
            account = null;
            if(e.getStatusCode() != 4) {
                updateValues();
                showUserCardReloader(R.string.user_card_google_services_error);
            }
            Log.w("HandleSignInResult", "Error:", e);
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
        if (progressBar != null)
          progressBar.setVisibility(View.GONE);

        if(user != null) {
            if(user.getError() == null) {
                UserPreferences.setSignedUser(user);
                updateValues();
            } else {
                if (user.getError().getDetails() != null) {
                    UserPreferences.setSignedUserOnNull();
                    updateValues();
                    showUserCardReloader(R.string.user_card_server_error);
                    Log.d("UserError", user.getError().getDetails());
                }
            }
        }
    }

    private void handleLoadingResponse() {
        if(NetworkPreference.isConnected()) {
            hideUserCardReloader();
        } else {
            UserPreferences.setSignedUserOnNull();
            updateValues();
            showUserCardReloader(R.string.user_card_timeout_error);
        }
    }

    private void handleErrorResponse(@NotNull Throwable error) {
        UserPreferences.setSignedUserOnNull();
        updateValues();
        showUserCardReloader(R.string.user_card_timeout_error);
        Log.d("User response err", Objects.requireNonNull(error.getMessage()));
    }

    private void updateValues() {
        User signedUser = UserPreferences.getSignedUser();

        signOutButton.setVisibility(signedUser != null ? View.VISIBLE : View.GONE);
        detailsButton.setVisibility(signedUser != null ? View.VISIBLE : View.GONE);

        userNameTextView.setText(signedUser != null ? signedUser.getName() : "");
        userEmailTextView.setText(signedUser != null ? signedUser.getEmail() : "");

        String userPicture = signedUser != null ? signedUser.getPicture() : null;

        if(userPicture != null) {
            Glide.with(this)
                    .load(userPicture)
                    .into(userPictureImageView);
        } else {
            userPictureImageView.setImageDrawable(null);
        }
    }

    private void showUserCardReloader(int errString) {
        progressBar.setVisibility(View.GONE);
        reloader.setVisibility(View.VISIBLE);
        userCardErrorTextView.setVisibility(View.VISIBLE);
        userCardErrorTextView.setText(errString);
    }

    private void hideUserCardReloader() {
        progressBar.setVisibility(View.VISIBLE);
        reloader.setVisibility(View.GONE);
        userCardErrorTextView.setVisibility(View.GONE);
        userCardErrorTextView.setText("");
    }
}