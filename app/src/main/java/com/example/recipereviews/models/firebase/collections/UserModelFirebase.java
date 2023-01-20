package com.example.recipereviews.models.firebase.collections;

import com.example.recipereviews.models.entities.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public class UserModelFirebase {

    private final String COLLECTION_NAME = "users";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public void addUser(User user, Runnable addUserCallback) {
        Map<String, Object> jsonUser = user.toMap();
        db.collection(COLLECTION_NAME)
                .document(user.getId())
                .set(jsonUser)
                .addOnSuccessListener(unused -> addUserCallback.run())
                .addOnFailureListener(e -> addUserCallback.run());
    }
}
