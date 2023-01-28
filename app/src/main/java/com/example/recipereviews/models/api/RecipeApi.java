package com.example.recipereviews.models.api;

import com.example.recipereviews.models.entities.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeApi {

    @GET("/recipes/{id}/information/?apiKey=0abb3009e93c41b28eb4d2ab8a53b3f6")
    Call<Recipe> getRecipeById(@Path("id") int id);

    @GET("/recipes/complexSearch/?number=20&apiKey=0abb3009e93c41b28eb4d2ab8a53b3f6")
    Call<GetRecipesDto> getRecipes();
}