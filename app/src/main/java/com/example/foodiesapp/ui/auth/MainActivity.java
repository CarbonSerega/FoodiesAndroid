package com.example.foodiesapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.foodiesapp.FoodiesApp;
import com.example.foodiesapp.R;
import com.example.foodiesapp.factory.ViewModelFactory;
import com.example.foodiesapp.models.User.User;
import com.example.foodiesapp.models.User.UserResponse;
import com.example.foodiesapp.utils.ui.AppBarTuner;
import com.example.foodiesapp.utils.ui.NavigationViewSettings;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        //NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener {

    @Inject
    public ViewModelFactory viewModelFactory;
    private SignInViewModel signInViewModel;

    private GoogleSignInClient gsc;
    private GoogleSignInAccount account;

    private View userCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((FoodiesApp) getApplication()).getAppComponent().injectInMainActivity(this);
        signInViewModel = new ViewModelProvider(this, viewModelFactory).get(SignInViewModel.class);
        signInViewModel.signInResponse().observe(this, this::consumeResponse);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        setSupportActionBar(findViewById(R.id.tuned_toolbar));
        ActionBar actionBar = AppBarTuner.tunedToolBar(getSupportActionBar(), R.layout.app_bar);
        SearchView searchView = AppBarTuner.tunedSearchView(actionBar, findViewById(R.id.search_view), findViewById(R.id.left_menu_icon));
        searchView.setOnQueryTextListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ON_START", "stared");
        this.account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI();
        gsc.silentSignIn().addOnCompleteListener(this, this::handleSignInResult);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ON_PAUSE", "paused");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ON_RESUME", "resumed");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    public void onLeftMenuIconClick(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.left_sidebar_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    boolean isFilterMenuPressed = false;

    public void onFilterCardClick(View view) {
        isFilterMenuPressed = !isFilterMenuPressed;
        findViewById(R.id.filter_menu_icon).setBackgroundResource(isFilterMenuPressed
                ? R.color.colorPrimaryDark
                : R.color.colorPrimary);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SearchView sv = findViewById(R.id.search_view);
            if(!sv.isIconified()) {
                sv.setQuery("", false);
                sv.setIconified(true);
            }
            else {
                //TODO
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(getApplicationContext(), "sdfsdf", Toast.LENGTH_LONG).show();
//        return false;
//    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void onSignInClick(View view) {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    public void onSignOutClick(View view) {
        gsc.signOut()
                .addOnCompleteListener(this, task -> {
                    CharSequence sequence = "Signed out!";
                    Toast.makeText(getApplicationContext(), sequence, Toast.LENGTH_SHORT).show();
                });

        gsc.revokeAccess().addOnCompleteListener(this, task -> {
            this.account = null;
            updateUI();
        });
    }

    private void updateUI() {
        View userCardUnauthedView = getLayoutInflater().inflate(R.layout.user_card_unauthed, findViewById(R.id.user_card_unauthed_view));
        userCardView = getLayoutInflater().inflate(R.layout.user_card, findViewById(R.id.user_card_view));
        NavigationViewSettings.switchNavigationView(this.account == null,
                userCardUnauthedView, userCardView, findViewById(R.id.left_sidebar));
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> task) {
        try {
            account = task.getResult(ApiException.class);
            updateUI();
            if(account != null) {
                signInViewModel.callClientSignIn(account.getIdToken());
            }
        } catch (ApiException e) {
            account = null;
            Log.w("HandleSignInResult", "handleSignInResult:error", e);
        }
    }

    private void consumeResponse(@NotNull UserResponse userResponse) {
        switch (userResponse.getStatusCode()) {
            case LOADING:
                handleLoadingResponse();
                break;
            case SUCCESS:
                handleSuccessResponse(userResponse.getUser());
                break;
            case ERROR:
                Log.d("ERROR", Objects.requireNonNull(userResponse.getError().getMessage()));
                break;
        }
    }

    private void handleSuccessResponse(User user) {
        userCardView.findViewById(R.id.left_panel_progress_bar).setVisibility(View.GONE);
        if(user != null){
            TextView userNameTextView = userCardView.findViewById(R.id.current_user_name_text_view);
            TextView userEmailTextView = userCardView.findViewById(R.id.current_user_email_text_view);
            CircleImageView userPictureImageView = userCardView.findViewById(R.id.current_user_image_view);

            userNameTextView.setText(user.getName());
            userEmailTextView.setText(user.getEmail());
            Glide.with(this)
                    .load(user.getPicture())
                    .into(userPictureImageView);
        }
    }

    private void handleLoadingResponse() {
        userCardView.findViewById(R.id.left_panel_progress_bar).setVisibility(View.VISIBLE);
    }

    private void handleErrorResponse(Throwable error) {
        Log.d("ERROR", Objects.requireNonNull(error.getMessage()));
    }
}
