package com.example.recipereviews.utils;

import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipereviews.R;

public class NavigationUtils {

    public static void navigate(View view, NavDirections navDirections) {
        Navigation.findNavController(view).navigate(navDirections);
    }

    public static void navigate(FragmentActivity fragmentActivity, NavDirections navDirections) {
        NavHostFragment navHostFragment = (NavHostFragment) fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.user_nav_host);
        navHostFragment.getNavController().navigate(navDirections);
    }
}
