package com.example.recipereviews.models.firebase;

import android.graphics.Bitmap;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;

public class StorageFirebase {
    private final String imageFolder;

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public StorageFirebase(String imageFolder) {
        this.imageFolder = imageFolder;
    }

    public void uploadImage(Bitmap imageBitmap, String name, Consumer<String> imageUploadCallback) {
        StorageReference imagesRef = storage.getReference().child(this.imageFolder + "/" + name + ".jpg");

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

}
