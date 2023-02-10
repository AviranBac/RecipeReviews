package com.example.recipereviews.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.models.RecipeModel;

public class RecipeDetailsFragmentViewModel extends ViewModel {
    private MutableLiveData<Recipe> recipeData;

    public RecipeDetailsFragmentViewModel(int recipeId) {
        this.recipeData = RecipeModel.getInstance().getRecipeById(recipeId);
    }

    public LiveData<Recipe> getRecipeData() {
        return this.recipeData;
    }

    public void resetRecipeData() {
        this.recipeData.setValue(null);
    }
}
