package com.example.recipereviews.models.firebase.collections;

import android.graphics.Bitmap;

import com.example.recipereviews.models.entities.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class UserModelFirebase {

    private final String IMAGE_FOLDER = "users";
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

    public void uploadUserImage(Bitmap imageBitmap, String name, Consumer<String> imageUploadCallback) {
        StorageReference imagesRef = storage.getReference().child(IMAGE_FOLDER + "/" + name + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        imagesRef.putBytes(data)
                .addOnFailureListener((Exception e) -> imageUploadCallback.accept(null))
                .addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) ->
                        imagesRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> imageUploadCallback.accept(uri.toString()))
                );
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

}
