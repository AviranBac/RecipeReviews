package com.example.recipereviews.models.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.models.entities.ReviewWithRecipe;
import com.example.recipereviews.models.entities.ReviewWithUser;
import com.example.recipereviews.models.firebase.ModelFirebase;
import com.example.recipereviews.models.room.RecipeReviewsLocalDb;
import com.example.recipereviews.models.room.RecipeReviewsLocalDbRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReviewModel {
    private static final ReviewModel instance = new ReviewModel();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final RecipeReviewsLocalDbRepository localDb = RecipeReviewsLocalDb.getLocalDb();
    private final MutableLiveData<LoadingState> reviewListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final MutableLiveData<LoadingState> profileReviewListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final ModelFirebase firebaseModel = new ModelFirebase();
    private MutableLiveData<List<ReviewWithUser>> reviewList = new MutableLiveData();
    private MutableLiveData<List<ReviewWithRecipe>> profileReviewList = new MutableLiveData();

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
        UserModel.getInstance().refreshUserList(() -> {
            this.reviewListLoadingState.setValue(LoadingState.LOADING);

            this.firebaseModel.getReviewsByRecipeId(recipeId, list -> executor.execute(() -> {
                list.forEach(review -> localDb.reviewDao().insertAll(review));
                this.reviewList.postValue(this.localDb.reviewDao().getByRecipeId(recipeId));
                this.reviewListLoadingState.postValue(LoadingState.NOT_LOADING);
            }));
        });
    }

    public LiveData<List<ReviewWithRecipe>> getReviewByUserId(String userId) {
        if (this.profileReviewList.getValue() == null) {
            this.refreshReviewByUserId(userId);
        }

        return this.profileReviewList;
    }

    public void refreshReviewByUserId(String userId) {
        UserModel.getInstance().refreshUserList(() -> {
            this.profileReviewListLoadingState.setValue(LoadingState.LOADING);

            this.firebaseModel.getReviewsByUserId(userId, list -> executor.execute(() -> {
                list.forEach(review -> localDb.reviewDao().insertAll(review));
                this.profileReviewList.postValue(this.localDb.reviewDao().getByUserId(userId));
                this.profileReviewListLoadingState.postValue(LoadingState.NOT_LOADING);
            }));
        });
    }
}
