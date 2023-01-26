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

    private ReviewListModel() {
    }

    public static ReviewListModel getInstance() {
        return instance;
    }
    private final ModelFirebase firebaseModel = new ModelFirebase();

    final public MutableLiveData<LoadingState> EventReviewListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private MutableLiveData<List<ReviewWithUser>> reviewList = new MutableLiveData();

    public LiveData<List<ReviewWithUser>> getReviewByRecipeId(int recipeId) {
        if (this.reviewList.getValue() == null) {
            refreshReviewByRecipeId(recipeId);
        }
        return this.reviewList;
    }

    public void refreshReviewByRecipeId(int recipeId) {
        Model.getInstance().refreshUserList(unused -> {
            this.EventReviewListLoadingState.setValue(LoadingState.LOADING);

            this.firebaseModel.getReviewsByRecipeId(recipeId,list-> executor.execute(()->{
                list.forEach(review -> localDb.reviewDao().insertAll(review));
                this.reviewList.postValue(this.localDb.reviewDao().getByRecipeId(recipeId));
                this.EventReviewListLoadingState.postValue(LoadingState.NOT_LOADING);
            }));
        });
    }
}
