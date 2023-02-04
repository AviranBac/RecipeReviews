package com.example.recipereviews.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.entities.Review;
import com.example.recipereviews.models.entities.User;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<User> userData = new MutableLiveData<>();
    private MutableLiveData<Recipe> recipeData = new MutableLiveData<>();
    private MutableLiveData<Review> reviewData = new MutableLiveData<>();

    public LiveData<User> getUserData() {
        return userData;
    }

    public LiveData<Recipe> getRecipeData() {
        return recipeData;
    }

    public LiveData<Review> getReviewData() {
        return reviewData;
    }

    public void setUserData(User user) {
        this.userData.setValue(user);
    }

    public void setRecipeData(Recipe recipe) {
        this.recipeData.setValue(recipe);
    }

    public void setReviewData(Review review) {
        this.reviewData.setValue(review);
    }
}
