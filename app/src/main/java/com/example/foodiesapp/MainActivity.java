package com.example.foodiesapp;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.foodiesapp.base.FoodiesActivity;
import com.example.foodiesapp.global.NetworkPreference;
import com.example.foodiesapp.utils.ui.AppBarTuner;
import com.example.foodiesapp.utils.web.Connectivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends FoodiesActivity implements
        //NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener,
        Connectivity.NetworkStateReceiverListener {


    private Connectivity networkConnectivity;

    public static SearchView searchView;

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set-up network connectivity
        networkConnectivity = new Connectivity();
        networkConnectivity.addListener(this);
        registerNetworkBroadcastReceiver(this);


        //Bottom navigation set-up
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        //Action bar set-up
        setSupportActionBar(findViewById(R.id.tuned_toolbar));
        ActionBar actionBar = AppBarTuner.tunedToolBar(getSupportActionBar(), R.layout.app_bar);
        searchView = AppBarTuner.tunedSearchView(actionBar, findViewById(R.id.search_view), findViewById(R.id.left_menu_icon));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterNetworkBroadcastReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkBroadcastReceiver(this);
    }

    public void onLeftMenuIconClick(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.left_sidebar_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private boolean isFilterMenuPressed = false;

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


    private void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(networkConnectivity, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void unregisterNetworkBroadcastReceiver(Context currentContext) {
        currentContext.unregisterReceiver(networkConnectivity);
    }

    @Override
    public void networkAvailable() {
        //Log.d("NETWORK", "networkAvailable: ");
        NetworkPreference.setIsConnected(true);
    }

    @Override
    public void networkUnavailable() {
        NetworkPreference.setIsConnected(false);
    }
}