package com.example.recipereviews.models.api;

import com.example.recipereviews.models.entities.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeApi {

    @GET("/recipes/{id}/information/?apiKey=8bcd2769d8474bdd92c8a44b61a95c00")
    Call<Recipe> getRecipeById(@Path("id") int id);

    @GET("/recipes/complexSearch/?number=20&apiKey=8bcd2769d8474bdd92c8a44b61a95c00")
    Call<GetRecipesDto> getRecipes();
}