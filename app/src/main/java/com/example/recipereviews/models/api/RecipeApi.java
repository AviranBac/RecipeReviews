package com.example.recipereviews.models.api;

import com.example.recipereviews.models.entities.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeApi {

    @GET("/recipes/{id}/information/?apiKey=389a18683db1459fae103325e6abc816")
    Call<Recipe> getRecipeById(@Path("id") int id);

    @GET("/recipes/complexSearch/?number=20&apiKey=389a18683db1459fae103325e6abc816")
    Call<GetRecipesDto> getRecipes();
}