package com.example.foodiesapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.foodiesapp.utils.AppBarTuner;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        ActionBar toolbar = AppBarTuner.tunedToolBar(getSupportActionBar(), R.layout.app_bar);
        SearchView searchView =
                AppBarTuner.tunedSearchView(toolbar, findViewById(R.id.search_view), findViewById(R.id.left_menu_icon));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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
}
