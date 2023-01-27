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
    private final LiveData<User> loggedInUser = UserModel.getInstance().getUserById(UserModel.getInstance().getCurrentUserId());;
    private final LiveData<List<Recipe>> recipeListData = RecipeModel.getInstance().getAllRecipes();;
    private final MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>();;

    public LiveData<User> getLoggedInUser() {
        return this.loggedInUser;
    }

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
