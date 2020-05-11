package com.example.foodiesapp.utils.ui;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public final class AppBarTuner {

    public static ActionBar tunedToolBar(ActionBar toolbar, int customViewResourceId) {
        if(toolbar != null) {
            toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            toolbar.setCustomView(customViewResourceId);
            Toolbar parent = (Toolbar)toolbar.getCustomView().getParent();
            parent.setPadding(0,0,0,0);
            parent.setContentInsetsAbsolute(0,0);
        }
        return toolbar;
    }

    public static SearchView tunedSearchView(ActionBar toolBar, SearchView searchView, ImageView leftMenuImageView) {

        searchView.setOnSearchClickListener(v -> {
            leftMenuImageView.setVisibility(View.INVISIBLE);
            Objects.requireNonNull(toolBar).setDisplayHomeAsUpEnabled(true);
        });

        searchView.setOnCloseListener(() -> {
            Objects.requireNonNull(toolBar).setDisplayHomeAsUpEnabled(false);
            leftMenuImageView.setVisibility(View.VISIBLE);
            return false;
        });

        return searchView;
    }
}

