package com.example.recipereviews.models.firebase;

import android.graphics.Bitmap;

import com.example.recipereviews.models.entities.Review;
import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.firebase.collections.ReviewModelFirebase;
import com.example.recipereviews.models.firebase.collections.UserModelFirebase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.List;
import java.util.function.Consumer;

public class ModelFirebase {

    private final UserModelFirebase userFirebase = new UserModelFirebase();
    private final ReviewModelFirebase reviewFirebase = new ReviewModelFirebase();

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
    }

    public void addUser(User user, Runnable addUserCallback) {
        this.userFirebase.addUser(user, addUserCallback);
    }

    public void uploadUserImage(Bitmap imageBitmap, String name, Consumer<String> imageUploadCallback) {
        this.userFirebase.uploadUserImage(imageBitmap, name, imageUploadCallback);
    }

    public void getReviewsByRecipeId(int recipeId, Consumer<List<Review>> callback) {
        this.reviewFirebase.getReviewsByRecipeId(recipeId, callback);
    }

    public void getReviewsByUserId(String userId, Consumer<List<Review>> callback) {
        this.reviewFirebase.getReviewsByUserId(userId, callback);
    }

    public void getUsers(Consumer<List<User>> callback) {
        this.userFirebase.getUsers(callback);
    }

    public void getUser(String userId, Consumer<User> callback) {
        this.userFirebase.getUser(userId, callback);
    }
}
