package com.example.recipereviews.models.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.room.RecipeReviewsLocalDb;
import com.example.recipereviews.models.room.RecipeReviewsLocalDbRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecipeModel {

    private static final RecipeModel instance = new RecipeModel();
    private final RecipeReviewsLocalDbRepository localDb = RecipeReviewsLocalDb.getLocalDb();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<LoadingState> recipeDetailsLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private final MutableLiveData<LoadingState> recipeListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
    private MutableLiveData<Recipe> recipe = new MutableLiveData<>();
    private LiveData<List<Recipe>> recipeList;

    private RecipeModel() {
    }

    public static RecipeModel getInstance() {
        return instance;
    }

    public MutableLiveData<LoadingState> getRecipeDetailsLoadingState() {
        return this.recipeDetailsLoadingState;
    }

    public MutableLiveData<LoadingState> getRecipeListLoadingState() {
        return this.recipeListLoadingState;
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        if (this.recipeList == null) {
            this.recipeList = this.localDb.recipeDao().getAll();
            refreshAllRecipes();
        }

        return this.recipeList;
    }

    public LiveData<Recipe> getRecipeById(int id) {
        if (this.recipe.getValue() == null) {
            this.fetchRecipeById(id);
        }

        return this.recipe;
    }

    public LiveData<List<Recipe>> getRecipesBySearchText(String text) {
        return this.localDb.recipeDao().getByName("%" + text + "%");
    }

    public void refreshAllRecipes() {
        this.recipeListLoadingState.setValue(LoadingState.LOADING);
        // TODO: call api i think
        this.executor.execute(() -> {
            List<Recipe> list = new ArrayList<Recipe>() {{
                add(new Recipe(1, "The Best Chili", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
                add(new Recipe(2, "Sausage & Pepperoni Stromboli", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
                add(new Recipe(3, "Lasagna Roll Ups", "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png"));
            }};
            list.forEach(recipe -> this.localDb.recipeDao().insertAll(recipe));
            this.recipeListLoadingState.postValue(LoadingState.NOT_LOADING);
        });
    }

    public void fetchRecipeById(int id) {
        this.recipeDetailsLoadingState.setValue(LoadingState.LOADING);
        this.executor.execute(() -> {
            this.localDb.recipeDao().insertAll(
                    new Recipe(id, "Sausage & Pepperoni Stromboli" + id,
                            "https://feelslikehomeblog.com/wp-content/uploads/2022/02/Worlds-Best-Chili-with-Bacon-FB.png",
                            28,
                            new ArrayList<>(Arrays.asList(
                                    "1 egg",
                                    "1 lb italian sausage",
                                    "1 cup shredded Parmesan cheese",
                                    "1 package sliced pepperoni")),
                            new ArrayList<>(Arrays.asList(
                                    "cook italian sausage in medium skillet until browned",
                                    "Drain on paper towels and crumble into small pieces",
                                    "Heat oven to 450 degrees",
                                    "Place parchment paper on a baking pan and lightly flour")))
            );
            // TODO: call api
            this.recipe.postValue(this.localDb.recipeDao().getById(id));
            this.recipeDetailsLoadingState.postValue(LoadingState.NOT_LOADING);
        });
    }
}
