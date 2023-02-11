package com.example.recipereviews.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.entities.User;
import com.example.recipereviews.models.models.RecipeModel;
import com.example.recipereviews.models.models.UserModel;

import java.util.List;

public class MainPageFragmentViewModel extends ViewModel {
    private final LiveData<User> loggedInUser = UserModel.getInstance().getLoggedInUser();
    private final MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>("");
    private final LiveData<List<Recipe>> searchedRecipes = Transformations.switchMap(
            this.searchQueryLiveData,
            search -> RecipeModel.getInstance().getRecipesBySearchText(search)
    );

    public LiveData<User> getLoggedInUser() {
        return this.loggedInUser;
    }

    public LiveData<List<Recipe>> getSearchedRecipes() {
        return this.searchedRecipes;
    }

    public void setNameQuery(String name) {
        this.searchQueryLiveData.setValue(name);
    }
}
