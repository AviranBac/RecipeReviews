package com.example.recipereviews.models.api;

import com.example.recipereviews.models.entities.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeApi {

    @GET("/recipes/{id}/information/?apiKey=7f917292117b4aa497e6b5d787d7f06b")
    Call<Recipe> getRecipeById(@Path("id") int id);

    @GET("/recipes/complexSearch/?number=20&apiKey=7f917292117b4aa497e6b5d787d7f06b")
    Call<GetRecipesDto> getRecipes();
}