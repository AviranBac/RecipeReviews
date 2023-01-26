package com.example.recipereviews.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.models.RecipesListModel;

import java.util.List;

public class MainPageFragmentViewModel extends ViewModel {
    private final LiveData<List<Recipe>> recipeListData = RecipesListModel.getInstance().getAllRecipes();
    private final MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>();

    public LiveData<List<Recipe>> getRecipeListData() {
        return this.recipeListData;
    }

    public LiveData<List<Recipe>> getSearchedRecipes() {
        return Transformations.switchMap(
                this.searchQueryLiveData,
                search -> RecipesListModel.getInstance().getRecipesBySearchText(search));
    }

    public void setNameQuery(String name) {
        this.searchQueryLiveData.setValue(name);
    }
}
