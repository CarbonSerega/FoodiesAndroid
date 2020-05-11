package com.example.foodiesapp.utils.ui;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

public final class NavigationViewSettings {

    public static void switchNavigationView(Boolean condition, View view1, View view2, NavigationView navView) {
        if(navView.getHeaderCount() != 0) {
            navView.removeHeaderView(navView.getHeaderView(0));
        }
        navView.addHeaderView(condition ? view1 : view2);
    }
}
