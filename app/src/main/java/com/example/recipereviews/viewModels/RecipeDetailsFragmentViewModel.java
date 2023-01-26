package com.example.recipereviews.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.models.RecipeDetailsModel;

public class RecipeDetailsFragmentViewModel extends ViewModel {
    private LiveData<Recipe> recipeData;

    public RecipeDetailsFragmentViewModel(int recipeId) {
        this.recipeData = RecipeDetailsModel.getInstance().getRecipeById(recipeId);
    }

    public LiveData<Recipe> getRecipeData() {
        return this.recipeData;
    }

}
