package com.example.recipereviews.models.models;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.firebase.AuthFirebase;
import com.example.recipereviews.models.firebase.ModelFirebase;
import com.example.recipereviews.models.room.RecipeReviewsLocalDb;
import com.example.recipereviews.models.room.RecipeReviewsLocalDbRepository;
import com.google.android.gms.common.util.Strings;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class UserModel {

    private static final UserModel instance = new UserModel();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final AuthFirebase authFirebase = new AuthFirebase();
    private final ModelFirebase modelFirebase = new ModelFirebase();
    private final RecipeReviewsLocalDbRepository localDb = RecipeReviewsLocalDb.getLocalDb();
    private final MutableLiveData<LoadingState> userListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final MutableLiveData<LoadingState> loggedInUserLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final MutableLiveData<User> loggedInUser = new MutableLiveData<>();

    private UserModel() {
    }

    public static UserModel getInstance() {
        return instance;
    }

    public Executor getExecutor() {
        return this.executor;
    }

    public MutableLiveData<LoadingState> getLoggedInUserLoadingState() {
        return this.loggedInUserLoadingState;
    }

    public void uploadUserImage(Bitmap imageBitmap, String name, Consumer<String> imageUploadCallback) {
        this.modelFirebase.uploadUserImage(imageBitmap, name, imageUploadCallback);
    }

    public void register(User user, String password, Runnable addUserCallback) {
        this.authFirebase.register(user.getEmail(), password, (String uid) -> {
            user.setId(uid);
            modelFirebase.addUser(user, addUserCallback);
        });
    }

    public void logout(Runnable callback) {
        this.authFirebase.logout(callback);
    }

    public void login(String email, String password, Runnable onSuccessCallback, Consumer<String> onFailureCallback) {
        this.authFirebase.login(email, password, onSuccessCallback, onFailureCallback);
    }

    public void doesEmailExist(String email, Consumer<Boolean> onExistCallback, Consumer<String> onNotExistCallback) {
        this.authFirebase.doesEmailExist(email, onExistCallback, onNotExistCallback);
    }

    public boolean isSignedIn() {
        return this.authFirebase.isSignedIn();
    }

    public String getCurrentUserId() {
        return this.authFirebase.getCurrentUserId();
    }

    public LiveData<User> getUserById(String id) {
        if (this.loggedInUser.getValue() == null) {
            this.refreshLoggedInUser(id, () -> {});
        }

        return this.loggedInUser;
    }

    public void refreshLoggedInUser(String userId, Runnable callback) {
        this.loggedInUserLoadingState.setValue(LoadingState.LOADING);

        this.modelFirebase.getUser(userId, user -> {
            executor.execute(() -> {
                if (user != null) {
                    this.localDb.userDao().insertAll(user);
                    this.loggedInUser.postValue(this.localDb.userDao().getById(userId));
                }

                this.loggedInUserLoadingState.postValue(LoadingState.NOT_LOADING);
            });

            callback.run();
        });
    }

    public void refreshUserList(Runnable callback) {
        this.userListLoadingState.setValue(LoadingState.LOADING);

        this.modelFirebase.getUsers(list -> {
            this.executor.execute(() -> {
                list.forEach(user -> this.localDb.userDao().insertAll(user));
                this.userListLoadingState.postValue(LoadingState.NOT_LOADING);
            });

            callback.run();
        });
    }

    public void updateUser(User user, String oldPassword, String newPassword, Runnable updateFinishedCallback, Consumer<String> updateFailedCallback) {
        if (!Strings.isEmptyOrWhitespace(oldPassword)) {
            this.authFirebase.updatePassword(oldPassword, newPassword, updateFailedCallback, () ->
                this.modelFirebase.updateUser(user, updateFinishedCallback, updateFailedCallback)
            );
        } else {
            this.modelFirebase.updateUser(user, updateFinishedCallback, updateFailedCallback);
        }
    }
}
