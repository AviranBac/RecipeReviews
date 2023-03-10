package com.example.recipereviews.models.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.function.Consumer;

public class AuthFirebase {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void register(String email, String password, Consumer<String> registerCallback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Task<AuthResult> task) -> {
                    if (task.isSuccessful() && task.getResult().getUser() != null) {
                        registerCallback.accept(task.getResult().getUser().getUid());
                    }
                });
    }

    public void login(String email, String password, Runnable onSuccessCallback, Consumer<String> onFailureCallback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener((AuthResult authResult) -> onSuccessCallback.run())
                .addOnFailureListener((Exception e) -> onFailureCallback.accept(e.getMessage()));
    }

    public void updatePassword(String oldPassword, String newPassword, Consumer<String> onFailureCallback, Runnable onSuccessCallback) {
        firebaseAuth.getCurrentUser().reauthenticate(EmailAuthProvider.getCredential(firebaseAuth.getCurrentUser().getEmail(), oldPassword))
                .addOnCompleteListener((Task<Void> reauthenticateTask) -> {
                    if (reauthenticateTask.isSuccessful()) {
                        firebaseAuth.getCurrentUser().updatePassword(newPassword)
                                .addOnCompleteListener((Task<Void> updatePasswordTask) -> {
                                    if (updatePasswordTask.isSuccessful()) {
                                        onSuccessCallback.run();
                                    }
                                })
                                .addOnFailureListener(error -> onFailureCallback.accept(error.getMessage()));
                    }
                })
                .addOnFailureListener(error -> onFailureCallback.accept(error.getMessage()));
    }

    public void logout(Runnable callback) {
        firebaseAuth.signOut();
        callback.run();
    }

    public void doesEmailExist(String email, Consumer<Boolean> onExistCallback, Consumer<String> onNotExistCallback) {
        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnSuccessListener((SignInMethodQueryResult signInMethodQueryResult) -> {
                    if (signInMethodQueryResult.getSignInMethods() != null) {
                        boolean isNewUser = signInMethodQueryResult.getSignInMethods().isEmpty();
                        onExistCallback.accept(!isNewUser);
                    }
                })
                .addOnFailureListener((Exception e) -> onNotExistCallback.accept(e.getMessage()));
    }

    public boolean isSignedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public String getCurrentUserId() {
        return this.isSignedIn() ?
                firebaseAuth.getCurrentUser().getUid() :
                null;
    }
}
