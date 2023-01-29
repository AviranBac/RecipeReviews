package com.example.recipereviews.models.models;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.models.entities.Review;
import com.example.recipereviews.models.entities.ReviewWithRecipe;
import com.example.recipereviews.models.entities.ReviewWithUser;
import com.example.recipereviews.models.firebase.ModelFirebase;
import com.example.recipereviews.models.room.RecipeReviewsLocalDb;
import com.example.recipereviews.models.room.RecipeReviewsLocalDbRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ReviewModel {
    private static final ReviewModel instance = new ReviewModel();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final RecipeReviewsLocalDbRepository localDb = RecipeReviewsLocalDb.getLocalDb();
    private final MutableLiveData<LoadingState> reviewListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final MutableLiveData<LoadingState> profileReviewListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final MutableLiveData<LoadingState> reviewLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final ModelFirebase firebaseModel = new ModelFirebase();
    private MutableLiveData<List<ReviewWithUser>> reviewList = new MutableLiveData<>();
    private MutableLiveData<List<ReviewWithRecipe>> profileReviewList = new MutableLiveData<>();
    private MutableLiveData<ReviewWithUser> reviewWithUser = new MutableLiveData<>();

    private ReviewModel() {
    }

    public static ReviewModel getInstance() {
        return instance;
    }

    public MutableLiveData<LoadingState> getReviewListLoadingState() {
        return this.reviewListLoadingState;
    }

    public MutableLiveData<LoadingState> getProfileReviewListLoadingState() {
        return this.profileReviewListLoadingState;
    }

    public LiveData<List<ReviewWithUser>> getReviewByRecipeId(int recipeId) {
        if (this.reviewList.getValue() == null) {
            this.refreshReviewByRecipeId(recipeId);
        }

        return this.reviewList;
    }

    public void refreshReviewByRecipeId(int recipeId) {
        this.reviewListLoadingState.setValue(LoadingState.LOADING);

        UserModel.getInstance().refreshUserList(() -> this.firebaseModel.getReviewsByRecipeId(recipeId, list -> executor.execute(() -> {
            list.forEach(review -> {
                localDb.reviewDao().insertAll(review);
                if (review.isDeleted()) {
                    localDb.reviewDao().delete(review);
                }
            });
            this.reviewList.postValue(this.localDb.reviewDao().getByRecipeId(recipeId));
            this.reviewListLoadingState.postValue(LoadingState.NOT_LOADING);
        })));
    }

    public LiveData<ReviewWithUser> getReviewWithRelationsById(String id) {
        if (this.reviewWithUser.getValue() == null) {
            this.fetchReviewWithRelationsById(id);
        }

        return this.reviewWithUser;
    }


    public LiveData<List<ReviewWithRecipe>> getReviewByUserId(String userId) {
        if (this.profileReviewList.getValue() == null) {
            this.refreshReviewByUserId(userId);
        }

        return this.profileReviewList;
    }

    public void refreshReviewByUserId(String userId) {
        this.profileReviewListLoadingState.setValue(LoadingState.LOADING);

        UserModel.getInstance().refreshLoggedInUser(userId, () -> this.firebaseModel.getReviewsByUserId(userId, list -> executor.execute(() -> {
            list.forEach(review -> localDb.reviewDao().insertAll(review));
            this.profileReviewList.postValue(this.localDb.reviewDao().getByUserId(userId));
            this.profileReviewListLoadingState.postValue(LoadingState.NOT_LOADING);
        })));
    }

    public void fetchReviewWithRelationsById(String reviewId) {
        this.reviewLoadingState.setValue(LoadingState.LOADING);

        this.firebaseModel.getReviewById(reviewId, review -> executor.execute(() -> {
            if (review != null) {
                this.localDb.reviewDao().insertAll(review);
            }
            this.reviewWithUser.postValue(this.localDb.reviewDao().getWithRelationsById(reviewId));
            this.reviewLoadingState.postValue(LoadingState.NOT_LOADING);
        }));
    }

    public void uploadReviewImage(Bitmap imageBitmap, String name, Consumer<String> imageUploadCallback) {
        this.firebaseModel.uploadReviewImage(imageBitmap, name, imageUploadCallback);
    }

    public void editReview(Review review, Consumer<Review> onEditSuccessListener, Consumer<String> onEditFailureListener) {
        this.firebaseModel.editReview(review, () -> onEditSuccessListener.accept(review), onEditFailureListener);
    }

    public void addReview(Review review, Consumer<Review> onAddSuccessListener, Consumer<String> onAddFailureListener) {
        this.firebaseModel.addReview(review, (String reviewId) -> {
            review.setId(reviewId);
            onAddSuccessListener.accept(review);
        }, onAddFailureListener);
    }

    public void deleteReview(Review review, Consumer<Review> onDeleteSuccessListener, Consumer<String> onDeleteFailureListener) {
        review.setDeleted(true);
        this.editReview(review, onDeleteSuccessListener, onDeleteFailureListener);
    }
}
