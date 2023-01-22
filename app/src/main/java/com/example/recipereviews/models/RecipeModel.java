package com.example.recipereviews.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.firebase.ModelFirebase;
import com.example.recipereviews.models.room.RecipeReviewsLocalDb;
import com.example.recipereviews.models.room.RecipeReviewsLocalDbRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecipeModel {
    private static final RecipeModel instance = new RecipeModel();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final ModelFirebase modelFirebase = new ModelFirebase();
    private final RecipeReviewsLocalDbRepository localDb = RecipeReviewsLocalDb.getLocalDb();

    private RecipeModel() {
    }

    public static RecipeModel getInstance() {
        return instance;
    }

    public Executor getExecutor() {
        return this.executor;
    }

    public enum LoadingState {
        LOADING,
        NOT_LOADING
    }

    final public MutableLiveData<LoadingState> EventRecipesListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);
    private LiveData<List<Recipe>> recipeList;

    public LiveData<List<Recipe>> getAllRecipes() {
        if (recipeList == null) {
            recipeList = localDb.recipeDao().getAll();
            refreshAllRecipes();
        }
        return recipeList;
    }

    public void refreshAllRecipes() {
        EventRecipesListLoadingState.setValue(LoadingState.LOADING);
        // TODO: call api i think
        executor.execute(() -> {
            List<Recipe> list = new ArrayList<Recipe>() {{
                add(new Recipe(1, "The Best Chili", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
                add(new Recipe(2, "Sausage & Pepperoni Stromboli", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
                add(new Recipe(3, "Lasagna Roll Ups", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
            }};
            for (Recipe recipe : list) {
                localDb.recipeDao().insertAll(recipe);
            }
            // update local last update
            EventRecipesListLoadingState.postValue(LoadingState.NOT_LOADING);
        });
    }

    public LiveData<List<Recipe>> getRecipesBySearchText(String text) {
        return  localDb.recipeDao().getAllByName("%" + text + "%");
    }
}
