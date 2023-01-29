package com.example.recipereviews.models.firebase.collections;

import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.firebase.StorageFirebase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class UserModelFirebase extends StorageFirebase {

    private static final String IMAGE_FOLDER = "users";
    private final String COLLECTION_NAME = "users";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserModelFirebase() {
        super(IMAGE_FOLDER);
    }

    public void addUser(User user, Runnable addUserCallback) {
        Map<String, Object> jsonUser = user.toMap();
        db.collection(COLLECTION_NAME)
                .document(user.getId())
                .set(jsonUser)
                .addOnCompleteListener(unused -> addUserCallback.run())
                .addOnFailureListener(e -> addUserCallback.run());
    }


    public void getUsers(Consumer<List<User>> callback) {
        this.db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    List<User> list = new LinkedList<>();
                    if (task.isSuccessful()) {
                        QuerySnapshot jsonsList = task.getResult();
                        for (DocumentSnapshot json : jsonsList) {
                            list.add(User.create(Objects.requireNonNull(json.getData()), json.getId()));
                        }

                    }
                    callback.accept(list);
                });
    }

    public void getUser(String userId, Consumer<User> callback) {
        this.db.collection(COLLECTION_NAME)
                .document(userId)
                .get()
                .addOnCompleteListener((Task<DocumentSnapshot> task) -> {
                    User user = null;

                    if (task.isSuccessful()) {
                        DocumentSnapshot json = task.getResult();
                        if (json != null) {
                            user = User.create(Objects.requireNonNull(json.getData()), json.getId());
                        }
                    }

                    callback.accept(user);
                });
    }
}
