package com.example.recipereviews.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public abstract class NavHostActivity extends AppCompatActivity {

    protected int activityLayoutId;
    protected int navHostId;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activityLayoutId);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(navHostId);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            navController.popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }
}