package com.example.recipereviews.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipereviews.models.entities.ReviewWithRecipe;
import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.models.ReviewModel;
import com.example.recipereviews.models.models.UserModel;

import java.util.List;

public class ProfileFragmentViewModel extends ViewModel {
    private LiveData<User> profileUser;
    private LiveData<List<ReviewWithRecipe>> reviewListData;

    public ProfileFragmentViewModel() {
        this.profileUser = UserModel.getInstance().getLoggedInUser();
        this.reviewListData = ReviewModel.getInstance().getLoggedInUserReviews();
    }

    public LiveData<User> getProfileUser() {
        return this.profileUser;
    }

    public LiveData<List<ReviewWithRecipe>> getReviewListDataByUserId() {
        return this.reviewListData;
    }
}
