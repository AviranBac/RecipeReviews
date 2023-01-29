package com.example.recipereviews.models.firebase.collections;

import com.example.recipereviews.models.entities.Review;
import com.example.recipereviews.models.firebase.StorageFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ReviewModelFirebase extends StorageFirebase {

    private static final String IMAGE_FOLDER = "reviews";
    private final String COLLECTION_NAME = "reviews";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ReviewModelFirebase() {
        super(IMAGE_FOLDER);
    }

    public void getReviewsByRecipeId(int recipeId, Consumer<List<Review>> callback) {
        this.db.collection(COLLECTION_NAME)
                .whereEqualTo("recipeId", recipeId)
                .get()
                .addOnCompleteListener(this.getOnCompleteListener(callback));
    }

    public void getReviewById(String reviewId, Consumer<Review> callback) {
        this.db.collection(COLLECTION_NAME).document(reviewId)
                .get()
                .addOnCompleteListener((Task<DocumentSnapshot> task) -> {
                    Review review = null;
                    if (task.isSuccessful()) {
                        DocumentSnapshot json = task.getResult();
                        if (json.exists()) {
                            review = Review.create(Objects.requireNonNull(json.getData()), json.getId());
                        }
                    }
                    callback.accept(review);
                });
    }

    public void getReviewsByUserId(String userId, Consumer<List<Review>> callback) {
        this.db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(this.getOnCompleteListener(callback));
    }

    private OnCompleteListener<QuerySnapshot> getOnCompleteListener(Consumer<List<Review>> callback) {
        return (Task<QuerySnapshot> task) -> {
            List<Review> list = new LinkedList<>();
            if (task.isSuccessful()) {
                QuerySnapshot jsonsList = task.getResult();
                for (DocumentSnapshot json : jsonsList) {
                    list.add(Review.create(Objects.requireNonNull(json.getData()), json.getId()));
                }

            }
            callback.accept(list);
        };
    }

    public void addReview(Review review, Consumer<String> onAddSuccessListener, Consumer<String> onAddFailureListener) {
        Map<String, Object> jsonReview = review.toMap();
        db.collection(COLLECTION_NAME)
                .add(jsonReview)
                .addOnSuccessListener(documentReference -> onAddSuccessListener.accept(documentReference.getId()))
                .addOnFailureListener(e -> onAddFailureListener.accept(e.getMessage()));
    }

    public void editReview(Review review, Runnable onEditSuccessListener, Consumer<String> onEditFailureListener) {
        Map<String, Object> jsonReview = review.toMap();
        db.collection(COLLECTION_NAME)
                .document(review.getId())
                .set(jsonReview, SetOptions.merge())
                .addOnSuccessListener(unused -> onEditSuccessListener.run())
                .addOnFailureListener(e -> onEditFailureListener.accept(e.getMessage()));
    }
}
