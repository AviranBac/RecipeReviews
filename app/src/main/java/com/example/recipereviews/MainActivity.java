package com.example.recipereviews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.recipereviews.activities.GuestActivity;
import com.example.recipereviews.activities.UserActivity;
import com.example.recipereviews.models.Model;

public class MainActivity extends AppCompatActivity {

    private final Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Model.getInstance().getExecutor().execute(() -> {
            if (Model.getInstance().isSignedIn()) {
                mainHandler.post(this::startUserActivity);
            } else {
                mainHandler.post(this::startGuestActivity);
            }
        });
    }

    private void startGuestActivity() {
        startActivityFromIntent(GuestActivity.class);
    }

    private void startUserActivity() {
        startActivityFromIntent(UserActivity.class);
    }

    private void startActivityFromIntent(Class<? extends AppCompatActivity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }
}