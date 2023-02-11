package com.example.recipereviews.models.api;

import com.example.recipereviews.models.entities.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeApi {

    @GET("/recipes/{id}/information/")
    Call<Recipe> getRecipeById(@Path("id") int id, @Query("apiKey") String apiKey);

    @GET("/recipes/complexSearch/?number=20")
    Call<GetRecipesDto> getRecipes(@Query("apiKey") String apiKey);
}