package com.example.recipereviews.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipereviews.models.entities.ReviewWithUser;
import com.example.recipereviews.models.models.ReviewListModel;

import java.util.List;

public class ReviewListFragmentViewModel extends ViewModel {
    private LiveData<List<ReviewWithUser>> reviewListData;

    public ReviewListFragmentViewModel(int recipeId) {
        this.reviewListData = ReviewListModel.getInstance().getReviewByRecipeId(recipeId);
    }

    public LiveData<List<ReviewWithUser>> getReviewListDataByRecipeId() {
        return this.reviewListData;
    }
}
