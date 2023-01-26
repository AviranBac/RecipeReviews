package com.example.recipereviews.models.firebase.collections;

import com.example.recipereviews.interfaces.Listener;
import com.example.recipereviews.models.entities.Review;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ReviewModelFirebase {

    private final String IMAGE_FOLDER = "reviews";
    private final String COLLECTION_NAME = "reviews";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public void getReviewsByRecipeId(int recipeId, Listener<List<Review>> callback) {
        this.db.collection(COLLECTION_NAME)
                .whereEqualTo("recipeId", recipeId)
                .get()
                .addOnCompleteListener(task -> {
                    List<Review> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        QuerySnapshot jsonsList = task.getResult();
                        for (DocumentSnapshot json : jsonsList) {
                            list.add(Review.create(Objects.requireNonNull(json.getData()), json.getId()));
                        }

                    }
                    callback.onComplete(list);
                });
    }
}
