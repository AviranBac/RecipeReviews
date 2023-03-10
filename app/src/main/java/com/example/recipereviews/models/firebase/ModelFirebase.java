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
        this.userFirebase.uploadImage(imageBitmap, name, imageUploadCallback);
    }

    public void getReviewsSince(long since, Consumer<List<Review>> callback) {
        this.reviewFirebase.getReviewsSince(since, callback);
    }

    public void getUsersSince(long since, Consumer<List<User>> callback) {
        this.userFirebase.getUsersSince(since, callback);
    }

    public void getUser(String userId, Consumer<User> callback) {
        this.userFirebase.getUser(userId, callback);
    }

    public void uploadReviewImage(Bitmap imageBitmap, String name, Consumer<String> imageUploadCallback) {
        this.reviewFirebase.uploadImage(imageBitmap, name, imageUploadCallback);
    }

    public void addReview(Review review, Consumer<String> onAddSuccessListener, Consumer<String> onAddFailureListener) {
        this.reviewFirebase.addReview(review, onAddSuccessListener, onAddFailureListener);
    }

    public void editReview(Review review, Runnable onEditSuccessListener, Consumer<String> onEditFailureListener) {
        this.reviewFirebase.editReview(review, onEditSuccessListener, onEditFailureListener);
    }

    public void updateUser(User user, Runnable successCallback, Consumer<String> failureCallback) {
        this.userFirebase.updateUser(user, successCallback, failureCallback);
    }
}
