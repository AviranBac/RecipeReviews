package com.example.recipereviews.models.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipereviews.enums.LoadingState;
import com.example.recipereviews.models.entities.Recipe;
import com.example.recipereviews.models.room.RecipeReviewsLocalDb;
import com.example.recipereviews.models.room.RecipeReviewsLocalDbRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecipeDetailsModel {
    private static final RecipeDetailsModel instance = new RecipeDetailsModel();
    private final RecipeReviewsLocalDbRepository localDb = RecipeReviewsLocalDb.getLocalDb();
    private final Executor executor = Executors.newSingleThreadExecutor();

    private RecipeDetailsModel() {
    }

    public static RecipeDetailsModel getInstance() {
        return instance;
    }

    final public MutableLiveData<LoadingState> EventRecipesDetailsLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);

    private MutableLiveData<Recipe> recipe = new MutableLiveData<>();

    public LiveData<Recipe> getRecipeById(int id) {
        if(recipe.getValue() == null) {
            fetchRecipeById(id);
        }
        return recipe;
    }

    public void fetchRecipeById(int id) {
        this.EventRecipesDetailsLoadingState.setValue(LoadingState.LOADING);
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
            this.EventRecipesDetailsLoadingState.postValue(LoadingState.NOT_LOADING);
        });
    }

}
