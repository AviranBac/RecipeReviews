package com.example.recipereviews.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipereviews.models.entities.ReviewWithUser;
import com.example.recipereviews.models.models.ReviewModel;

import java.util.List;

public class ReviewListFragmentViewModel extends ViewModel {
    private LiveData<List<ReviewWithUser>> reviewListData;

    public ReviewListFragmentViewModel(int recipeId) {
        this.reviewListData = ReviewModel.getInstance().getReviewByRecipeId(recipeId);
    }

    public LiveData<List<ReviewWithUser>> getReviewListDataByRecipeId() {
        return this.reviewListData;
    }
}
