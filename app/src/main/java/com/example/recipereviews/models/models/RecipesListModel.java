package com.example.recipereviews.models.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.room.RecipeReviewsLocalDb;
import com.example.recipereviews.models.room.RecipeReviewsLocalDbRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecipesListModel {
    private static final RecipesListModel instance = new RecipesListModel();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final RecipeReviewsLocalDbRepository localDb = RecipeReviewsLocalDb.getLocalDb();

    private RecipesListModel() {
    }

    public static RecipesListModel getInstance() {
        return instance;
    }

    final public MutableLiveData<LoadingState> EventRecipesListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private LiveData<List<Recipe>> recipeList;

    public LiveData<List<Recipe>> getAllRecipes() {
        if (this.recipeList == null) {
            this.recipeList = this.localDb.recipeDao().getAll();
            refreshAllRecipes();
        }
        return this.recipeList;
    }

    public void refreshAllRecipes() {
        this.EventRecipesListLoadingState.setValue(LoadingState.LOADING);
        // TODO: call api i think
        this.executor.execute(() -> {
            List<Recipe> list = new ArrayList<Recipe>() {{
                add(new Recipe(1, "The Best Chili", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
                add(new Recipe(2, "Sausage & Pepperoni Stromboli", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
                add(new Recipe(3, "Lasagna Roll Ups", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
            }};
            list.forEach(recipe -> this.localDb.recipeDao().insertAll(recipe));
            this.EventRecipesListLoadingState.postValue(LoadingState.NOT_LOADING);
        });
    }

    public LiveData<List<Recipe>> getRecipesBySearchText(String text) {
        return this.localDb.recipeDao().getByName("%" + text + "%");
    }
}
