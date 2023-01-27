package com.example.recipereviews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import com.example.recipereviews.activities.GuestActivity;
import com.example.recipereviews.activities.UserActivity;
import com.example.recipereviews.models.models.UserModel;

public class MainActivity extends AppCompatActivity {

    private final Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserModel.getInstance().getExecutor().execute(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }

            Runnable startActivityRunnable = UserModel.getInstance().isSignedIn() ?
                    () -> startActivityFromIntent(UserActivity.class) :
                    () -> startActivityFromIntent(GuestActivity.class);

            mainHandler.post(startActivityRunnable);
        });
    }

    private void startActivityFromIntent(Class<? extends AppCompatActivity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }
}