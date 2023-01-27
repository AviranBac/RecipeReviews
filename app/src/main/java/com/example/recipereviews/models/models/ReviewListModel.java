package com.example.recipereviews.models.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.models.entities.ReviewWithUser;
import com.example.recipereviews.models.firebase.ModelFirebase;
import com.example.recipereviews.models.room.RecipeReviewsLocalDb;
import com.example.recipereviews.models.room.RecipeReviewsLocalDbRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReviewListModel {
    private static final ReviewListModel instance = new ReviewListModel();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final RecipeReviewsLocalDbRepository localDb = RecipeReviewsLocalDb.getLocalDb();
    private final MutableLiveData<LoadingState> eventReviewListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final ModelFirebase firebaseModel = new ModelFirebase();
    private MutableLiveData<List<ReviewWithUser>> reviewList = new MutableLiveData();

    private ReviewListModel() {
    }

    public static ReviewListModel getInstance() {
        return instance;
    }

    public MutableLiveData<LoadingState> getEventReviewListLoadingState() {
        return this.eventReviewListLoadingState;
    }

    public LiveData<List<ReviewWithUser>> getReviewByRecipeId(int recipeId) {
        if (this.reviewList.getValue() == null) {
            this.refreshReviewByRecipeId(recipeId);
        }

        return this.reviewList;
    }

    public void refreshReviewByRecipeId(int recipeId) {
        UserModel.getInstance().refreshUserList(() -> {
            this.eventReviewListLoadingState.setValue(LoadingState.LOADING);

            this.firebaseModel.getReviewsByRecipeId(recipeId, list -> executor.execute(() -> {
                list.forEach(review -> localDb.reviewDao().insertAll(review));
                this.reviewList.postValue(this.localDb.reviewDao().getByRecipeId(recipeId));
                this.eventReviewListLoadingState.postValue(LoadingState.NOT_LOADING);
            }));
        });
    }
}
