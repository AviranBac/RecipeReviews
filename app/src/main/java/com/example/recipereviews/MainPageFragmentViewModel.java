package com.example.recipereviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.recipereviews.models.RecipeModel;
import com.example.recipereviews.models.entities.Recipe;

import java.util.List;

public class MainPageFragmentViewModel extends ViewModel {
    private LiveData<List<Recipe>>  recipeListData = RecipeModel.getInstance().getAllRecipes();
    private MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>();

    public LiveData<List<Recipe>> getRecipeListData() {
        return this.recipeListData;
    }

    public LiveData<List<Recipe>> getSearchedRecipes() {
        return Transformations.switchMap(
                this.searchQueryLiveData,
                search -> RecipeModel.getInstance().getRecipesBySearchText(search));
    }

    public void setNameQuery(String name) {
        this.searchQueryLiveData.setValue(name);
    }
}
