package com.example.recipereviews.models.firebase;

import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.firebase.collections.UserModelFirebase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class ModelFirebase {

    private final UserModelFirebase userFirebase = new UserModelFirebase();

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
}
